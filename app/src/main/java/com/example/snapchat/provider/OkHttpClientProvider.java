package com.example.snapchat.provider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.snapchat.utils.CustomTrustManager;
import com.example.snapchat.utils.JwtInterceptor;
import com.example.snapchat.utils.JwtManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

@Singleton
public interface OkHttpClientProvider extends Provider<OkHttpClient> {

    @NonNull
    OkHttpClient get();

    @Singleton
    class Impl implements OkHttpClientProvider {
        private final OkHttpClient[] clients = new OkHttpClient[1 << 2];

        @Inject
        public Impl() {
        }

        @NonNull
        @Override
        public OkHttpClient get() {
            try {
                return get(true, false);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                Log.e("e",e.toString());
                throw new RuntimeException(e);
            }
        }

        @NonNull
        private synchronized OkHttpClient get(boolean isOAuthBased, boolean usesOfflineCache) throws NoSuchAlgorithmException, KeyManagementException {
            final int index = 0;
            OkHttpClient client = clients[index];
            if (client == null) {
                TrustManager[] trustAllCertificates = new CustomTrustManager[]{ new CustomTrustManager()};
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0]);
                SSLContext.setDefault(sslContext);
                // allow all host
                builder.hostnameVerifier((hostname, session) -> true);
                builder.addInterceptor(new JwtInterceptor());

                //builder.sslSocketFactory(RxUtils.createSSLSocketFactory((X509Certificate)CertificateUtil.certificate),new RxUtils.TrustAllManager((X509Certificate) CertificateUtil.certificate)) ;
                /*List<Interceptor> interceptors = builder.interceptors();
                if (usesOfflineCache) {
                    final File cacheDirectory = new File(context.getFilesDir(), "http-cache");
                    if (!cacheDirectory.exists()) {
                        cacheDirectory.mkdirs();
                    }
                    final Cache cache = new Cache(cacheDirectory, cacheSize);
                    builder.cache(cache);
                    interceptors.add(new StaleIfErrorInterceptor());
                    interceptors.add(new StaleIfErrorHandlingInterceptor());
                    builder.networkInterceptors().add(new NoCacheHeaderStrippingInterceptor());
                }
                interceptors.add(new UserAgentInterceptor(
                        System.getProperty("http.agent") + " " +
                                context.getString(R.string.app_name) + "/" +
                                BuildConfig.APPLICATION_ID + "/" +
                                BuildConfig.VERSION_NAME));
                if (isOAuthBased) {
                    interceptors.add(new OauthHeaderRequestInterceptor(context));
                    interceptors.add(oauthRefreshTokenAuthenticator);
                }
                interceptors.add(new NewVersionBroadcastInterceptor());
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    interceptors.add(loggingInterceptor);
                }
                builder.authenticator(oauthRefreshTokenAuthenticator);*/
                client = builder.build();
                clients[index] = client;

            }
            return client;
        }
    }
}
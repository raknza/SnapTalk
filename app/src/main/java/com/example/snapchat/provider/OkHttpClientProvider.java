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
        public synchronized OkHttpClient get() {
            final int index = 0;
            OkHttpClient client = clients[index];
            if (client == null) {
                TrustManager[] trustAllCertificates = new CustomTrustManager[]{ new CustomTrustManager()};
                SSLContext sslContext = null;
                try {
                    sslContext = SSLContext.getInstance("TLSv1.2");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                try {
                    sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
                } catch (KeyManagementException e) {
                    throw new RuntimeException(e);
                }
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0]);
                SSLContext.setDefault(sslContext);
                // allow all host
                builder.hostnameVerifier((hostname, session) -> true);
                builder.addInterceptor(new JwtInterceptor());

                client = builder.build();
                clients[index] = client;

            }
            return client;
        }
    }
}
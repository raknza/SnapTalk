package com.example.snapchat.provider;

import android.content.Context;

import com.example.snapchat.api.LoginAPI;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;


@Module
@InstallIn(SingletonComponent.class)
public abstract class DefaultModule {

    @Binds
    public abstract RetrofitProvider provideRetrofitProvider(RetrofitProvider.Impl impl);

    @Binds
    public abstract OkHttpClientProvider provideOkHttpClientProvider(OkHttpClientProvider.Impl impl);



    @InstallIn(SingletonComponent.class)
    @EntryPoint
    public interface ProviderEntryPoint {
        public LoginAPI getLoginAPI();
        public OkHttpClientProvider getOkHttpClientProvider();
        public Gson getGSon();

    }

    @Module
    @InstallIn(SingletonComponent.class)
    public static class ProvideModule {

        Context myContext;
        public void setContext(Context context){ myContext = context;}

        @Singleton
        @Provides
        Retrofit provideRetrofit(RetrofitProvider.Impl impl){
            return impl.get();
        }

        @Singleton
        @Provides
        OkHttpClient provideOkHttpClient(OkHttpClientProvider.Impl impl){
            return impl.get();
        }

        @Singleton
        @Provides
        Gson provideGson() {
            return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        }

        @Singleton
        @Provides
        Context provideContext() { return myContext; }
    }

}




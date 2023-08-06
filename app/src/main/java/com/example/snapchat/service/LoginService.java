package com.example.snapchat.service;

import static com.example.snapchat.http.ApiConstants.URL_MY_USER_INFO;

import androidx.annotation.NonNull;

import com.example.snapchat.data.response.LoginResponse;
import com.example.snapchat.data.response.SignupResponse;
import com.example.snapchat.http.ApiConstants;
import com.example.snapchat.provider.RetrofitProvider;

import java.util.Map;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LoginService {

    /**
     * A Provider implementation for LoginService.
     */
    @Module
    @InstallIn(SingletonComponent.class)
    class Provider {
        @Singleton
        @Provides
        public LoginService get(@NonNull RetrofitProvider retrofitProvider) {
            return retrofitProvider.get().create(LoginService.class);
        }
    }


    /**
     * If there are form validation errors, this call will fail with 400 or 409 error code.
     * In case of validation errors the response body will be
     */
    @NonNull
    @FormUrlEncoded
    @POST(ApiConstants.URL_REGISTRATION)
    Call<SignupResponse> register(@FieldMap Map<String, String> parameters);


    /**
     * Reset password for account associated with an email address.
     */
    @NonNull
    @FormUrlEncoded
    @PUT(ApiConstants.URL_USER_INFO_UPDATE)
    Call<ResponseBody> updateUserInfo(@FieldMap Map<String, String> parameters);

    @NonNull
    @FormUrlEncoded
    @POST(ApiConstants.URL_LOGIN)
    Call<LoginResponse> login(@FieldMap Map<String, String> parameters);

    /**
     * @return basic profile information for currently authenticated user.
     */
    @NonNull
    @FormUrlEncoded
    @GET(URL_MY_USER_INFO)
    Call<ResponseBody> getProfile(@Path("username") String username);

}
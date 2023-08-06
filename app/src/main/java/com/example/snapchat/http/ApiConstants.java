package com.example.snapchat.http;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ApiConstants {

    public static final String USER_NAME = "username";

    public static final String URL_MY_USER_INFO = "/api/user/{username}";

    public static final String URL_ACCESS_TOKEN = "/oauth2/access_token/";

    public static final String URL_USER_INFO_UPDATE = "/api/user/{username}";

    public static final String URL_LOGIN = "api/user/login";

    public static final String URL_REGISTRATION = "/api/user/registration";

    public static final String TOKEN_TYPE_ACCESS = "access_token";

    public static final String TOKEN_TYPE_REFRESH = "refresh_token";

    public static final String TOKEN_TYPE_JWT = "jwt";

    public static final String GRANT_TYPE_PASSWORD = "password";

    public static final String VALIDATION_DECISIONS = "validation_decisions";


    @StringDef({TOKEN_TYPE_ACCESS, TOKEN_TYPE_REFRESH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TokenType {
    }



}
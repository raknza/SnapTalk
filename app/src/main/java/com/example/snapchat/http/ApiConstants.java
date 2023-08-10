package com.example.snapchat.http;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ApiConstants {

    public static final String URL_USER_INFO = "/api/user";
    public static final String URL_OTHER_USER_INFO = "/api/user/{username}";
    public static final String URL_CHECK_AUTH = "/api/user/auth";
    public static final String URL_SEARCH_USER = "/api/user/{username}";
    public static final String URL_USER_INFO_UPDATE = "/api/user";
    public static final String URL_MY_CONTACTS = "/api/contact/contactlist";
    public static final String URL_CREATE_CONTACT = "/api/contact/create";
    public static final String URL_LOGIN = "api/user/login";
    public static final String URL_REGISTRATION = "/api/user/registration";


}
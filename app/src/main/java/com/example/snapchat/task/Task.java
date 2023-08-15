package com.example.snapchat.task;

import android.os.AsyncTask;
/**
 * This class is deprecated. Issues in it's
 * implementation include the lack of a guarantee of the
 * result not being delivered to the callback method
 * after cancellation.
 * <p>
 * New asynchronous HTTP request implementations should
 * consider using Retrofit's asynchronous API. If that's
 * not sufficient, or if the implementation is not of an
 * HTTP call, then AsyncTask or Loader implementations
 * should be considered instead.
 */
public abstract class Task<T> extends AsyncTask<Void, Void, T> {
    @Override
    protected void onCancelled(T unused) {
        super.onCancelled(unused);
    }

    public abstract void onException(Exception ex);
}


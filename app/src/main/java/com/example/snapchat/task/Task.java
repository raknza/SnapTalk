package com.example.snapchat.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.snapchat.provider.DefaultModule;

import java.lang.ref.WeakReference;

import dagger.hilt.android.EntryPointAccessors;

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

    @Nullable
    private WeakReference<TaskProgressCallback> progressCallback;

    @Nullable
    private WeakReference<TaskMessageCallback> messageCallback;


    private final CallTrigger callTrigger;

    public Task() {
        this.callTrigger = CallTrigger.LOADING_UNCACHED;
    }

    public void setTaskProcessCallback(@Nullable TaskProcessCallback callback) {
        setProgressCallback(callback);
        setMessageCallback(callback);
    }

    public void setProgressCallback(@Nullable TaskProgressCallback callback) {
        if (callback == null) {
            progressCallback = null;
        } else {
            progressCallback = new WeakReference<>(callback);
        }
    }

    public void setMessageCallback(@Nullable TaskMessageCallback callback) {
        messageCallback = callback == null ? null : new WeakReference<>(callback);
    }

    @Nullable
    private TaskProgressCallback getProgressCallback() {
        return progressCallback == null ? null : progressCallback.get();
    }

    @Nullable
    private TaskMessageCallback getMessageCallback() {
        return messageCallback == null ? null : messageCallback.get();
    }

    @Override
    protected void onPreExecute() {
        final TaskProgressCallback callback = getProgressCallback();
        if (callback != null) {
            callback.startProcess();
        }
    }

    @Override
    protected void onCancelled(T unused) {
        super.onCancelled(unused);
        this.onCancelled();
    }

    @Override
    protected void onPostExecute(T unused) {
        super.onPostExecute(unused);
    }


    /**
     * @return The {@link MessageType} based on the {@link #callTrigger}.
     */
    private MessageType getMessageType() {
        switch (callTrigger) {
            case USER_ACTION:
                return MessageType.DIALOG;
            case LOADING_CACHED:
            case LOADING_UNCACHED:
            default:
                return MessageType.FLYIN_ERROR;
        }
    }

    protected void handleException(Exception e) {
        new Handler(Looper.getMainLooper()).post(() -> onException(e));
    }

    public abstract void onException(Exception ex);
}

enum MessageType {
    FLYIN_ERROR, FLYIN_WARNING, FLYIN_INFO, ERROR, WARNING, INFO, EMPTY, DIALOG
}

interface TaskMessageCallback {
    void onMessage(@NonNull MessageType messageType, @NonNull String message);
}

interface TaskProcessCallback extends TaskProgressCallback, TaskMessageCallback {
}

interface TaskProgressCallback {
    void startProcess();
    void finishProcess();

    class ProgressViewController implements TaskProgressCallback {
        @NonNull
        private final View progressView;

        public ProgressViewController(@NonNull final View progressView) {
            this.progressView = progressView;
        }

        @Override
        public void startProcess() {
            progressView.setVisibility(View.VISIBLE);
        }

        @Override
        public void finishProcess() {
            progressView.setVisibility(View.GONE);
        }
    }
}

enum CallTrigger {
    /**
     * A request initiated by a user action.
     */
    USER_ACTION(MessageType.DIALOG),
    /**
     * A request initiated to load some data, that's being cached
     * by the application.
     */
    LOADING_CACHED(MessageType.FLYIN_ERROR),
    /**
     * A request initiated to load some data, that's not being
     * cached by the application.
     */
    LOADING_UNCACHED(MessageType.FLYIN_ERROR);

    /**
     * The message type that's associated with the request type.
     */
    @NonNull
    private final MessageType messageType;

    /**
     * Create a new instance of an HTTP request type.
     *
     * @param messageType The message type that's associated with
     *                    the request type.
     */
    CallTrigger(@NonNull final MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * @return The message type that's associated with the request
     *         type.
     */
    @NonNull
    MessageType getMessageType() {
        return messageType;
    }
}
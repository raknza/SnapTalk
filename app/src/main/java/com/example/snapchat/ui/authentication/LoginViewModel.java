package com.example.snapchat.ui.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.snapchat.data.response.LoginResponse;
import com.example.snapchat.task.LoginTask;
import com.example.snapchat.utils.JwtManager;



public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Boolean> inLoginProcess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> loginMessage = new MutableLiveData<>();
    public final MutableLiveData<String> username = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();

    public LoginViewModel(){
        inLoginProcess.setValue(false);
        loginSuccess.setValue(false);
    }

    public LiveData<Boolean> getInLoginProcess() {
        return inLoginProcess;
    }
    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public LiveData<String> getLoginMessage() { return loginMessage; }

    @SuppressLint("StaticFieldLeak")
    public void login(Context context){
        Bundle bundle = new Bundle();
        bundle.putString("username",username.getValue());
        bundle.putString("password",password.getValue());
        inLoginProcess.setValue(true);
        LoginTask logintask = new LoginTask(context, bundle) {
            @Override
            protected void onPostExecute(LoginResponse result){
                super.onPostExecute(result);
                if(result != null) {
                    JwtManager.saveJwtToSharedPreferences(context,result.token);
                    loginSuccess.setValue(true);
                    loginMessage.setValue(context.getString(R.string.login_success));
                }
                else {
                    inLoginProcess.setValue(false);
                    loginMessage.setValue(context.getString(R.string.login_failed));
                }
            }
        };
        logintask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
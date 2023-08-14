package com.example.snapchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.model.User;
import com.example.snapchat.data.response.AuthResponse;
import com.example.snapchat.task.CheckAuthTask;
import com.example.snapchat.task.GetContactTask;
import com.example.snapchat.task.GetUserInfoTask;
import com.example.snapchat.utils.DataManager;

import java.util.Arrays;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isAuthValid ;
    private LiveData<User> user;
    public MainViewModel(){
        isAuthValid = new MutableLiveData<>();
        user =  DataManager.getInstance().getUserLiveData();
        isAuthValid.setValue(true);
    }

    public MutableLiveData<Boolean> getIsAuthValid() { return isAuthValid; }
    public LiveData<User> getUser() { return user; }

    @SuppressLint("StaticFieldLeak")
    public void checkAuth(Context context){
        CheckAuthTask checkAuthTask = new CheckAuthTask(context) {
            @Override
            protected void onPostExecute(AuthResponse result){
                super.onPostExecute(result);
                if(!result.isTokenValid) {
                    isAuthValid.setValue(false);
                }
            }
        };
        checkAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

package com.example.snapchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.snapchat.data.model.Contact;
import com.example.snapchat.data.model.User;
import com.example.snapchat.data.response.AuthResponse;
import com.example.snapchat.task.CheckAuthTask;
import com.example.snapchat.task.GetContactTask;
import com.example.snapchat.task.GetUserInfoTask;

import java.util.Arrays;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isAuthValid ;
    private MutableLiveData<List<Contact>> contactList ;

    private MutableLiveData<User> user;
    public MainViewModel(){
        isAuthValid = new MutableLiveData<>();
        contactList = new MutableLiveData<>();
        user = new MutableLiveData<>();
        isAuthValid.setValue(true);
    }

    public MutableLiveData<Boolean> getIsAuthValid() { return isAuthValid; }
    public MutableLiveData<List<Contact>> getContactList() { return contactList; }

    public MutableLiveData<User> getUser() { return user; }

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

    @SuppressLint("StaticFieldLeak")
    public void getContactList(Context context){
        GetContactTask getContactTask = new GetContactTask(context) {
            @Override
            protected void onPostExecute(Contact[] result){
                super.onPostExecute(result);
                if(result != null) {
                    contactList.setValue(Arrays.asList(result));
                }
            }
        };
        getContactTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    public void getUserInfo(Context context){
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask(context) {
            @Override
            protected void onPostExecute(User result){
                super.onPostExecute(result);
                if(result != null) {
                    Log.e("User", result.username);
                    user.setValue(result);
                }
            }
        };
        getUserInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

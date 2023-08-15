package com.example.snapchat.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.snapchat.data.model.User;
import com.example.snapchat.data.response.EmptyResponse;
import com.example.snapchat.task.UpdateProfileTask;
import com.example.snapchat.utils.DataManager;

public class ProfileViewModel extends ViewModel {

    public final MutableLiveData<String> avatarUrl = new MutableLiveData<>();
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> profile = new MutableLiveData<>();
    public final MutableLiveData<Boolean> modifySuccess = new MutableLiveData<>();

    public ProfileViewModel() {
        User user = DataManager.getInstance().getUserLiveData().getValue();
        avatarUrl.setValue(user.avatar);
        name.setValue(user.name);
        email.setValue(user.email);
        profile.setValue(user.profile);
        modifySuccess.setValue(false);
    }

    @SuppressLint("StaticFieldLeak")
    public void updateUserInfo(Context context){
        Bundle bundle = new Bundle();
        bundle.putString("avatar",avatarUrl.getValue());
        bundle.putString("name",name.getValue());
        bundle.putString("email",email.getValue());
        bundle.putString("profile",profile.getValue());
        UpdateProfileTask updateProfileTask = new UpdateProfileTask(context,bundle){
            @Override
            protected void onPostExecute(EmptyResponse result){
                if(result != null){
                    User user = new User(DataManager.getInstance().getUserLiveData().getValue());
                    user.avatar = avatarUrl.getValue();
                    user.name = name.getValue();
                    user.email = email.getValue();
                    user.profile = profile.getValue();
                    DataManager.getInstance().updateUser(user);
                    modifySuccess.postValue(true);
                }
                else
                    modifySuccess.postValue(false);
            }
        };
        updateProfileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


}
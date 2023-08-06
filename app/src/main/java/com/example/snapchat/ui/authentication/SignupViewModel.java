package com.example.snapchat.ui.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.example.snapchat.data.response.SignupResponse;
import com.example.snapchat.task.SignupTask;
import com.example.snapchat.utils.JwtManager;

import javax.annotation.Nullable;

public class SignupViewModel extends ViewModel {
    private final MutableLiveData<SignupFormState> signupFormState = new MutableLiveData<SignupFormState>();
    private final MutableLiveData<Boolean> inSignupProcess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> signupSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> signupMessage = new MutableLiveData<>();
    public final MutableLiveData<String> username = new MutableLiveData<>();
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>();


    public SignupViewModel(){
        inSignupProcess.setValue(false);
        signupSuccess.setValue(false);
        signupFormState.setValue(new SignupFormState(null, null,null,null,true));
    }

    public LiveData<SignupFormState> getSignupFormState() {
        return signupFormState;
    }
    public LiveData<Boolean> getInSignupProcess() {
        return inSignupProcess;
    }
    public LiveData<Boolean> getSignupSuccess() { return signupSuccess; }
    public LiveData<String> getSignupMessage() { return signupMessage; }

    public void updateSignupForm(@Nullable String username, @Nullable String email, @Nullable String password, @Nullable String confirmPassword){
        signupFormState.setValue(signupFormState.getValue().dataChanged(username,email, password,confirmPassword));
    }

    @SuppressLint("StaticFieldLeak")
    public void signup(Context context){
        Bundle bundle = new Bundle();
        bundle.putString("username",username.getValue());
        if(email.getValue() != null)
            bundle.putString("email",email.getValue());
        bundle.putString("password",password.getValue());
        inSignupProcess.setValue(true);
        SignupTask signupTask = new SignupTask(context, bundle) {
            @Override
            protected void onPostExecute(SignupResponse result){
                super.onPostExecute(result);
                if(result != null) {
                    JwtManager.saveJwtToSharedPreferences(context,result.token);
                    signupSuccess.setValue(true);
                    signupMessage.setValue(result.message);
                }
                else {
                    signupSuccess.setValue(false);
                    signupMessage.setValue(context.getString(R.string.signup_failed));
                }
                inSignupProcess.setValue(false);
            }
        };
        signupTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

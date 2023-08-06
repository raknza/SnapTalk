package com.example.snapchat.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.myapplication.databinding.ActivitySignUpBinding;

public class SignupActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    private ActivitySignUpBinding binding;
    EditText usernameEditText;
    EditText passwordEditText ;
    EditText emailEditText ;
    EditText confirmPasswordEditText ;
    Button signupButton ;
    ProgressBar loadingProgressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        usernameEditText = binding.username;
        emailEditText = binding.email;
        passwordEditText = binding.password;
        confirmPasswordEditText = binding.confirmPassword;

        signupButton = binding.signUp;
        loadingProgressBar = binding.loading;

        // progressbar visible update
        signupViewModel.getInSignupProcess().observe(this, newData -> {
            loadingProgressBar.setVisibility(newData == true? View.VISIBLE:View.GONE);
        });
        // signup event callback
        signupViewModel.getSignupSuccess().observe(this, signupResult -> {
            if(signupResult == true)
                onSignupSuccess();
        });
        // check is data in form valid
        signupViewModel.getSignupFormState().observe(this, signupFormState -> {
            if (signupFormState == null) {
                return;
            }
            signupButton.setEnabled(signupFormState.isDataValid());
            if (signupFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(signupFormState.getUsernameError()));
            }
            if(signupFormState.getEmailError() != null){
                emailEditText.setError(getString((signupFormState.getEmailError())));
            }
            if (signupFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(signupFormState.getPasswordError()));
            }
            if (signupFormState.getConfirmPasswordError() != null) {
                confirmPasswordEditText.setError(getString(signupFormState.getConfirmPasswordError()));
            }

        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                signupViewModel.updateSignupForm(usernameEditText.getText().toString(), emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),confirmPasswordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);

        signupButton.setOnClickListener(v -> signupViewModel.signup(getApplicationContext()));

        binding.setViewModel(signupViewModel);
        binding.setLifecycleOwner(this);
    }

    private void onSignupSuccess(){
        finish();
    }
}
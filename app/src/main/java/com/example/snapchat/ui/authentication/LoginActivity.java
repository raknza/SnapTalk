package com.example.snapchat.ui.authentication;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.snapchat.MainActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        Button loginButton = binding.login;
        ProgressBar loadingProgressBar = binding.loading;

        // progressbar visible update
        loginViewModel.getInLoginProcess().observe(this, newData -> {
            loadingProgressBar.setVisibility(newData == true?View.VISIBLE:View.GONE);
        });
        // login event callback
        loginViewModel.getLoginSuccess().observe(this, loginResult -> {
            if(loginResult == true)
                onLoginSuccess();
        });

        loginButton.setOnClickListener(v -> loginViewModel.login(getApplicationContext()));
        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        TextView textViewSignup = binding.signUp;
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                openSignupPage();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_blue_dark));
            }
        };
        SpannableString spannableString = new SpannableString(textViewSignup.getText());
        spannableString.setSpan(clickableSpan, textViewSignup.getText().length() - 7, textViewSignup.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSignup.setText(spannableString);
        textViewSignup.setMovementMethod(LinkMovementMethod.getInstance());


    }

    private void onLoginSuccess(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void openSignupPage(){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



}
package com.example.snapchat.ui.authentication;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

/**
 * Data validation state of the sign-up form.
 */
class SignupFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer confirmPasswordError;
    private boolean isDataValid;
    SignupFormState(Integer usernameError, Integer emailError,Integer passwordError, Integer confirmPasswordError, boolean isDataValid){
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.emailError = emailError;
        this.confirmPasswordError = confirmPasswordError;
        this.isDataValid = isDataValid;
    }
    SignupFormState(SignupFormState signupFormState){
        this.usernameError = signupFormState.usernameError;
        this.passwordError = signupFormState.passwordError;
        this.emailError = signupFormState.emailError;
        this.confirmPasswordError = signupFormState.confirmPasswordError;
        this.isDataValid = signupFormState.isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() { return passwordError; }
    @Nullable
    Integer getEmailError(){ return emailError; }

    @Nullable
    Integer getConfirmPasswordError() {
        return confirmPasswordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    public SignupFormState dataChanged(@Nullable String username, @Nullable String email, @Nullable String password, @Nullable String confirmPassword) {

        this.usernameError = !isUserNameValid(username)?R.string.invalid_username:null;
        this.emailError = !isValidEmail(email)?R.string.invalid_email:null;
        this.passwordError = !isPasswordValid(password)?R.string.invalid_password:null;
        this.confirmPasswordError = !isConfirmPasswordValid(password,confirmPassword)?R.string.invalid_confirmPassword:null;
        isDataValid = this.usernameError == null && this.emailError == null && this.passwordError == null && this.confirmPasswordError == null;
        return new SignupFormState(this);
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    private boolean isValidEmail(String email) {
        if(email == null || email.isEmpty())
            return true;
        else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
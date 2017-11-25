package com.alex.greenroute.presentation.screens.tutorial.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alex.greenroute.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alex on 19.10.2015.
 */
public class FragmentLogin extends Fragment {
    /**
     * The container view which has layout change animations turned on. In this sample, this view
     * is a {@link android.widget.LinearLayout}.
     */
    @BindView(R.id.loginLayoutContainer)
    ViewGroup mContainerView;

    @BindView(R.id.text_email)
    EditText email;

    @BindView(R.id.text_password)
    AppCompatEditText password;

    @BindView(R.id.buttonLogin)
    Button login;

    @BindView(R.id.buttonTest)
    Button test;

    @BindView(R.id.buttonForgotPassword)
    Button resetPass;

    AppCompatEditText confirmPassword;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_login, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.buttonLogin)
    void onLogin() {
        //Login
        if(mContainerView.getChildCount() > 3) {
            signUp();
        } else {
            logIn();
        }
    }

    @OnClick(R.id.buttonTest)
    void test() {
        addItem();
    }

    @OnClick(R.id.buttonForgotPassword)
    void onForgotPassword() {
        showResetPasswordDialog();
    }

    private void showResetPasswordDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.input_title_reset_password)
                .content(R.string.input_content_reset_password)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Email", email.getText(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        sendResetEmail(input);
                    }
                }).show();
    }

    private void sendResetEmail(CharSequence emailAddress) {
        if (TextUtils.isEmpty(emailAddress) || isInvalidEmail(emailAddress.toString())) {
            showErrorDialog(R.string.sign_up_email_error);
        } else {
            //networkHandler.sendResetPasswordEmail(emailAddress.toString());
            email.setText(emailAddress);
        }
    }

    private void logIn() {
        String eml = email.getText().toString();
        if(isInvalidEmail(eml)) {
            showErrorDialog(R.string.sign_up_email_error);
            return;
        }
        //networkHandler.loginUser(eml, password.getText().toString(), this);
    }

    private void signUp() {
        String pwd = password.getText().toString();
        String pwdConf = confirmPassword.getText().toString();

        String eml = email.getText().toString();
        /*if(!validatePasswords(userDataPassword1, userDataPassword2) &&
                isInvalidEmail(userDataEmail)) {

        }*/
        if(isInvalidEmail(eml)) {
            showErrorDialog(R.string.sign_up_email_error);
            return;
        }

        if(TextUtils.isEmpty(pwd)) {
            showErrorDialog(R.string.sign_up_password_empty);
            return;
        }

        if(pwd.equals(pwdConf)) {
            //networkHandler.registerUserAndLogin(eml, pwd, this);
        } else {
            showErrorDialog(R.string.sign_up_passwords_error);
        }
    }

    private void showErrorDialog(int content) {
        new MaterialDialog.Builder(getActivity())
                .title("Error")
                .content(content)
                .positiveText("OK")
                .show();
    }

    ViewGroup confirmPasswordLayout;

    private void switchToSignUp() {
        // Instantiate a new "row" view.
        confirmPasswordLayout = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                R.layout.confirm_password_edit_text, mContainerView, false);

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(confirmPasswordLayout, 2);

        confirmPassword = (AppCompatEditText) confirmPasswordLayout.findViewById(R.id.text_confirm_password);
        /*textInputLayoutConfirmPassword = (TextInputLayout) confirmPasswordLayout.findViewById(R.id.text_input_layout_confirm_password);*/

        login.setText(R.string.login_register);
        test.setText(R.string.login_back);
    }

    private void switchToLogIn() {
        mContainerView.removeView(confirmPasswordLayout);

        login.setText(R.string.login);
        test.setText(R.string.register);
    }

    private void addItem() {
        if(mContainerView.getChildCount() > 3) {
            switchToLogIn();
        } else {
            switchToSignUp();
        }
    }

    private boolean isInvalidEmail(String strEmail) {
        return false;
    }

    private boolean validatePasswords(String password1, String password2) {
        if(!password1.equals(password2)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }
}

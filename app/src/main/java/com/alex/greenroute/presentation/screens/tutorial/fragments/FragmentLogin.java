package com.alex.greenroute.presentation.screens.tutorial.fragments;

import android.content.Intent;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.alex.greenroute.R;
import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.component.MiscUtils;
import com.alex.greenroute.data.DataRepository;
import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.presentation.screens.main.MainActivity;
import com.alex.greenroute.presentation.screens.tutorial.AuthCallbacks;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alex on 19.10.2015.
 */
public class FragmentLogin extends Fragment implements AuthCallbacks {
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

    @Inject
    DataRepository dataRepository;

    @Inject
    PrefsRepository prefsRepository;

    AppCompatEditText confirmPassword;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        GreenApplication.component().inject(this);
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
        dataRepository.login(eml, password.getText().toString(), this);
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
            dataRepository.register(eml, pwd, this);
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

        confirmPassword = confirmPasswordLayout.findViewById(R.id.text_confirm_password);
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
        return (MiscUtils.readEmails(strEmail) == null);
    }

    private boolean validatePasswords(String password1, String password2) {
        if(!password1.equals(password2)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    @Override
    public void onLoginSuccessful() {
        prefsRepository.setUsername(email.getText().toString());
        prefsRepository.setPassword(password.getText().toString());

        startActivity(new Intent(getContext(), MainActivity.class));
    }

    @Override
    public void onRegisterSuccessful() {
        logIn();
    }

    @Override
    public void onError(String err) {
        new MaterialDialog.Builder(getContext())
                .title("Error")
                .content(err)
                .positiveText("OK")
                .show();
    }
}

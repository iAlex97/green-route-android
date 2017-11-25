package com.alex.greenroute.presentation.screens.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.alex.greenroute.R;
import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.DataRepository;
import com.alex.greenroute.presentation.screens.main.DaggerMainComponent;
import com.alex.greenroute.presentation.screens.main.MainComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.text_email)
    EditText email;

    @BindView(R.id.text_password)
    EditText password;

    @Inject
    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        LoginComponent component = DaggerLoginComponent.builder()
                .appComponent(GreenApplication.component())
                .build();
        component.inject(this);
    }

    @OnClick(R.id.button_register)
    void onRegister() {

    }

    @OnClick(R.id.button_login)
    void onLogin() {
        dataRepository.login(email.getText().toString(), password.getText().toString());
    }
}

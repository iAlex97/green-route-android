package com.alex.greenroute.presentation.screens.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;

import com.alex.greenroute.R;
import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.local.prefs.PrefsRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.text_confirm_ip)
    AppCompatEditText editText;

    @Inject
    PrefsRepository prefsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        GreenApplication.component().inject(this);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.save)
    void onSave() {

    }
}

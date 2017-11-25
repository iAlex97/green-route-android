package com.alex.greenroute.presentation.screens.login;

import com.alex.greenroute.injection.AppComponent;
import com.alex.greenroute.injection.PerActivity;

import dagger.Component;

/**
 * Created by alex on 28.03.2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}

package com.alex.greenroute.presentation.screens.tutorial;

/**
 * Created by alex on 25/11/2017.
 */

public interface AuthCallbacks {
    void onLoginSuccessful();

    void onRegisterSuccessful();

    void onError(String err);
}

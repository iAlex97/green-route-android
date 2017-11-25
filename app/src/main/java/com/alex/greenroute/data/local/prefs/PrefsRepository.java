package com.alex.greenroute.data.local.prefs;

import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.local.prefs.util.StringPreference;
import com.alex.greenroute.data.local.prefs.util.StringSetPreference;
import com.google.gson.Gson;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by ialex on 15.02.2017.
 */

public class PrefsRepository {

    @Inject
    @Named("cookies")
    StringSetPreference cookiesPreference;

    @Inject
    @Named("username")
    StringPreference usernamePreference;

    @Inject
    @Named("password")
    StringPreference passwordPreference;

    @Inject
    Gson gson;

    public PrefsRepository() {
        GreenApplication.component().inject(this);
    }

    /**
     *
     */

    public void setUsername(String username) {
        usernamePreference.set(username);
    }

    public String getUsername() {
        return usernamePreference.get();
    }

    public void setPassword(String password) {
        passwordPreference.set(password);
    }

    public String getPassword() {
        return passwordPreference.get();
    }

    public Set<String> getCookies() {
        return cookiesPreference.get();
    }

    public void saveCookies(Set<String> value) {
        cookiesPreference.set(value);
    }
}

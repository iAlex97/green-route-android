package com.alex.greenroute.data.local.prefs;

import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.local.prefs.util.StringPreference;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by ialex on 15.02.2017.
 */

public class PrefsRepository {

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


}

package com.alex.greenroute.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alex.greenroute.data.local.prefs.util.LongPreference;
import com.alex.greenroute.data.local.prefs.util.StringPreference;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ialex on 15.02.2017.
 */

@Module
public class PrefsModule {

    @Provides
    @Singleton
    PrefsRepository providePrefsRepository() {
        return new PrefsRepository();
    }

    @Provides
    @Singleton
    SharedPreferences provideAppSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Named("username")
    @Singleton
    StringPreference provideUsernamePreference(SharedPreferences preferences) {
        return new StringPreference(preferences, "username", null);
    }

    @Provides
    @Named("password")
    @Singleton
    StringPreference providePasswordData(SharedPreferences preferences) {
        return new StringPreference(preferences, "password", null);
    }

    @Provides
    @Named("ftp_username")
    @Singleton
    StringPreference provideFTPUsernamePreference(SharedPreferences preferences) {
        return new StringPreference(preferences, "ftp_username", null);
    }

    @Provides
    @Named("ftp_password")
    @Singleton
    StringPreference provideFTPPasswordData(SharedPreferences preferences) {
        return new StringPreference(preferences, "ftp_password", null);
    }

    @Provides
    @Named("temp_product")
    @Singleton
    StringPreference provideTempProduct(SharedPreferences preferences) {
        return new StringPreference(preferences, "temp_product", null);
    }

    @Provides
    @Named("temp_photos")
    @Singleton
    StringPreference provideTempPhotos(SharedPreferences preferences) {
        return new StringPreference(preferences, "temp_photos", null);
    }

    @Provides
    @Named("intent_timestamp")
    @Singleton
    LongPreference provideIntentTimestamp(SharedPreferences preferences) {
        return new LongPreference(preferences, "intent_timestamp", -1);
    }
}

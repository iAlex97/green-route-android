package com.alex.greenroute.data.local.prefs.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

public class GsonPreference<T> {
    private final SharedPreferences preferences;
    private final String key;
    private final T defaultValue;
    private final Gson gson;
    private Class<T> type;

    public GsonPreference(SharedPreferences preferences, String key, Class<T> type, Gson gson) {
        this(preferences, key, null, type, gson);
    }

    public GsonPreference(SharedPreferences preferences, String key, T defaultValue, Class<T>
            type, Gson gson) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
        this.type = type;
        this.gson = gson;
    }

    public T get() {
        String value = preferences.getString(key, "");
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return gson.fromJson(value, type);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(T value) {
        if (value != null) {
            preferences.edit().putString(key, gson.toJson(value, type)).apply();
        } else {
            preferences.edit().putString(key, null).apply();
        }
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}

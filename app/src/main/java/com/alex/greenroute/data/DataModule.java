package com.alex.greenroute.data;

import android.net.Uri;

import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.data.remote.Api;
import com.alex.greenroute.data.util.UriSerialization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ialex on 15.02.2017.
 */

@Module
public class DataModule {

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Uri.class, new UriSerialization());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    DataRepository provideDataManager(Api rxApi, PrefsRepository prefsRepository) {
        return new DataRepository(rxApi, prefsRepository);
    }
}

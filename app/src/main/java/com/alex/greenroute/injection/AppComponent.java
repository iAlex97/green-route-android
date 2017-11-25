package com.alex.greenroute.injection;

import android.content.Context;

import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.DataModule;
import com.alex.greenroute.data.DataRepository;
import com.alex.greenroute.data.local.prefs.PrefsModule;
import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.data.remote.RemoteModule;
import com.alex.greenroute.presentation.screens.tutorial.fragments.FragmentLogin;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DataModule.class, RemoteModule.class, PrefsModule.class})
public interface AppComponent {

    void inject(GreenApplication application);

    void inject(PrefsRepository prefsRepository);

    void inject(FragmentLogin fragmentLogin);

    PrefsRepository prefsRepository();

    DataRepository dataManager();

    Context context();

    /*AnalyticsHandler analytics();*/

}

package com.alex.greenroute.component;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;

import com.alex.greenroute.BuildConfig;
import com.alex.greenroute.injection.AppComponent;
import com.alex.greenroute.injection.AppModule;
import com.alex.greenroute.injection.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;


/**
 * Created by alex on 23.10.2015.
 */
public class GreenApplication extends Application {
    private AppComponent appComponent;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /* Get the context when you don't have access to it in any other way */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        GreenApplication.context = getApplicationContext();

        //Logger.setLogLevel(Logger.LogLevel.DEBUG);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            LeakCanary.install(this);
        } else {
            /*Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new
                    StickersUncaughtExceptionHandler(getContext());
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);*/
            //startCrashlytics();
            Timber.plant(new CrashlyticsTree());
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)  {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public static AppComponent component() {
        return ((GreenApplication) getContext()).appComponent;
    }
}
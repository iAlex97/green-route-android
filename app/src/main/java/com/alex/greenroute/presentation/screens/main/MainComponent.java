package com.alex.greenroute.presentation.screens.main;

import com.alex.greenroute.injection.AppComponent;
import com.alex.greenroute.injection.PerActivity;

import dagger.Component;

/**
 * Created by ialex on 13.02.2017.
 *
 */

@PerActivity
@Component(dependencies = AppComponent.class)
public interface MainComponent {

    void inject(MainActivity activity);

}

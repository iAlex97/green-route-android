package com.alex.greenroute.presentation.screens.live;

import com.alex.greenroute.injection.AppComponent;
import com.alex.greenroute.injection.PerActivity;

import dagger.Component;

/**
 * Created by alex on 26/11/2017.
 */

@PerActivity
@Component(dependencies = AppComponent.class)
public interface LiveComponent {

    void inject(LiveActivity activity);

}

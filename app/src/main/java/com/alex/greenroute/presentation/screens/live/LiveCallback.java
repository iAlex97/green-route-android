package com.alex.greenroute.presentation.screens.live;

import com.alex.greenroute.data.remote.response.PollutionResponse;

/**
 * Created by alex on 26/11/2017.
 */

public interface LiveCallback {
    void onNewLiveData(PollutionResponse data);

    void onError();
}

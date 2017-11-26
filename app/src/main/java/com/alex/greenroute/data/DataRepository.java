package com.alex.greenroute.data;

import android.support.annotation.NonNull;

import com.alex.greenroute.component.MiscUtils;
import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.data.remote.Api;
import com.alex.greenroute.data.remote.body.LoginBody;
import com.alex.greenroute.data.remote.body.RegisterBody;
import com.alex.greenroute.data.remote.models.AirStation;
import com.alex.greenroute.data.remote.response.AirQualityResponse;
import com.alex.greenroute.data.remote.response.ApiResponse;
import com.alex.greenroute.data.remote.response.PollutionResponse;
import com.alex.greenroute.presentation.screens.live.LiveCallback;
import com.alex.greenroute.presentation.screens.main.StationMarkerListener;
import com.alex.greenroute.presentation.screens.tutorial.AuthCallbacks;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by ialex on 15.02.2017.
 */

public class DataRepository {

    private Api api;
    private PrefsRepository prefsRepository;

    public DataRepository(Api api, PrefsRepository prefsRepository) {
        this.api = api;
        this.prefsRepository = prefsRepository;
    }

    public void login(String username, String password, final AuthCallbacks callbacks) {
        api.login(new LoginBody(username, password)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                ApiResponse resp = response.body();
                if (resp == null) {
                    callbacks.onError("Unkown error");
                    return;
                }

                switch (resp.logs.get(0).code) {
                    case "600":
                        callbacks.onLoginSuccessful();
                        break;
                    case "601":
                    case "602":
                        callbacks.onError(resp.logs.get(0).message);
                        break;
                    default:
                        callbacks.onError("Unkown error");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Timber.e(t, "fail");
                callbacks.onError(t.getMessage());
            }
        });
    }

    public void register(String username, String password, final AuthCallbacks callbacks) {
        api.register(new RegisterBody(username, password)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                ApiResponse resp = response.body();
                if (resp == null) {
                    callbacks.onError("Unkown error");
                    return;
                }

                switch (resp.logs.get(0).code) {
                    case "500":
                        callbacks.onRegisterSuccessful();
                        break;
                    case "501":
                    case "502":
                    case "503":
                        callbacks.onError(resp.logs.get(0).message);
                        break;
                    default:
                        callbacks.onError("Unkown error");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Timber.e(t, "fail");
                callbacks.onError(t.getMessage());
            }
        });
    }

    public void getLivePollutionJson(final LiveCallback callback) {
        api.getPollutonJson().enqueue(new Callback<PollutionResponse>() {
            @Override
            public void onResponse(Call<PollutionResponse> call, Response<PollutionResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                callback.onNewLiveData(response.body());
            }

            @Override
            public void onFailure(Call<PollutionResponse> call, Throwable t) {
                callback.onError();
            }
        });
    }

    public void getAirQualityStations(final StationMarkerListener listener) {
        api.getAirQuality(44.42f, 26.07f, 500).enqueue(new Callback<List<AirStation>>() {
            @Override
            public void onResponse(Call<List<AirStation>> call, Response<List<AirStation>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                listener.onNewMarkers(MiscUtils.mapResponseToMarkers(response.body()));
            }

            @Override
            public void onFailure(Call<List<AirStation>> call, Throwable t) {
                Timber.d("onFailure");
            }
        });
    }
}

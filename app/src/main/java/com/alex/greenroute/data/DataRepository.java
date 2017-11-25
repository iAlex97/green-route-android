package com.alex.greenroute.data;

import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.data.remote.Api;
import com.alex.greenroute.data.remote.body.LoginBody;
import com.alex.greenroute.data.remote.response.ApiResponse;

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

    public void login(String username, String password) {
        api.login(new LoginBody(username, password)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse resp = response.body();
                Timber.d("sall");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Timber.d("fail");
            }
        });
    }
}

package com.alex.greenroute.data.remote;

import com.alex.greenroute.data.remote.body.LoginBody;
import com.alex.greenroute.data.remote.body.RegisterBody;
import com.alex.greenroute.data.remote.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ialex on 15.02.2017.
 */

public interface Api {
    String URL_PROD_BASE = "http://192.168.2.4/";
    String URL_PROD_TEST = URL_PROD_BASE/*"http://192.168.0.105/"*/;

    @POST("/GreenRoute/user/register")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ApiResponse> register(@Body RegisterBody body);

    @POST("/GreenRoute/user/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ApiResponse> login(@Body LoginBody body);

    @POST("/GreenRoute/user/logout")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ApiResponse> logout();
}

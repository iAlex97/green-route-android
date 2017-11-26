package com.alex.greenroute.data.remote;

import com.alex.greenroute.data.remote.body.LoginBody;
import com.alex.greenroute.data.remote.body.RegisterBody;
import com.alex.greenroute.data.remote.models.AirStation;
import com.alex.greenroute.data.remote.response.AirQualityResponse;
import com.alex.greenroute.data.remote.response.ApiResponse;
import com.alex.greenroute.data.remote.response.PollutionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ialex on 15.02.2017.
 */

public interface Api {
    String URL_PROD_BASE = "http://192.168.2.5/";
    String URL_PROD_TEST = URL_PROD_BASE/*"http://192.168.0.105/"*/;

    @POST("/GreenRoute/api/user/register")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ApiResponse> register(@Body RegisterBody body);

    @POST("/GreenRoute/api/user/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ApiResponse> login(@Body LoginBody body);

    @POST("/GreenRoute/api/user/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ApiResponse> login(@Query("email") String body, @Query("password") String password);

    @POST("/GreenRoute/api/user/logout")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<ApiResponse> logout();

    @GET("/GreenRoute/api/poldata.json")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<PollutionResponse> getPollutonJson();

    @GET("/GreenRoute/api/aqi")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Call<List<AirStation>> getAirQuality(@Query("lat") float lat, @Query("lng") float lng, @Query("radius") float radius);
}

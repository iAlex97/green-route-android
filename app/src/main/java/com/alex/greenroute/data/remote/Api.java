package com.alex.greenroute.data.remote;

/**
 * Created by ialex on 15.02.2017.
 */

public interface Api {
    String URL_PROD_BASE = "192.168.2.5";
    String URL_PROD_TEST = URL_PROD_BASE/*"http://192.168.0.105/"*/;

    /* @POST("/user/register")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<RegisterResponse> register(@Body RegisterBody body);

    @POST("/user/login")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<LoginResponse> login(@Body LoginBody body);

    @POST("/user/logout")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<ApiResponse> logout();

    @GET("/get/cardslist")
    @Headers({"Cache-Control: no-store, no-cache", "User-Agent: android"})
    Observable<GenericResponse> getCardsList();*/
}

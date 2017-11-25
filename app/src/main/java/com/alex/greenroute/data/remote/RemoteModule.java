package com.alex.greenroute.data.remote;

import com.google.gson.Gson;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ialex on 15.02.2017.
 */

@Module
public class RemoteModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
    }

    @Provides
    @Singleton
    Api provideRetrofitApi(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Api.URL_PROD_TEST)
                .client(okHttpClient)
                .build()
                .create(Api.class);
    }
}

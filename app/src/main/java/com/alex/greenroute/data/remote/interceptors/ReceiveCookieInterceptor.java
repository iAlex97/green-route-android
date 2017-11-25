package com.alex.greenroute.data.remote.interceptors;

import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.data.remote.NetConstants;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by alex on 14.12.2015.
 */
public class ReceiveCookieInterceptor implements Interceptor {

    private final PrefsRepository prefsRepository;

    public ReceiveCookieInterceptor(PrefsRepository prefsRepository) {
        this.prefsRepository = prefsRepository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                if(header.contains(NetConstants.IAC_SESSION)) {
                    cookies.add(header);
                }
            }

            prefsRepository.saveCookies(cookies);
        }

        return originalResponse;
    }
}

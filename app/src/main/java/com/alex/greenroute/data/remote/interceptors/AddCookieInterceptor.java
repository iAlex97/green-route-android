package com.alex.greenroute.data.remote.interceptors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.data.remote.NetConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by alex on 14.12.2015.
 */
public class AddCookieInterceptor implements Interceptor {
    private final PrefsRepository prefsRepository;

    public AddCookieInterceptor(PrefsRepository prefsRepository) {
        this.prefsRepository = prefsRepository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> cookies = (HashSet) prefsRepository.getCookies();

        if(cookies != null) {
            for (String cookie : cookies) {
                if(cookie.contains(NetConstants.IAC_SESSION)) {
                    Map<String, String> temp = splitCookie(cookie);

                    String token = temp.get(NetConstants.IAC_SESSION);

                    builder.addHeader("Cookie", NetConstants.IAC_SESSION + "=" + token);
                    Timber.d("Adding Header: " + NetConstants.IAC_SESSION + "=" + token); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                }
            }
        } else {
            Log.d("interceptor", "Null iac_session, not adding this cookie");
        }

        return chain.proceed(builder.build());
    }

    /**
     *
     * @param cookie string
     * @return the cookie map;
     */

    private Map<String, String> splitCookie(String cookie) { //TODO build Cookie class and store this shit
        Map<String, String> prajitura = new HashMap<>();

        String[] parts = cookie.split(";");
        for(String part : parts) {
            String[] args = part.split("=");

            prajitura.put(args[0].trim(), args[1].trim());
        }
        return prajitura;
    }
}

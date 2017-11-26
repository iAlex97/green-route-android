package com.alex.greenroute.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.alex.greenroute.data.remote.models.AirStation;
import com.alex.greenroute.presentation.screens.main.StationMarker;
import com.google.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex on 27.09.2017.
 */

public class MiscUtils {

    private static final Pattern EMAIL_ADDRESS_PATTERN
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * Checking for all possible internet providers
     **/

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network", "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static List<String> readEmails(String text) {
        List<String> toReturn = new ArrayList<>();

        Matcher m = EMAIL_ADDRESS_PATTERN.matcher(text);

        while (m.find()) {
            toReturn.add(m.group().trim());
        }

        return (toReturn.size() > 0) ? toReturn : null;
    }

    public static Bitmap makeTintedBitmap(Bitmap src, int color) {
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas c = new Canvas(result);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        c.drawBitmap(src, 0, 0, paint);
        return result;
    }

    public static List<StationMarker> mapResponseToMarkers(List<AirStation> in) {
        List<StationMarker> markers = new ArrayList<>();

        for (AirStation station : in) {
            markers.add(new StationMarker(station.nume, station.aqi, station.lat, station.lng));
        }

        return markers;
    }

    public static LatLng getOtherLatLng(com.google.android.gms.maps.model.LatLng in) {
        return new LatLng(in.latitude, in.longitude);
    }

    public static double distanceBetween(com.google.android.gms.maps.model.LatLng StartP, com.google.android.gms.maps.model.LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return Radius * c;
    }
}

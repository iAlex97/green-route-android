package com.alex.greenroute.presentation.screens.main;

import android.content.Context;
import android.os.AsyncTask;

import com.alex.greenroute.R;
import com.alex.greenroute.component.MiscUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 26/11/2017.
 */

public class GreenTrip extends AsyncTask<Void, Void, DirectionsResult> {

    private final GoogleMap map;

    private Context context;

    private LatLng origin;
    private LatLng destination;

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(context.getString(R.string.directions_api_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    public GreenTrip(Context context, GoogleMap map, LatLng origin, LatLng destination) {
        this.map = map;
        this.context = context;
        this.origin = origin;
        this.destination = destination;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    @Override
    protected DirectionsResult doInBackground(Void... voids) {
        DateTime now = new DateTime();
        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING)
                    .origin(MiscUtils.getOtherLatLng(origin))
                    .destination(MiscUtils.getOtherLatLng(destination))
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(DirectionsResult directionsResult) {
        super.onPostExecute(directionsResult);

        if (directionsResult != null) {
            addPolyline(directionsResult, map);
        }
    }
}

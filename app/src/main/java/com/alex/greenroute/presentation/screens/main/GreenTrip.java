package com.alex.greenroute.presentation.screens.main;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alex.greenroute.R;
import com.alex.greenroute.component.MiscUtils;
import com.alex.greenroute.data.local.prefs.util.LongPreference;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 26/11/2017.
 */

public class GreenTrip extends AsyncTask<Void, Void, DirectionsResult> {

    private int colorRed = Color.parseColor("#F44336");
    private int colorGreen = Color.parseColor("#4CAF50");


    private final List<StationMarker> stations;

    private final GoogleMap map;

    private Context context;

    private LatLng origin;
    private LatLng destination;

    private MaterialDialog loader;

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(10)
                .setApiKey(context.getString(R.string.directions_api_key))
                .setConnectTimeout(3, TimeUnit.SECONDS)
                .setReadTimeout(3, TimeUnit.SECONDS)
                .setWriteTimeout(3, TimeUnit.SECONDS);
    }

    public GreenTrip(Context context, GoogleMap map, List<StationMarker> stations, LatLng origin, LatLng destination) {
        this.map = map;
        this.stations = stations;
        this.context = context;
        this.origin = origin;
        this.destination = destination;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        TreeMap<Long, Integer> distances = new TreeMap<>();
        long mincost = Long.MAX_VALUE;

        for (int j=0; j<results.routes.length; j++) {
            List<LatLng> decodedPath = PolyUtil.decode(results.routes[j].overviewPolyline.getEncodedPath());

            long cost = 0;

            for (int i=0; i < decodedPath.size(); i += 5) {
                LatLng point = decodedPath.get(i);

                double minDistance = Double.MAX_VALUE;
                StationMarker selected = null;

                for (StationMarker marker : stations) {
                    double currentDistance = MiscUtils.distanceBetween(point, marker.getPosition());

                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        selected = marker;
                    }
                }

                if (selected != null) {
                    cost += selected.getAqi();
                }
            }

            distances.put(cost, j);

            if (cost < mincost) {
                mincost = cost;
            }
        }

        for(Map.Entry<Long, Integer> entry : distances.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();

            int color = (Integer) new ArgbEvaluator().evaluate((key - mincost) / mincost, colorGreen, colorRed);
            List<LatLng> decodedPath = PolyUtil.decode(results.routes[value].overviewPolyline.getEncodedPath());

            mMap.addPolyline(new PolylineOptions().width((value + 1) * 6).color(color).addAll(decodedPath));
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        loader = new MaterialDialog.Builder(context)
                .title("Calculare rute")
                .content("Va rugam asteptati")
                .progress(true, 0)
                .show();
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
                    .alternatives(true)
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

        if (loader != null) {
            loader.dismiss();
        }

        if (directionsResult != null) {
            addPolyline(directionsResult, map);
        }
    }
}

package com.alex.greenroute.presentation.screens.main;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by alex on 26/11/2017.
 */

public class StationMarker implements ClusterItem {
    private final int aqi;

    private final String name;

    private final LatLng position;

    public StationMarker(String name, String aqi, String lat, String lng) {
        this.name = name;
        this.aqi = Integer.valueOf(aqi);

        float latitude = Float.parseFloat(lat);
        float longitude = Float.parseFloat(lng);

        this.position = new LatLng(latitude, longitude);
    }

    public int getAqi() {
        return aqi;
    }

    public String getName() {
        return name;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return "Salam alecu";
    }
}

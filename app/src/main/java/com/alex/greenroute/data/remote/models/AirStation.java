package com.alex.greenroute.data.remote.models;

import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by alex on 26/11/2017.
 */

public class AirStation {
    public String id;

    public String station_id;

    public String aqi;

    public String nume;

    public String pm25;

    public String o3;

    public String no2;

    public String so2;

    @SerializedName("t")
    public String t;

    @SerializedName("p")
    public String p;

    @SerializedName("h")
    public String humidity;

    @SerializedName("w")
    public String wind;

    public String lat;

    public String lng;
}

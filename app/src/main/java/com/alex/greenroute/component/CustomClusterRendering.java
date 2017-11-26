package com.alex.greenroute.component;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.alex.greenroute.R;
import com.alex.greenroute.data.remote.models.AirStation;
import com.alex.greenroute.presentation.screens.main.StationMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by alex on 23.04.2016.
 */
public class CustomClusterRendering extends DefaultClusterRenderer<StationMarker> {
    private Bitmap defaultMarker;

    private int colorRed = Color.parseColor("#F44336");
    private int colorGreen = Color.parseColor("#4CAF50");


    public CustomClusterRendering(Context context, GoogleMap map, ClusterManager<StationMarker> clusterManager) {
        super(context, map, clusterManager);

        defaultMarker = BitmapFactory.decodeResource(context.getResources(), R.drawable.flat_marker);
    }

    protected void onBeforeClusterItemRendered(StationMarker item, MarkerOptions markerOptions) {
        //markerOptions.icon(item.getIcon());
        //markerOptions.snippet(item.getSnippet());
        int color = (Integer) new ArgbEvaluator().evaluate(item.getAqi() / 200.f, colorGreen, colorRed);
        markerOptions.title(item.getName());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(MiscUtils.makeTintedBitmap(defaultMarker, color)));
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}

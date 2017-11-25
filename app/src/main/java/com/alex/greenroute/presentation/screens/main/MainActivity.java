package com.alex.greenroute.presentation.screens.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alex.greenroute.R;
import com.alex.greenroute.component.GreenApplication;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MainComponent component = DaggerMainComponent.builder()
                .appComponent(GreenApplication.component())
                .build();
        component.inject(this);

        setupMap();
    }

    @Override
    public void onCameraIdle() {
        Timber.d("onCameraIdle");
    }

    @Override
    public void onCameraMoveCanceled() {
        Timber.d("onCameraMoveCanceled");
    }

    @Override
    public void onCameraMove() {
        Timber.d("onCameraMove");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Timber.d("onMapReady");

        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.439663, 26.096306), 8));
    }

    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}

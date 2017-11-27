package com.alex.greenroute.presentation.screens.main;

import android.Manifest;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.ActionMenuView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.View;

import com.alex.greenroute.R;
import com.alex.greenroute.component.CustomClusterRendering;
import com.alex.greenroute.component.GreenApplication;
import com.alex.greenroute.data.DataRepository;
import com.alex.greenroute.data.local.prefs.PrefsRepository;
import com.alex.greenroute.presentation.screens.live.LiveActivity;
import com.alex.greenroute.presentation.screens.settings.SettingsActivity;
import com.alex.greenroute.presentation.screens.tutorial.TutorialActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mypopsy.widget.FloatingSearchView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        LocationListener,
        Drawer.OnDrawerItemClickListener,
        StationMarkerListener {

    @BindView(R.id.search)
    FloatingSearchView mSearchView;

    @Inject
    PrefsRepository prefsRepository;

    @Inject
    DataRepository dataRepository;

    private List<StationMarker> lastMarkers;

    private LatLng mLastPosition;

    private GoogleMap mMap;

    private ClusterManager<StationMarker> mClusterManager;

    private Drawer mDrawer;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MainComponent component = DaggerMainComponent.builder()
                .appComponent(GreenApplication.component())
                .build();
        component.inject(this);


        if (prefsRepository.getPassword() == null || prefsRepository.getUsername() == null) {
            startActivity(new Intent(this, TutorialActivity.class));
            finish();
            return;
        }

        setupMap();
        setupSearchView();
        setupDrawer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Timber.i("Place: " + place.getName());

                getDirectionsTo(place.getLatLng());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Timber.e( status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            mSearchView.setActivated(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isActivated()) {
            mSearchView.setActivated(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     *      GOOGLE MAPS CALLBACKS
     */

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

        setupClusterer();
        MainActivityPermissionsDispatcher.setupLocationProviderWithPermissionCheck(this);
    }

    @Override
    public void onNewMarkers(List<StationMarker> markers) {
        this.lastMarkers = markers;
        mClusterManager.addItems(markers);
    }

    /**
     *      LOCATION CALLBACKS
     */
    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        mLastPosition = latLng;

        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Timber.d("Latitude: %f, Longitude: %f", latitude, longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     *     DRAWER CALLBACKS
     */

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if (drawerItem.getIdentifier() == 2) {
            startActivity(new Intent(this, LiveActivity.class));
            return true;
        } else if (drawerItem.getIdentifier() == 3) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else {
            return false;
        }
    }

    /**
     *     SETUP METHODS
     */

    private void setupMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);
        mClusterManager.setRenderer(new CustomClusterRendering(this, mMap, mClusterManager));

        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnCameraIdleListener(mClusterManager);

        dataRepository.getAirQualityStations(this);
    }

    private void setupSearchView() {
        mSearchView.setIcon(new DrawerArrowDrawable(this));

        mSearchView.setOnIconClickListener(new FloatingSearchView.OnIconClickListener() {
            @Override
            public void onNavigationClick() {
                if (mSearchView.isActivated()) {
                    mSearchView.setActivated(false);
                } else {
                    //pop the drawer
                    mDrawer.openDrawer();
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSearchAction(CharSequence text) {
                mSearchView.setActivated(false);
            }
        });

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchView.setOnSearchFocusChangedListener(new FloatingSearchView.OnSearchFocusChangedListener() {
            @Override
            public void onFocusChanged(final boolean focused) {
                if (focused) {
                    openPlacesPredicition();
                }
            }
        });

        mSearchView.setText(null);
        mSearchView.setLogo(ContextCompat.getDrawable(this, R.drawable.logo));
        mSearchView.showLogo(true);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void setupLocationProvider() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }

    private void setupDrawer() {
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Ionescu Alexandru").withEmail(prefsRepository.getUsername()).withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Live");

        //create the drawer and remember the `Drawer` result object
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2
                )
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIdentifier(3))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .build();

        mDrawer.setOnDrawerItemClickListener(this);
    }

    void openPlacesPredicition() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    private void clearMapsWithoutMarkers() {
        mMap.clear();
        mClusterManager.clearItems();
        onNewMarkers(lastMarkers);
    }

    private void getDirectionsTo(LatLng to) {
        //clearMapWithoutMarkers();
        if (lastMarkers == null || mLastPosition == null) {
            return;
        }

        clearMapsWithoutMarkers();

        new GreenTrip(this, mMap, lastMarkers, mLastPosition, to).execute();
    }
}

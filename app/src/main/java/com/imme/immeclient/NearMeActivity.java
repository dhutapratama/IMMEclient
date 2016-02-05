package com.imme.immeclient;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NearMeActivity extends AppCompatActivity {

    // Google Map
    private GoogleMap googleMap;
    private CustomMapFragment mCustomMapFragment;


    GPSTracker gps;
    // Get the location manager
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

        // InitializeUI
        initializeUI();
    }

    private void initializeUI() {

        gps = new GPSTracker(this);

        try {
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    }

    private void initilizeMap() {
        if (googleMap == null) {
            mCustomMapFragment = ((CustomMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map));
            //mCustomMapFragment.setOnDragListener(NearMeActivity.this);
            googleMap = mCustomMapFragment.getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }


        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        LatLng latLng = new LatLng(latitude + 0.0002, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        googleMap.animateCamera(cameraUpdate);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        //googleMap.addMarker(new MarkerOptions().position(latLng).title("Saya"));
        //googleMap.animateCamera(zoom);
        //locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
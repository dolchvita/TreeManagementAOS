package com.snd.app.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.HashSet;
import java.util.Set;

public class GpsStatusHelper extends GnssStatus.Callback implements GpsStatus.Listener, LocationListener {
    interface Listener {
        void onSatelliteStatusChanged(GnssStatus status);
        void onGpsStatusChanged(int event);
    }

    protected String TAG = this.getClass().getName();

    private final Set<Listener> listeners = new HashSet<>();
    private final LocationManager locationManager;
    private final Context context;

    public GpsStatusHelper(LocationManager locationManager, Context context){
        this.locationManager = locationManager;
        this.context = context;
    }

    public void registerListener(Listener listener) {
        listeners.add(listener); // Add the listener to the set of listeners
    }

    @Override
    public void onGpsStatusChanged(int event) {
        for (Listener listener : listeners) {
            listener.onGpsStatusChanged(event);
        }
    }

    @Override
    public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
        int satelliteCount = status.getSatelliteCount();
        if (satelliteCount >= 0) {
            // Enough satellites, proceed to get location
            getLocation();
            Log.d(TAG, "***여기까지 오면 끝***");
        }
    }

    // 위치 가져오는 메서드
    private void getLocation() {
        // Assume you have the necessary location permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
    }

    // 위성 개수 세는 메서드
    public int getSatelliteCount(@NonNull GnssStatus status) {
        return status.getSatelliteCount();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Do something with the location
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
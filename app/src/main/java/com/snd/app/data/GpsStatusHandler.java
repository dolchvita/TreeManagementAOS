package com.snd.app.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GnssStatus;
import android.location.LocationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.N)
public abstract class GpsStatusHandler {
    protected final Context context;
    protected final GnssStatus.Callback callback;
    protected final LocationManager manager;


    public GpsStatusHandler(LocationManager manager, Context context, GnssStatus.Callback callback) {
        this.context = context;
        this.callback = callback;
        this.manager=manager;
    }

    @SuppressLint("MissingPermission")
    void registerCallback() {
        manager.registerGnssStatusCallback(callback);
    }

    void unregisterCallback() {
        manager.unregisterGnssStatusCallback(callback);
    }
}

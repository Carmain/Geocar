package com.supinfo.jva.geocar.external_class;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Martin on 04/04/2015.
 */
public class Locator implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
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

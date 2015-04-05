package com.supinfo.jva.geocar.external_class;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 04/04/2015.
 */
public class Locator implements LocationListener {

    private APIRequest requestStuff = new APIRequest();
    private Context context;

    public Locator(Context that) {
        this.context = that;
    }

    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String username = preferences.getString("username", ""); // If we couln't get the informations, we return an empty string.
        String password = preferences.getString("password", "");

        Map<String, Double> position = new HashMap<String, Double>();
        position.put("latitude", location.getLatitude());
        position.put("longitude", location.getLongitude());

        String response = requestStuff.requestAPI(context, "updatePosition", username, password, position);
        JSONObject json = null;
        Boolean success = false;
        try {
            json = new JSONObject(response);
            success = (Boolean) json.get("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, success + "", Toast.LENGTH_SHORT).show();
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

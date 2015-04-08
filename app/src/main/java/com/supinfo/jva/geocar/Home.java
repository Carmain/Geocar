package com.supinfo.jva.geocar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.supinfo.jva.geocar.tools.APIRequest;
import com.supinfo.jva.geocar.tools.Locator;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends ActionBarActivity {

    private APIRequest requestStuff = new APIRequest();
    private Locator locator = new Locator(this);
    private GoogleMap map;
    private int exitCount = 0;
    private Button findCar = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        displayCarPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if the user got the GPS enabled before send his position
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled){
            /**
             * LocationManager.GPS_PROVIDER : Use to get the position
             * 60000                        : Time between updates in milliseconds (within 1 minute)
             * 0                            : Time between updates meter. At zero because not used
             * Locator                      : Callback launched as soon as the supplier will be activated
             */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locator);
        }
        else{
            connectLocalisationAgreement();
        }

        final Context that = this;
        findCar = (Button) findViewById(R.id.findMyCar);

        findCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(that, "get the car!", Toast.LENGTH_SHORT).show();
                displayCarPosition();
            }
        });
    }

    // Ask the user to activate his GPS connection
    private void connectLocalisationAgreement(){
        AlertDialog.Builder agreementLocation = new AlertDialog.Builder(this);
        agreementLocation.setMessage(R.string.errorGPS);
        agreementLocation.setCancelable(false);
        agreementLocation.setPositiveButton(R.string.enableGPS, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // If we want to active the GPS, we send the user to the activity
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        agreementLocation.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface window, int id){
                window.cancel();
            }
        });

        AlertDialog alertWindow = agreementLocation.create();
        alertWindow.show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.backPressed, Toast.LENGTH_SHORT).show();
        exitCount ++;
        if (exitCount >= 2) {
            logOut();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exitCount = 0;
            }
        }, 2000);
    }

    private void displayCarPosition() {
        final Home that = this;

        // Get informations about the user from the login page
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");


        if(username.equals("") && password.equals("")) {
            Toast.makeText(this, R.string.errorSession, Toast.LENGTH_SHORT).show();
        }
        else {
            // Request the server to get the car position.
            String response = requestStuff.requestAPI(that, "getCarPosition", username, password);
            JSONObject json = null;
            Boolean success = false;
            try {
                json = new JSONObject(response);
                success = (Boolean) json.get("success");
            } catch (JSONException e) {
                Toast.makeText(that, R.string.errorCarPosition, Toast.LENGTH_SHORT).show();
            }

            if (success) {
                try {
                    JSONObject position = (JSONObject) json.get("position");
                    double latitude =  (double) position.get("latitude");
                    double longitude = (double) position.get("longitude");
                    setUpMapIfNeeded(latitude, longitude);
                } catch (JSONException e) {
                    Toast.makeText(that, R.string.errorCarPosition, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(that, R.string.errorCarPosition, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------  GOOGLE MAP API STUFF -------------------------------
    // -------------------------------------------------------------------------------------

    private void setUpMapIfNeeded(double latitude, double longitude) {
        // First
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                moveCameraFromCar(latitude, longitude);
            }
        }
        else {
            moveCameraFromCar(latitude, longitude);
        }
    }

    private void setUpMap(double latitude, double longitude) {
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
    }

    private void moveCameraFromCar(double latitude, double longitude) {
        setUpMap(latitude, longitude);
        LatLng carPosition = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(carPosition, 5));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 3000, null);
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------------
    // -------------------------------  MENU ACTIVITY STUFF  -------------------------------
    // -------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                logOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // -------------------------------------------------------------------------------------
    // -------------------------------------------------------------------------------------

    private void logOut() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().commit();
        finish();
        System.exit(0);
    }
}

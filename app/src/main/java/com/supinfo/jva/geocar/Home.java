package com.supinfo.jva.geocar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.supinfo.jva.external_class.APIRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends ActionBarActivity {

    private APIRequest requestStuff = new APIRequest();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        final Home that = this;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        if(username == "" && password == "") {
            Toast.makeText(this, R.string.errorSession, Toast.LENGTH_SHORT).show();
        }
        else {
            String response = requestStuff.requestAPI(that, "getCarPosition", username, password);
            JSONObject json = null;
            Boolean success = false;
            try {
                json = new JSONObject(response);
                success = (Boolean) json.get("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {
                try {
                    JSONObject position = (JSONObject) json.get("position");
                    Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(that, R.string.error_car_position, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(that, R.string.error_id, Toast.LENGTH_SHORT).show();
            }
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled){
            // Test if the GPS is enabled
            Toast.makeText(this, R.string.okGPS, Toast.LENGTH_SHORT).show(); // TODO : Supprimer la ligne Au moment de la livraison du projet (toast de test)
        }
        else{
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.errorGPS)
                .setCancelable(false)
                .setPositiveButton(R.string.enableGPS,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // If we want to active the GPS, we send the user to the activity
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });

        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

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
            case R.id.quit_app:
                finish();
                System.exit(0);
                break;
            default:
                Log.e("Other", "Other stuffs");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

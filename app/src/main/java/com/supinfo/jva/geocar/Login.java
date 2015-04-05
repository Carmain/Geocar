package com.supinfo.jva.geocar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.supinfo.jva.geocar.external_class.APIRequest;


public class Login extends ActionBarActivity {

    private EditText usernameField = null;
    private EditText passwordField = null;
    private Button sendDataButton = null;
    private final Login context = this;

    private APIRequest requestStuff = new APIRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // If we couln't get the informations, we return an empty string.
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        if(username == "" && password == "") {

            usernameField = (EditText) findViewById(R.id.username);
            passwordField = (EditText) findViewById(R.id.password);
            sendDataButton = (Button) findViewById(R.id.sendButton);

            sendDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logIn();
                }
            });
        }
        else {
            goHome();
        }
    }

    void logIn() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this.context, R.string.error_toast, Toast.LENGTH_SHORT).show();
        }
        else {
            String response = requestStuff.requestAPI(this.context, "login", username, password);
            Boolean success = false;
            try {
                JSONObject json = new JSONObject(response);
                success = (Boolean) json.get("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (success) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", username);
                editor.putString("password", password);
                editor.commit();
                goHome();
            }
            else {
                Toast.makeText(this.context, R.string.error_id, Toast.LENGTH_SHORT).show();
            }
        }
    }

    void goHome() {
        Intent home = new Intent(Login.this, Home.class);
        startActivity(home);
    }
}

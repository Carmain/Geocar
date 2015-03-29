package com.supinfo.jva.geocar;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Home extends ActionBarActivity {

    private EditText usernameField = null;
    private EditText passwordField = null;
    private Button sendDataButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        sendDataButton = (Button) findViewById(R.id.sendButton);
        final Home that = this;

        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(that, R.string.error_toast, Toast.LENGTH_SHORT).show();
                }
                else {
                    connectUser(username, password);
                    Toast.makeText(that, username + " " + password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void connectUser(String username, String password) {
        HttpClient httpclient = new DefaultHttpClient(); // Create a new HttpClient
        HttpPost httppost = new HttpPost("http://91.121.105.200/SUPTracking/"); // Create a post header

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("action", "login"));
            nameValuePairs.add(new BasicNameValuePair("login", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String json = EntityUtils.toString(response.getEntity());
            Log.e("", json); // Print the result

        } catch (ClientProtocolException e) {
            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
        }
    }
}

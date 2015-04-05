package com.supinfo.jva.geocar.tools;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.supinfo.jva.geocar.R;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 01/04/2015.
 */
public class APIRequest {

    public String requestAPI(Context that, String action, String username, String password, Map<String, Double> position) {
        HttpClient httpclient = new DefaultHttpClient(); // Create a new HttpClient
        HttpPost httppost = new HttpPost("http://91.121.105.200/SUPTracking/"); // Create a post header

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("action", action));
            nameValuePairs.add(new BasicNameValuePair("login", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));

            if (position.get("latitude") != null) {
                nameValuePairs.add(new BasicNameValuePair("latitude", position.get("latitude") + ""));
                nameValuePairs.add(new BasicNameValuePair("longitude", position.get("longitude") + ""));
            }

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            return EntityUtils.toString(response.getEntity());

        } catch (ClientProtocolException e) {
            Toast.makeText(that, R.string.error_connection, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(that, R.string.error_connection, Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    public String requestAPI(Context that, String action, String username, String password) {
        Map<String, Double> position = new HashMap<String, Double>();
        position.put("latitude", null);
        position.put("longitude", null);
        return requestAPI(that, action, username, password, position);
    }
}

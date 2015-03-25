package com.supinfo.jva.geocar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText usernameField = null;
    private EditText passwordField = null;
    private Button sendDataButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        sendDataButton = (Button) findViewById(R.id.sendButton);
        final MainActivity that = this;

        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(that, R.string.error_toast, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(that, username + " " + password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

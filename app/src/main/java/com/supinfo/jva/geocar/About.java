package com.supinfo.jva.geocar;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView version = (TextView) findViewById(R.id.version);
        Button gitButton = (Button) findViewById(R.id.button_git);

        Resources res = getResources();
        String actualVersion = res.getString(R.string.version, "1.0.0");
        version.setText(actualVersion);

        gitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlProject = "https://github.com/Carmain/Geocar";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlProject));
                startActivity(intent);
            }
        });

    }
}

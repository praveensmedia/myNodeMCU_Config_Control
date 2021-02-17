package com.praveensmedia.mynodemcuconfig_control.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.praveensmedia.mynodemcuconfig_control.R;
import com.praveensmedia.mynodemcuconfig_control.helpers.BackgroundTask;
import static com.praveensmedia.mynodemcuconfig_control.ui.Controls.response;
import static com.praveensmedia.mynodemcuconfig_control.ui.Home.ip;

public class Config extends AppCompatActivity {
    Button btnUpdate;
    EditText ssid,pass;
    TextView responseView,urlhit, showUrl;
    Controls controls;
    String command;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        btnUpdate=(Button)findViewById(R.id.updateCred);
        ssid     =(EditText)findViewById(R.id.yourSSID);
        pass     =(EditText)findViewById(R.id.yourPASS);
        responseView =(TextView)findViewById(R.id.response);
        urlhit   =(TextView)findViewById(R.id.URL);
        showUrl =(TextView)findViewById(R.id.linkurl2);
        showUrl.setMovementMethod(LinkMovementMethod.getInstance());
        controls =new Controls();
        String selected = "Selected IP: http:/"+ip;
        urlhit.setText(selected);
        AdView mAdView = findViewById(R.id.adViewConfig);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        final BackgroundTask pingNodeMcu= new BackgroundTask(this) {
            @Override
            public void doInBackground() {
                controls.hitUrl(command);
            }
            @Override
            public void onPostExecute() {
                responseView.setText(response);
            }
        };
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SSID = ssid.getText().toString();
                String PASS = pass.getText().toString();
                responseView.setText("");
                if(!SSID.isEmpty() && !PASS.isEmpty()){
                    command ="WC"+SSID+","+PASS+",";
                    String mainUrl ="http:/"+ip+"/"+command;
                    urlhit.setText(mainUrl);
                    pingNodeMcu.execute();
                }else {
                    Toast.makeText(Config.this,"SSID/PASS not be Empty",Toast.LENGTH_SHORT).show();
                    responseView.setText("empty credentials");
                }
            }
        });

    }
}
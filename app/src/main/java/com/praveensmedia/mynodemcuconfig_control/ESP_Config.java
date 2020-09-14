package com.praveensmedia.mynodemcuconfig_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class ESP_Config extends AppCompatActivity {
    private  Button goToSettings;
    private  Button Refresh;
    private TextView status,guide1;
    public   ping_mcu sender1;
    private Handler mHandler2;
    private EditText ssidd,passs;
    public boolean trig=false;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_s_p__config);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mHandler2 = new Handler();
        ssidd =(EditText)findViewById(R.id.ssid1);
        passs =(EditText)findViewById(R.id.pass1);
        guide1=(TextView)findViewById(R.id.guide) ;

        status = (TextView)findViewById(R.id.status2) ;
        goToSettings =(Button)findViewById(R.id.gotoSysSettings);
        goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        Refresh =(Button)findViewById(R.id.saveRefresh);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trig){
                    MainActivity.cmm = "wf"+ssidd.getText().toString()+","+passs.getText().toString()+",";
                }else{
                    MainActivity.cmm="hello";
                }

                MainActivity.adr="192.168.4.1";
                ping_mcu process = new ping_mcu();
                process.execute();
                startProgress1();
            }
        });
    }
    private void startProgress1() {

//      New thread to perform background operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                //for (int i = 0; i <= 100; i++) {
                // final int currentProgressCount = i;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                  Update the value background thread to UI thread
                mHandler2.post(new Runnable() {
                    @Override
                    public void run() {
                        final String du= sender1.resp;
                        if(du.equals("connected")){
                            Refresh.setText("SAVE");
                            guide1.setText("Enter your Home WiFi Credentials then Click SAVE ");
                            trig=true;
                            status.setText(du);
                            ssidd.setVisibility(View.VISIBLE);
                            passs.setVisibility(View.VISIBLE);
                            goToSettings.setVisibility(View.INVISIBLE);
                            sender1.resp="done";
                            //MainActivity.cmm="st01wfpraveensWiFi,zxccxzzxc,";
                            //MainActivity.adr="192.168.4.1";
                        }
                        else if(du.equals("done")){
                            status.setText(du);
                            trig=false;
                            guide1.setText("Succesfully credentials updated. go back to Home");

                        }else {
                            status.setText("not connected");
                        }


                    }
                });
                // }
            }
        }).start();
    }
}
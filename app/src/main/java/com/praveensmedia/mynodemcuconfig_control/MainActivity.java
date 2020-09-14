package com.praveensmedia.mynodemcuconfig_control;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {

    public static String adr="";
    public static String cmm="";
    public String xy;
    private Handler mHandler;
    private Button mStartButton;
    private TextView ipText,portText;
    public NsdHelper helper;
    private Button espConfig;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        mStartButton = (Button) findViewById(R.id.pingService);
        espConfig =(Button)findViewById(R.id.config);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        ipText =(TextView)findViewById(R.id.ipAd);
        portText =(TextView)findViewById(R.id.portMe);
        helper =new NsdHelper(this);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProgress();
                EditText serName =findViewById(R.id.searchBox);

                xy =serName.getText().toString();

                helper.mServiceName=xy;
                helper.discoverServices();

            }
        });
        espConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipText.setText("tryAgain");
                portText.setText("0");
                Button yx =findViewById(R.id.goToControls) ;
                yx.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this, ESP_Config.class);
                startActivity(intent);
            }
        });


    }
   /* @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pingService:
                startProgress();
                EditText serName =findViewById(R.id.searchBox);

                xy =serName.getText().toString();

                helper.mServiceName=xy;
                helper.discoverServices();
                break;
            case R.id.config:
                ipText.setText("tryAgain");
                portText.setText("0");
                Button yx =findViewById(R.id.goToControls) ;
                yx.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(this, ESP_Config.class);
                startActivity(intent);
                break;
        }
    }*/
    private void startProgress() {

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // mProgressBar.setProgress(currentProgressCount);
                        Log.d("TAG", "im in handler: " );
                        final String ip =charRemoveAt(helper.getIpadress(),0);
                        final String pt =helper.getPot();
                        ipText.setText(ip);
                        portText.setText(pt);
                        if(!helper.getIpadress().equals("/tryAgain")){
                            Button yx =findViewById(R.id.goToControls) ;
                            yx.setVisibility(View.VISIBLE);
                            adr=ip;

                        }else{
                            Button yx =findViewById(R.id.goToControls) ;
                            yx.setVisibility(View.INVISIBLE);

                        }

                        //mText.setText(String.valueOf(currentProgressCount));

                    }
                });
                // }
            }
        }).start();
    }

    public static String charRemoveAt(String str, int p) {
        return str.substring(0, p) + str.substring(p + 1);
    }


    public void onBtnClick (View view){
        //onStart();
        Log.d("Praveen", "Starting.");
        // mConnection = new ChatConnection(mUpdateHandler);
        //mNsdHelper.initializeNsd();
        EditText serName =findViewById(R.id.searchBox);

        xy =serName.getText().toString();

        helper.mServiceName=xy;


        try {
            int secondsToSleep=1;
            Thread.sleep(secondsToSleep * 300);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

    }
    public void onBtnClick2 (View view){
        Intent intent = new Intent(this, ControlNodeMCU.class);
        startActivity(intent);
        // onStart();

        //Button yx =findViewById(R.id.goToControls) ;
        //yx.setVisibility(View.INVISIBLE);
        helper.stopDiscovery();


    }

}
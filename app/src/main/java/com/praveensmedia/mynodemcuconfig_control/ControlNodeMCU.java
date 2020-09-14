package com.praveensmedia.mynodemcuconfig_control;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class ControlNodeMCU extends AppCompatActivity {
    private static Button d0of,d1of,d2of,d3of,d4of,d5of,d6of,d7of,d8of,d0on,d1on,d2on,d3on,d4on,d5on,d6on,d7on,d8on;

    public ping_mcu sender;
    private TextView feedb;
    private Handler mHandler1;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_node_m_c_u);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mHandler1 = new Handler();
        //
        feedb=(TextView)findViewById(R.id.feedback2);
        d0of=(Button) findViewById(R.id.d00);
        d1of=(Button) findViewById(R.id.d10);
        d2of=(Button) findViewById(R.id.d20);
        d3of=(Button) findViewById(R.id.d30);
        d4of=(Button) findViewById(R.id.d40);
        d5of=(Button) findViewById(R.id.d50);
        d6of=(Button) findViewById(R.id.d60);
        d7of=(Button) findViewById(R.id.d70);
        d8of=(Button) findViewById(R.id.d80);
        d0on=(Button) findViewById(R.id.d01);
        d1on=(Button) findViewById(R.id.d11);
        d2on=(Button) findViewById(R.id.d21);
        d3on=(Button) findViewById(R.id.d31);
        d4on=(Button) findViewById(R.id.d41);
        d5on=(Button) findViewById(R.id.d51);
        d6on=(Button) findViewById(R.id.d61);
        d7on=(Button) findViewById(R.id.d71);
        d8on=(Button) findViewById(R.id.d81);
        startProgress1();


        d0of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad0off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();


            }
        });
        d1of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad1of";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();//TextView feed =findViewById(R.id.feedback2);

            }
        });
        d2of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad2off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d3of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad3off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d4of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad4off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d5of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad5off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d6of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad6off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d7of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad7off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d8of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad8off";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d0on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad0on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d1on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad1on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d2on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad2on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d3on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad3on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d4on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad4on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d5on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad5on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d6on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad6on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d7on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad7on";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
                startProgress1();
            }
        });
        d8on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cmm="dad8on";
                //MainActivity.cmm="wfdh,fdhfdghfghtdgj,";
                ping_mcu process1 =new ping_mcu();
                process1.execute();
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
                mHandler1.post(new Runnable() {
                    @Override
                    public void run() {
                        final String xy=sender.resp;
                        feedb.setText(xy);
                        //Log.d("TAG", "im in handler: " );
                        // mProgressBar.setProgress(currentProgressCount);
                        //final String ip =charRemoveAt(helper.getIpadress(),0);
                        //final String pt =helper.getPot();
                        // ipText.setText(ip);
                        // portText.setText(pt);
                        //if(!helper.getIpadress().equals("/tryAgain")){
                        //    Button yx =findViewById(R.id.goToControls) ;
                        //     yx.setVisibility(View.VISIBLE);
                        //    adr=ip;

                        // }else{
                        //  Button yx =findViewById(R.id.goToControls) ;
                        //  yx.setVisibility(View.INVISIBLE);

                        // }

                        //mText.setText(String.valueOf(currentProgressCount));

                    }
                });
                // }
            }
        }).start();
    }

}
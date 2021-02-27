package com.praveensmedia.mynodemcuconfig_control;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.praveensmedia.mynodemcuconfig_control.ui.Controls;
import com.praveensmedia.mynodemcuconfig_control.ui.Home;



public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME=500;
    public static String ip ="";
    public static SharedPreferences sharedPreferencesControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        sharedPreferencesControls = getSharedPreferences("defaultControls", MODE_PRIVATE);
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startHome();
            }
        },SPLASH_TIME);
    }
    public void startHome(){
        boolean land = sharedPreferencesControls.getBoolean("land",false);
        String ipadr = sharedPreferencesControls.getString("ip","IP Address");
        if(!ipadr.equals("IP Address"))ip=ipadr;
        Intent intent;
        if(land){
            intent = new Intent(SplashScreen.this, Controls.class);
        }else {
            intent = new Intent(SplashScreen.this, Home.class);
        }
        startActivity(intent);
        finish();
    }

}
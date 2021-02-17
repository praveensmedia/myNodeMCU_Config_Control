package com.praveensmedia.mynodemcuconfig_control;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.praveensmedia.mynodemcuconfig_control.ui.Home;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME=2000;
    private InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        final FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                interstitialAd = null;
                // Proceed to the next level.
                startHome();
            }
        };
        InterstitialAd.load(
                SplashScreen.this,
                getString(R.string.interstialHome),
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                    }
                });

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(interstitialAd != null){
                    interstitialAd.show(SplashScreen.this);
                    //startHome();
                }else {
                    startHome();
                }
            }
        },SPLASH_TIME);
    }
    public void startHome(){
        Intent intent = new Intent(SplashScreen.this, Home.class);
        startActivity(intent);
        finish();
    }

}
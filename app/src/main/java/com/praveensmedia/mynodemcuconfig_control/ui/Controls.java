package com.praveensmedia.mynodemcuconfig_control.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.praveensmedia.mynodemcuconfig_control.R;
import com.praveensmedia.mynodemcuconfig_control.helpers.BackgroundTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.praveensmedia.mynodemcuconfig_control.ui.Home.ip;


public class Controls extends AppCompatActivity {

    private TextView feedback;
    public static String response;
    private static String urltoShow;
    private static String command;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        InterstitialAd.load(
                Controls.this,
                getString(R.string.interstialControl),
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                       // interstitialAd.show(ControlNodeMCU.this);
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                    }
                });
        AdView mAdView = findViewById(R.id.adViewControl);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        final TextView urlHit=(TextView)findViewById(R.id.urlview);
        feedback =(TextView)findViewById(R.id.response);
        Button d0of = (Button) findViewById(R.id.d00);
        Button d1of = (Button) findViewById(R.id.d10);
        Button d2of = (Button) findViewById(R.id.d20);
        Button d3of = (Button) findViewById(R.id.d30);
        Button d4of = (Button) findViewById(R.id.d40);
        Button d5of = (Button) findViewById(R.id.d50);
        Button d6of = (Button) findViewById(R.id.d60);
        Button d7of = (Button) findViewById(R.id.d70);
        Button d8of = (Button) findViewById(R.id.d80);
        Button d0on = (Button) findViewById(R.id.d01);
        Button d1on = (Button) findViewById(R.id.d11);
        Button d2on = (Button) findViewById(R.id.d21);
        Button d3on = (Button) findViewById(R.id.d31);
        Button d4on = (Button) findViewById(R.id.d41);
        Button d5on = (Button) findViewById(R.id.d51);
        Button d6on = (Button) findViewById(R.id.d61);
        Button d7on = (Button) findViewById(R.id.d71);
        Button d8on = (Button) findViewById(R.id.d81);

        final BackgroundTask pingNodeMcu= new BackgroundTask(this) {
            @Override
            public void doInBackground() {
                hitUrl(command);
            }
            @Override
            public void onPostExecute() {
                feedback.setText(response);
                urlHit.setText(urltoShow);
            }
        };

        d0of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d0L";
                pingNodeMcu.execute();
            }
        });
        d1of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d1L";
                pingNodeMcu.execute();
            }
        });
        d2of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d2L";
                pingNodeMcu.execute();
            }
        });
        d3of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d3L";
                pingNodeMcu.execute();
            }
        });
        d4of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d4L";
                pingNodeMcu.execute();
            }
        });
        d5of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d5L";
                pingNodeMcu.execute();
            }
        });
        d6of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d6L";
                pingNodeMcu.execute();
            }
        });
        d7of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d7L";
                pingNodeMcu.execute();
            }
        });
        d8of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d8L";
                pingNodeMcu.execute();
            }
        });
        d0on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d0H";
                pingNodeMcu.execute();
            }
        });
        d1on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d1H";
                pingNodeMcu.execute();
            }
        });
        d2on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d2H";
                pingNodeMcu.execute();
            }
        });
        d3on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d3H";
                pingNodeMcu.execute();
            }
        });
        d4on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d4H";
                pingNodeMcu.execute();
            }
        });
        d5on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d5H";
                pingNodeMcu.execute();
            }
        });
        d6on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d6H";
                pingNodeMcu.execute();
            }
        });
        d7on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d7H";
                pingNodeMcu.execute();
            }
        });
        d8on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command ="d8H";
                pingNodeMcu.execute();
            }
        });

    }

    public void hitUrl(String cmd){
        try {
            URL url = new URL("http:/"+ip+"/"+cmd);
            urltoShow =String.valueOf(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            inputStream));

            StringBuilder response = new StringBuilder();
            String currentLine;

            while ((currentLine = in.readLine()) != null)
                response.append(currentLine);

            in.close();
            Log.d("Response:", response.toString());
            Controls.response =response.toString();

            //return response.toString();

        }
        catch (MalformedURLException e)
        {
            Controls.response ="incorrect URL";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Controls.response ="Error-NoRequestMade";
        }
        //return null;
    }

    @Override
    public void onBackPressed() {
        finish();

        super.onBackPressed();
    }
}
package com.praveensmedia.mynodemcuconfig_control.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.praveensmedia.mynodemcuconfig_control.R;
import com.praveensmedia.mynodemcuconfig_control.helpers.viewHolder;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    public static final String SERVICE_TYPE = "_http._tcp.";//"_services._dns-sd._udp";//
    public static final String TAG = "NSD_Helper";
    public static String mServiceName = "ST";
    public static String ip ="";
    NsdServiceInfo mService;
    TextView showIP,showPort,showName,showSName,showLink;
    RecyclerView recyclerView;
    public static ArrayList<NsdServiceInfo> arrayList;
    RecyclerView.Adapter<viewHolder> myAdapter;
    private Handler handler;
    Button btnControl,btnConfig,refresh;
    private InterstitialAd interstitialAd,interstitialAd1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                interstitialAd = null;
                startControls();

            }
        };
        final FullScreenContentCallback fullScreenContentCallback1 = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                interstitialAd1 = null;
                startConfig();

            }
        };
        InterstitialAd.load(
                Home.this,
                getString(R.string.interstialControl),
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
        InterstitialAd.load(
                Home.this,
                getString(R.string.interstialConfig),
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd1 = ad;
                        interstitialAd1.setFullScreenContentCallback(fullScreenContentCallback1);
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Code to be executed when an ad request fails.
                    }
                });
        refresh = (Button)findViewById(R.id.refreshDiscover);
        btnConfig = (Button)findViewById(R.id.configure);
        btnControl = (Button)findViewById(R.id.controls);
        showIP =(TextView)findViewById(R.id.ipSpace);
        showPort =(TextView)findViewById(R.id.portSpace);
        showName =(TextView)findViewById(R.id.name);
        showSName =(TextView)findViewById(R.id.serviceName);
        showLink =(TextView)findViewById(R.id.linkurl);
        showLink.setMovementMethod(LinkMovementMethod.getInstance());
        recyclerView =(RecyclerView)findViewById(R.id.recycler);
        arrayList = new ArrayList<NsdServiceInfo>();
        mNsdManager =(NsdManager)this.getSystemService(NSD_SERVICE);
        myAdapter = new RecyclerView.Adapter<viewHolder>() {
            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
                return new viewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull viewHolder holder, int position) {
                NsdServiceInfo service = arrayList.get(position);
                holder.setContent(service.getServiceName(),service.getServiceType());
                final  NsdServiceInfo info =service;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Home.this, "Resolving: "+info.getServiceName(), Toast.LENGTH_SHORT).show();
                        mNsdManager.resolveService(info, mResolveListener);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return arrayList.size();
            }

        };
        AdView mAdView = findViewById(R.id.adViewHome);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"refreshing..",Toast.LENGTH_SHORT).show();
                arrayList.clear();
                discoverServices();
                myAdapter.notifyDataSetChanged();
            }
        });
        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interstitialAd !=null){
                    interstitialAd.show(Home.this);
                }else{
                    startControls();
                }
            }
        });
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interstitialAd1 !=null){
                    interstitialAd1.show(Home.this);
                }else{
                    startConfig();
                }
            }
        });
        discoverServices();
        initializeResolveListener();
        myAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(myAdapter);
        handler = new Handler(getMainLooper());
        refreshList();
    }
    private void refreshList(){
        Home.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myAdapter.notifyDataSetChanged();
                handler.postDelayed(this, 1000);
            }

        });
    }
    private void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }
            @Override
            public void onServiceFound(NsdServiceInfo service) {
                //initializeResolveListener();
                Log.d(TAG, "Discovered:"+service);
               // mNsdManager.resolveService(service, mResolveListener);
                arrayList.add(service);

              //  myAdapter.notifyDataSetChanged();
               // refreshList();
            }
            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.d(TAG, "service lost" + service);
                if (mService == service) {
                    mService = null;
                }
            }
            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d(TAG, "Discovery stopped: " + serviceType);
            }
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.d(TAG, "Discovery failed: Error code:" + errorCode);
            }
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.d(TAG, "Discovery failed: Error code:" + errorCode);
            }
        };
    }
    private void startControls(){
        Intent intent = new Intent(Home.this, Controls.class);
        startActivity(intent);
    }
    private void startConfig(){
        Intent intent = new Intent(Home.this, Config.class);
        startActivity(intent);
    }
    private void discoverServices() {
        stopDiscovery();  // Cancel any existing discovery request
        initializeDiscoveryListener();
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }
    private void stopDiscovery() {
        if (mDiscoveryListener != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            mDiscoveryListener = null;
        }
    }
    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "Resolve failed" + errorCode);
            }
            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                ip=String.valueOf(serviceInfo.getHost());
                showIP.setText(ip);
                showName.setText(String.valueOf(serviceInfo.getServiceName()));
                showSName.setText(String.valueOf(serviceInfo.getServiceType()));
                showPort.setText(String.valueOf(serviceInfo.getPort()));
                mService = serviceInfo;
            }
        };
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            stopDiscovery();
            arrayList.clear();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "press again to exit App", Toast.LENGTH_SHORT).show();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(null);
        stopDiscovery();
        arrayList.clear();
        finish();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        stopDiscovery();
        super.onPause();
    }

    @Override
    protected void onResume() {
        arrayList.clear();
        discoverServices();
        super.onResume();
    }
}
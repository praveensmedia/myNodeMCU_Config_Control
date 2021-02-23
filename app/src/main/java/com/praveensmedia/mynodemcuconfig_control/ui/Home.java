package com.praveensmedia.mynodemcuconfig_control.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
    public static String SERVICE_TYPE = "_http._tcp.";//"_services._dns-sd._udp";//
    public static final String TAG = "NSD_Helper";
    public static String mServiceName = "ST";
    public static String ip ="";
    public static boolean switchChecked =false;
    NsdServiceInfo mService;
    public static NsdServiceInfo nsdServiceInfo;
    TextView showIP,showPort,showName,showSName,showLink,belowUrl;
    RecyclerView recyclerView;
    public static ArrayList<NsdServiceInfo> arrayList;
    RecyclerView.Adapter<viewHolder> myAdapter;
    private Handler handler;
    Button btnControl,btnConfig,refresh;
    SwitchCompat switchCompat;
    boolean fromClick = false;
    private InterstitialAd interstitialAd,interstitialAd1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*AdView mAdView = findViewById(R.id.adViewHome);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
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
                        //interstitialAd = ad;
                        //interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
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
                        //interstitialAd1 = ad;
                        //interstitialAd1.setFullScreenContentCallback(fullScreenContentCallback1);
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
        switchCompat = (SwitchCompat)findViewById(R.id.switch1);
        belowUrl =(TextView)findViewById(R.id.beloUrlHome);
        belowUrl.setMovementMethod(LinkMovementMethod.getInstance());
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
                        if(switchChecked){
                            //Toast.makeText(Home.this, "Resolving: "+info.getServiceName(), Toast.LENGTH_SHORT).show();
                            arrayList.clear();
                            String type =info.getServiceType();
                            String[] Type =type.split("\\.");
                            SERVICE_TYPE =info.getServiceName()+"."+Type[0];
                            Log.d("fromRCY",SERVICE_TYPE);
                            fromClick = true;
                            switchCompat.setChecked(false);
                            discoverServices(SERVICE_TYPE);
                        }else{
                            Toast.makeText(Home.this, "Resolving: "+info.getServiceName(), Toast.LENGTH_SHORT).show();
                            mNsdManager.resolveService(info, mResolveListener);
                        }
                    }
                });
            }
            @Override
            public int getItemCount() {
                return arrayList.size();
            }
        };
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"refreshing..",Toast.LENGTH_SHORT).show();
                arrayList.clear();
                discoverServices(SERVICE_TYPE);
                myAdapter.notifyDataSetChanged();
            }
        });

        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ip.isEmpty()){
                    Toast.makeText(Home.this,"ipAddress is Empty",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setTitle("Enter IPAddress to Control");
                    builder.setMessage("No Service Selected.");
                    final EditText input = new EditText(Home.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ip = "/"+input.getText().toString();
                            showIP.setText(ip);
                            if(interstitialAd !=null){
                                interstitialAd.show(Home.this);
                                //startControls();
                            }else{
                                startControls();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else{
                    if(interstitialAd !=null){
                        interstitialAd.show(Home.this);
                        //startControls();
                    }else{
                        startControls();
                    }
                }
            }
        });
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ip.isEmpty()){
                    Toast.makeText(Home.this,"ipAddress is Empty",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setTitle("Enter IPAddress to Config");
                    builder.setMessage("No Service Selected.");
                    final EditText input = new EditText(Home.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ip = "/"+input.getText().toString();
                            showIP.setText(ip);
                            if(interstitialAd1 !=null){
                                interstitialAd1.show(Home.this);
                                //startConfig();
                            }else{
                                startConfig();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else{
                   if(interstitialAd1 !=null){
                        interstitialAd1.show(Home.this);
                        //startConfig();
                   }else{
                        startConfig();
                   }
                }

            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(Home.this,"you checkedme"+isChecked,Toast.LENGTH_SHORT).show();
                if(isChecked){
                    Toast.makeText(Home.this,"Discovering All Services",Toast.LENGTH_SHORT).show();
                    SERVICE_TYPE = "_services._dns-sd._udp";
                    arrayList.clear();
                    switchChecked=true;
                    discoverServices(SERVICE_TYPE);
                }else{
                    if(!fromClick){
                        SERVICE_TYPE = "_http._tcp.";
                        Toast.makeText(Home.this,"Discovering http.tcp Services only",Toast.LENGTH_SHORT).show();
                    }fromClick=false;
                    arrayList.clear();
                    switchChecked=false;
                    discoverServices(SERVICE_TYPE);
                }
            }
        });
        discoverServices(SERVICE_TYPE);
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
                showIPDetails();
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
                arrayList.add(service);
                Log.d(TAG, "Service found"+service);
            }
            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.d(TAG, "service lost" + service);
                arrayList.remove(service);
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
                Toast.makeText(Home.this,"Discovery Failed:"+errorCode,Toast.LENGTH_SHORT).show();
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
    private void discoverServices(String serviceType) {
        stopDiscovery();  // Cancel any existing discovery request
        initializeDiscoveryListener();
        mNsdManager.discoverServices(
                serviceType, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
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
              //  Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                nsdServiceInfo =serviceInfo;
                /*if(switchChecked){
                    Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                }else{
                    ip=String.valueOf(serviceInfo.getHost());
                    showIP.setText(ip);
                    showName.setText(String.valueOf(serviceInfo.getServiceName()));
                    showSName.setText(String.valueOf(serviceInfo.getServiceType()));
                    showPort.setText(String.valueOf(serviceInfo.getPort()));

                }*/
                mService = serviceInfo;

            }
        };
    }
    public void showIPDetails(){
        if(nsdServiceInfo != null) {
            if (!switchChecked) {
                // Log.d(TAG, "Resolve Succeeded. " + nsdServiceInfo);
                ip = String.valueOf(nsdServiceInfo.getHost());
                showIP.setText(ip);
                showName.setText(String.valueOf(nsdServiceInfo.getServiceName()));
                showSName.setText(String.valueOf(nsdServiceInfo.getServiceType()));
                showPort.setText(String.valueOf(nsdServiceInfo.getPort()));
            }
        }
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
        refreshList();
        discoverServices(SERVICE_TYPE);
        super.onResume();
    }
}
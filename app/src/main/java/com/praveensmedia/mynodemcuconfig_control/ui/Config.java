package com.praveensmedia.mynodemcuconfig_control.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.praveensmedia.mynodemcuconfig_control.R;
import com.praveensmedia.mynodemcuconfig_control.helpers.BackgroundTask;

import static com.praveensmedia.mynodemcuconfig_control.SplashScreen.ip;
import static com.praveensmedia.mynodemcuconfig_control.ui.Controls.command;
import static com.praveensmedia.mynodemcuconfig_control.ui.Controls.response;

public class Config extends AppCompatActivity {
    Button btnUpdate;
    EditText ssid,pass;
    TextView responseView,urlhit, showUrl;
    Controls controls;
    String command2;
    SharedPreferences sharedPreferences;
    ImageView imgYT,imgGP,imgGH,imgRpay,imgPayP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        sharedPreferences =getSharedPreferences("SingleCommand", MODE_PRIVATE);

        btnUpdate    =(Button)findViewById(R.id.updateCred);
        ssid         =(EditText)findViewById(R.id.yourSSID);
        pass         =(EditText)findViewById(R.id.yourPASS);
        responseView =(TextView)findViewById(R.id.response);
        showUrl      =(TextView)findViewById(R.id.beloUrl);
        showUrl.setMovementMethod(LinkMovementMethod.getInstance());
        urlhit       =(TextView)findViewById(R.id.URL);
        imgYT        =(ImageView)findViewById(R.id.imgYouTube);
        imgGP        =(ImageView)findViewById(R.id.imgGPlay);
        imgGH        =(ImageView)findViewById(R.id.imgGHub);
        imgRpay      =(ImageView)findViewById(R.id.imgRazor);
        imgPayP      =(ImageView)findViewById(R.id.imgPaypal);
        imgYT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://www.youtube.com/channel/UCKV9I-kh0tk8T57_uCJTEtw";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        imgGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://github.com/praveensmedia/myNodeMCU_Config_Control";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        imgGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://play.google.com/store/apps/details?id=com.praveensmedia.mynodemcuconfig_control";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        imgRpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://rzp.io/l/praveensmedia";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        imgPayP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://paypal.me/praveensmedia?locale.x=en_GB";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });
        controls =new Controls();
        String selected = "Selected IP: http:/"+ip;
        urlhit.setText(selected);
        final BackgroundTask pingNodeMcu= new BackgroundTask(this) {
            @Override
            public void doInBackground() {
                controls.hitUrl();
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
                    command2 =sharedPreferences.getString("cmd","WC");;
                    command  ="http:/"+ip+"/"+ command2 +SSID+","+PASS+",";
                    urlhit.setText(command);
                    pingNodeMcu.execute();
                }else {
                    Toast.makeText(Config.this,"SSID/PASS not be Empty",Toast.LENGTH_SHORT).show();
                    String emty ="empty credentials";
                    responseView.setText(emty);
                }
            }
        });
        btnUpdate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(Config.this,"upressed",Toast.LENGTH_SHORT).show();
                final EditText btnFun = new EditText(Config.this);
                String bFun  = sharedPreferences.getString("cmd","WC");
                btnFun.setText(bFun);
                AlertDialog.Builder builder = new AlertDialog.Builder(Config.this);
                builder.setTitle("Customise Command:");
                //builder.setMessage("No Service Resolved.");
                //final EditText input = new EditText(Controls.this);

                btnFun.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(btnFun);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newFunc =btnFun.getText().toString();
                        if(!newFunc.isEmpty()){
                            SharedPreferences.Editor editor =sharedPreferences.edit();
                            editor.putString("cmd",newFunc);
                            editor.apply();
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
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
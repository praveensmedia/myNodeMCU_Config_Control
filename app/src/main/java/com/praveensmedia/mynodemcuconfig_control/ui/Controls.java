package com.praveensmedia.mynodemcuconfig_control.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.praveensmedia.mynodemcuconfig_control.R;
import com.praveensmedia.mynodemcuconfig_control.helpers.BackgroundTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.praveensmedia.mynodemcuconfig_control.ui.Home.ip;


public class Controls extends AppCompatActivity {

    private TextView feedback;
    public static String response;
    public static String command;
    SharedPreferences sharedPreferencesBtns, sharedPreferencesCmds,sharedPreferencesTxts;
    String[] names,commands,texts;
    Button[] buttons;
    TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        /*AdView mAdView = findViewById(R.id.adViewControl);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        textViews =new TextView[9];
        textViews[0] =(TextView) findViewById(R.id.txtd0);
        textViews[1] =(TextView) findViewById(R.id.txtd1);
        textViews[2] =(TextView) findViewById(R.id.txtd2);
        textViews[3] =(TextView) findViewById(R.id.txtd3);
        textViews[4] =(TextView) findViewById(R.id.txtd4);
        textViews[5] =(TextView) findViewById(R.id.txtd5);
        textViews[6] =(TextView) findViewById(R.id.txtd6);
        textViews[7] =(TextView) findViewById(R.id.txtd7);
        textViews[8] =(TextView) findViewById(R.id.txtd8);

        buttons =new Button[18];
        buttons[0]  = (Button) findViewById(R.id.d01);
        buttons[1]  = (Button) findViewById(R.id.d11);
        buttons[2]  = (Button) findViewById(R.id.d21);
        buttons[3]  = (Button) findViewById(R.id.d31);
        buttons[4]  = (Button) findViewById(R.id.d41);
        buttons[5]  = (Button) findViewById(R.id.d51);
        buttons[6]  = (Button) findViewById(R.id.d61);
        buttons[7]  = (Button) findViewById(R.id.d71);
        buttons[8]  = (Button) findViewById(R.id.d81);
        buttons[9]  = (Button) findViewById(R.id.d00);
        buttons[10] = (Button) findViewById(R.id.d10);
        buttons[11] = (Button) findViewById(R.id.d20);
        buttons[12] = (Button) findViewById(R.id.d30);
        buttons[13] = (Button) findViewById(R.id.d40);
        buttons[14] = (Button) findViewById(R.id.d50);
        buttons[15] = (Button) findViewById(R.id.d60);
        buttons[16] = (Button) findViewById(R.id.d70);
        buttons[17] = (Button) findViewById(R.id.d80);

        names =new String[18];
        commands =new String[18];
        texts =new String[9];
        sharedPreferencesBtns = getSharedPreferences("ButtonsData", MODE_PRIVATE);
        sharedPreferencesCmds = getSharedPreferences("ButtonsCmds", MODE_PRIVATE);
        sharedPreferencesTxts = getSharedPreferences("ButtonsTxts", MODE_PRIVATE);
        final TextView urlHit=(TextView)findViewById(R.id.urlview);
        feedback =(TextView)findViewById(R.id.response);
        TextView belowUrl = (TextView) findViewById(R.id.beloUrl2);
        belowUrl.setMovementMethod(LinkMovementMethod.getInstance());
        String selected = "Selected IP: http:/"+ip;
        urlHit.setText(selected);

        final BackgroundTask pingNodeMcu= new BackgroundTask(this) {
            @Override
            public void doInBackground() {
                hitUrl();
            }
            @Override
            public void onPostExecute() {
                feedback.setText(response);
                //urlHit.setText(urltoShow);
            }
        };
        String def ="HIGH";
        String cmdD ="H";

        for(int i =0; i<9;i++){
            texts[i]=sharedPreferencesTxts.getString("txt"+i,"--D"+i+"--");
            textViews[i].setText(texts[i]);
            final int finalI = i;
            textViews[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    createDialogTxts(finalI);
                    return false;
                }
            });
        }

        for(int i =0; i<18;i++){
            int extra = i;
            if(i>=9){
                def ="LOW";
                cmdD ="L";
                extra=i-9;
            }
            commands[i] =sharedPreferencesCmds.getString("cmd"+i,"d"+extra+cmdD);
            names[i] = sharedPreferencesBtns.getString("btn"+i,def);
            buttons[i].setText(names[i]);

            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    command= "http:/"+ip+"/"+commands[finalI];
                    urlHit.setText(command);
                    pingNodeMcu.execute();
                }
            });
            buttons[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    createAlertBtns(finalI);
                    return false;
                }
            });
        }
    }

    public void hitUrl(){
        try {
            URL url = new URL(command);
            String urltoShow = String.valueOf(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = connection.getInputStream();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                inputStream));

                StringBuilder response = new StringBuilder();
                String currentLine;

                while ((currentLine = in.readLine()) != null)
                    response.append(currentLine);

                in.close();
                Log.d("Response:", response.toString());

                Controls.response = response.toString();

            } else {
                //inputStream = connection.getErrorStream();
                Controls.response ="InValidResponse..";
            }
        } catch (IOException e)
        {
            Controls.response ="NoResponse...";
        }
        //return null;
    }
    public void createAlertBtns(final int buttonNumber){
        //Toast.makeText(Controls.this,"upressed"+buttonNumber,Toast.LENGTH_SHORT).show();
        LayoutInflater factory = LayoutInflater.from(Controls.this);
        final View alertView = factory.inflate(R.layout.alert_pop, null);
        final EditText btnName = (EditText) alertView.findViewById(R.id.btnName);
        final EditText btnFun = (EditText) alertView.findViewById(R.id.btnFun);
        String def ="HIGH";
        String cmdD ="H";
        int extra = buttonNumber;
        if(buttonNumber >=9){
            def ="LOW";
            cmdD ="L";
            extra = buttonNumber-9;
        }
        String bName =names[buttonNumber] = sharedPreferencesBtns.getString("btn"+buttonNumber,def);
        String bFun =commands[extra] = sharedPreferencesCmds.getString("cmd"+buttonNumber,"d"+extra+cmdD);
        btnName.setText(bName);
        btnFun.setText(bFun);
        AlertDialog.Builder builder = new AlertDialog.Builder(Controls.this);
        builder.setTitle("Customise Button");
        //builder.setMessage("No Service Resolved.");
        //final EditText input = new EditText(Controls.this);
        btnName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        btnFun.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(alertView);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName=btnName.getText().toString();
                String newFunc=btnFun.getText().toString();
                if (!newName.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferencesBtns.edit();
                    editor.putString("btn"+buttonNumber,btnName.getText().toString());
                    editor.apply();
                    buttons[buttonNumber].setText(btnName.getText().toString());
                }/*else {
                    Toast.makeText(Controls.this,"Button Name Not Changed",Toast.LENGTH_SHORT).show();
                }*/

                if (!newFunc.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferencesCmds.edit();
                    editor.putString("cmd"+buttonNumber,btnFun.getText().toString());
                    editor.apply();
                    commands[buttonNumber] = btnFun.getText().toString();
                    //commands[buttonNumber].setText(btnName.getText().toString());
                }/*else {
                    Toast.makeText(Controls.this,"Button Command Not Changed",Toast.LENGTH_SHORT).show();
                }*/

                //Toast.makeText(Controls.this,"entered_"+btnFun.getText()+"_"+btnName.getText(),Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    public void createDialogTxts(final int txtNumber){
        final EditText btnFun = new EditText(Controls.this);
        String bFun  = sharedPreferencesTxts.getString("txt"+txtNumber,"--D"+txtNumber+"--");
        btnFun.setText(bFun);
        AlertDialog.Builder builder = new AlertDialog.Builder(Controls.this);
        builder.setTitle("Customise Text:");
        //builder.setMessage("No Service Resolved.");
        //final EditText input = new EditText(Controls.this);
        btnFun.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(btnFun);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newStr =btnFun.getText().toString();
                if(!newStr.isEmpty()){
                    SharedPreferences.Editor editor =sharedPreferencesTxts.edit();
                    editor.putString("txt"+txtNumber,newStr);
                    editor.apply();
                    textViews[txtNumber].setText(newStr);
                }
                //Toast.makeText(Controls.this,"entered_"+btnFun.getText()+"_",Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
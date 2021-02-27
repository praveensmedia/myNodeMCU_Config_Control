package com.praveensmedia.mynodemcuconfig_control.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.praveensmedia.mynodemcuconfig_control.R;
import com.praveensmedia.mynodemcuconfig_control.SplashScreen;
import com.praveensmedia.mynodemcuconfig_control.helpers.BackgroundTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.praveensmedia.mynodemcuconfig_control.SplashScreen.ip;
import static com.praveensmedia.mynodemcuconfig_control.SplashScreen.sharedPreferencesControls;

public class Controls extends AppCompatActivity {

    private TextView feedback;
    private TextView[] seekTxt;
    private TextView[] seekName;
    public static String response;
    public static String command;

    String[] names,commands,texts,seekCmd;
    Button[] buttons;
    TextView[] textViews;
    SwitchCompat landHere;
    SeekBar[] seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

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
        seekBar = new SeekBar[3];
        seekName = new TextView[3];
        seekTxt =new TextView[3];
        seekBar[0] =(SeekBar)findViewById(R.id.seekBar1);
        seekBar[1] =(SeekBar)findViewById(R.id.seekBar2);
        seekBar[2] =(SeekBar)findViewById(R.id.seekBar3);
        seekTxt[0] =(TextView)findViewById(R.id.seekBarfeed1);
        seekTxt[1] =(TextView)findViewById(R.id.seekBarfeed2);
        seekTxt[2] =(TextView)findViewById(R.id.seekBarfeed3);
        seekName[0] =(TextView)findViewById(R.id.seekBarname1);
        seekName[1] =(TextView)findViewById(R.id.seekBarname2);
        seekName[2] =(TextView)findViewById(R.id.seekBarname3);

        landHere = (SwitchCompat)findViewById(R.id.landHere);
        final TextView urlHit=(TextView)findViewById(R.id.urlview);
        feedback =(TextView)findViewById(R.id.response);
        TextView belowUrl = (TextView) findViewById(R.id.beloUrl2);

        final BackgroundTask pingNodeMcu= new BackgroundTask(this) {
            @Override
            public void doInBackground() {
                hitUrl();
            }
            @Override
            public void onPostExecute() {
                feedback.setText(response);
            }
        };

        seekCmd =new String[3];
        for(int i=0; i<3; i++){
            final int finalI = i;
            seekCmd[finalI] = sharedPreferencesControls.getString("skCmd"+finalI,"A"+finalI);
            final String name = sharedPreferencesControls.getString("skName"+finalI,"ANL"+finalI);
            final int max = sharedPreferencesControls.getInt("skMax"+finalI,255);
            final int min = sharedPreferencesControls.getInt("skMin"+finalI,0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekBar[finalI].setMin(min);
            }
            seekBar[finalI].setMax(max);
            seekTxt[finalI].setText(String.valueOf(min));
            seekName[finalI].setText(name);

            seekName[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    seekCmd[finalI] = sharedPreferencesControls.getString("skCmd"+finalI,"A"+finalI);
                    String name1 = sharedPreferencesControls.getString("skName"+finalI,"ANL"+finalI);
                    int max1 = sharedPreferencesControls.getInt("skMax"+finalI,255);
                    int min1 = sharedPreferencesControls.getInt("skMin"+finalI,0);
                    LayoutInflater factory = LayoutInflater.from(Controls.this);
                    final View alertView = factory.inflate(R.layout.seek_alert, null);
                    final EditText Ename = (EditText) alertView.findViewById(R.id.seekName);
                    final EditText Emin = (EditText) alertView.findViewById(R.id.min);
                    final EditText Emax = (EditText) alertView.findViewById(R.id.max);
                    final EditText Cmd = (EditText) alertView.findViewById(R.id.cmdIn);
                    Ename.setText(name1);
                    Emin.setText(String.valueOf(min1));
                    Emax.setText(String.valueOf(max1));
                    Cmd.setText(seekCmd[finalI]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Controls.this);
                    builder.setTitle("Customise SeekBar");
                    builder.setView(alertView);
                    builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newName=Ename.getText().toString();
                            String newMin=Emin.getText().toString();
                            String newMax=Emax.getText().toString();
                            String newCmd=Cmd.getText().toString();
                            SharedPreferences.Editor editor = sharedPreferencesControls.edit();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                seekBar[finalI].setMin(Integer.parseInt(newMin));
                                seekTxt[finalI].setText(newMin);
                            }else {
                                Toast.makeText(Controls.this,"Min Can't be set - Android 7 or Lower",Toast.LENGTH_SHORT).show();
                            }
                            seekName[finalI].setText(newName);
                            seekBar[finalI].setMax(Integer.parseInt(newMax));
                            seekCmd[finalI]=newCmd;
                            editor.putString("skName"+finalI,newName);
                            editor.putInt("skMin"+finalI,Integer.parseInt(newMin));
                            editor.putInt("skMax"+finalI,Integer.parseInt(newMax));
                            editor.putString("skCmd"+finalI,newCmd);
                            editor.apply();

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
            seekBar[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser)seekTxt[finalI].setText(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.d("seekMe","start-"+seekBar);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.d("seekMe","stop-"+seekBar.getProgress());
                   // String prog = String.valueOf(seekBar.getProgress());
                    command= "http:/"+ip+"/"+seekCmd[finalI]+seekBar.getProgress();
                    urlHit.setText(command);
                   // hitUrl();
                    pingNodeMcu.execute();
                }
            });
        }


        belowUrl.setMovementMethod(LinkMovementMethod.getInstance());
        String selected = "Selected IP: http:/"+ip;
        urlHit.setText(selected);
        boolean land = sharedPreferencesControls.getBoolean("land",false);
        landHere.setChecked(land);
        landHere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(Controls.this,"screenLand ON",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferencesControls.edit();
                    editor.putBoolean("land",true);
                    editor.apply();
                }else{
                    Toast.makeText(Controls.this,"screenLand OFF",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferencesControls.edit();
                    editor.putBoolean("land",false);
                    editor.apply();
                }
            }
        });

        String def ="HIGH";
        String cmdD ="H";
        for(int i =0; i<18;i++){
            int extra = i;
            if(i>=9){
                def ="LOW";
                cmdD ="L";
                extra=i-9;
            }else
            {
                texts[i]= sharedPreferencesControls.getString("txt"+i,"--D"+i+"--");
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
            commands[i] = sharedPreferencesControls.getString("cmd"+i,"d"+extra+cmdD);
            names[i] = sharedPreferencesControls.getString("btn"+i,def);
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
    }
    public void createAlertBtns(final int buttonNumber){
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
        String bName =names[buttonNumber] = sharedPreferencesControls.getString("btn"+buttonNumber,def);
        String bFun =commands[extra] = sharedPreferencesControls.getString("cmd"+buttonNumber,"d"+extra+cmdD);
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
                    SharedPreferences.Editor editor = sharedPreferencesControls.edit();
                    editor.putString("btn"+buttonNumber,btnName.getText().toString());
                    editor.apply();
                    buttons[buttonNumber].setText(btnName.getText().toString());
                }
                if (!newFunc.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferencesControls.edit();
                    editor.putString("cmd"+buttonNumber,btnFun.getText().toString());
                    editor.apply();
                    commands[buttonNumber] = btnFun.getText().toString();
                    //commands[buttonNumber].setText(btnName.getText().toString());
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
    }

    public void createDialogTxts(final int txtNumber){
        final EditText btnFun = new EditText(Controls.this);
        String bFun  = sharedPreferencesControls.getString("txt"+txtNumber,"--D"+txtNumber+"--");
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
                    SharedPreferences.Editor editor = sharedPreferencesControls.edit();
                    editor.putString("txt"+txtNumber,newStr);
                    editor.apply();
                    textViews[txtNumber].setText(newStr);
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
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
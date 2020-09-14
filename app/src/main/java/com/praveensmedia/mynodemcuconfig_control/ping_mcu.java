package com.praveensmedia.mynodemcuconfig_control;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.praveensmedia.mynodemcuconfig_control.MainActivity.adr;
import static com.praveensmedia.mynodemcuconfig_control.MainActivity.cmm;

public class ping_mcu extends AsyncTask<Void,Void,String> {


    public static String resp="fromNodeMCU";


    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("http://"+adr+"/"+"myNo"+cmm);
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
            resp=response.toString();

            return response.toString();





        }
        catch (MalformedURLException e)
        {

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);

    }
}

package com.toe.toeweather.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kukentaira on 9/29/15.
 */
public class NetworkTask extends AsyncTask<String, String, String> {

    private final String TAG = "Toe";

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer weatherSB = new StringBuffer("");
            String totalInfo;
            while((line = br.readLine())!=null) {
                weatherSB.append(line);
            }
            totalInfo = weatherSB.toString();
            return totalInfo;
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}

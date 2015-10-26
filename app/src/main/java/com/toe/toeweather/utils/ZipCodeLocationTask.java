package com.toe.toeweather.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TommyQu on 10/24/15.
 */
public class ZipCodeLocationTask extends AsyncTask<String, Integer, String> {

    private static final String TAG = "ToeZipCodeLocationTask";
    private Context mContext;
    private ZipCodeLocationTaskListener mZipCodeLocationTaskListener;
    private String mLocationInfo;

    public ZipCodeLocationTask(Context context, ZipCodeLocationTaskListener zipCodeLocationTaskListener) {
        mContext = context;
        mZipCodeLocationTaskListener = zipCodeLocationTaskListener;
        mLocationInfo = "";
    }

    public interface ZipCodeLocationTaskListener {
        public void getLocationByZipCodeSuccess(String locationInfo);
        public void getLocationByZipCodeFail();
    }

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
            StringBuffer geolookSB = new StringBuffer("");
            String totalInfo;
            while((line = br.readLine())!=null) {
                geolookSB.append(line);
            }
            totalInfo = geolookSB.toString();
            JSONObject jsonObject = new JSONObject(totalInfo);
            JSONObject locationObject = jsonObject.getJSONObject("location");
            mLocationInfo += locationObject.getString("state")+"/"+locationObject.getString("city");
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return mLocationInfo;
    }

    @Override
    protected void onPostExecute(String locationInfo) {
        super.onPostExecute(locationInfo);
        if(mZipCodeLocationTaskListener != null) {
            if(locationInfo != null && locationInfo.length() > 0) {
                mZipCodeLocationTaskListener.getLocationByZipCodeSuccess(locationInfo);
            }
            else {
                mZipCodeLocationTaskListener.getLocationByZipCodeFail();
            }
        }
    }
}

package com.toe.toeweather.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TommyQu on 10/7/15.
 */
public class WeatherLocation extends AsyncTask<String, Integer, String> implements LocationListener{

    private final String TAG = "ToeWeatherLocation";

    private Context mContext;
    private WeatherLocationListener mWeatherLocationListener;
    private LocationManager mLocationManager;

    public interface WeatherLocationListener {
        public void getWeatherLocationSuccess(String locationInfo);
        public void getWeatherLocationFail();
    }

    public WeatherLocation(Context context, WeatherLocationListener weatherLocationListener) {
        mContext = context;
        mWeatherLocationListener = weatherLocationListener;
    }

    @Override
    protected String doInBackground(String... params) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        String locationInfo = null;
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
            StringBuffer locationSB = new StringBuffer("");
            String totalInfo;
            while((line = br.readLine())!=null) {
                locationSB.append(line);
            }
            totalInfo = locationSB.toString();
            JSONObject jsonObject = new JSONObject(totalInfo);
            JSONObject locationObject = new JSONObject(jsonObject.getString("location"));
            locationInfo = locationObject.getString("state")+"/"+locationObject.getString("city");
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage().toString());
        }
        return locationInfo;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(mWeatherLocationListener != null) {
            if(s != null) {
                mWeatherLocationListener.getWeatherLocationSuccess(s);
            }
            else {
                mWeatherLocationListener.getWeatherLocationFail();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

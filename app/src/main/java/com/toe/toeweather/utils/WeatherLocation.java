package com.toe.toeweather.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * Created by TommyQu on 10/7/15.
 */
public class WeatherLocation {

    private final String TAG = "ToeWeatherLocation";

    private Context mContext;

    public WeatherLocation(Context context) {
        mContext = context;
    }

    /**
     * Get city and state name by longitude and latitude
     */
    public void getLocation() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLongitude(), location.getLatitude(), 1);
                    if(addresses.size() > 0) {
                        Log.d(TAG, addresses.get(0).getLocality());
                        Log.d(TAG, addresses.get(0).getCountryName());
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "Unable to load GPS", Toast.LENGTH_LONG);
                    Log.d(TAG, "getLocation:"+e.toString());
                }
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
        };
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 100, locationListener);
//        String provider = locationManager.GPS_PROVIDER;
//        Location locationParam = locationManager.getLastKnownLocation(provider);
//        double longitude = locationParam.getLongitude();
//        double latitude = locationParam.getLatitude();
    }
}

package com.toe.toeweather;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.toe.toeweather.utils.NetworkTask;

import org.json.JSONObject;


public class MainActivity extends Activity {

    private final String TAG = "Toe";
    private TextView mLocationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String serviceString = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager)getSystemService(serviceString);
        String provider = locationManager.GPS_PROVIDER;
        Location locationParam = locationManager.getLastKnownLocation(provider);
        double longitude = locationParam.getLongitude();
        double latitude = locationParam.getLatitude();
        Log.d(TAG, String.valueOf(longitude));
        Log.d(TAG, String.valueOf(latitude));

        NetworkTask networkTask = new NetworkTask();
        try {
            String totalInfo = networkTask.execute("http://api.wunderground.com/api/b00e06c861ae9642/conditions/q/CA/San_Francisco.json").get();
            JSONObject jsonObject = new JSONObject(totalInfo);
            JSONObject currentObservationObject = new JSONObject(jsonObject.getString("current_observation"));
            JSONObject displayLocationObject = new JSONObject(currentObservationObject.getString("display_location"));
            String location = (displayLocationObject.getString("full"));
            mLocationText = (TextView)findViewById(R.id.location_text);
            mLocationText.setText(location);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

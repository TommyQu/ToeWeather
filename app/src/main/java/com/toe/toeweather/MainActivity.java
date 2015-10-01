package com.toe.toeweather;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.toe.toeweather.utils.NetworkTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends Activity {

    private final String TAG = "Toe";
    private ListView mWeatherList;
    private TextView mLocationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationText = (TextView)findViewById(R.id.location_text);

        mWeatherList = (ListView)findViewById(R.id.weather_list);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, setData(), R.layout.weather_item,
                new String[]{"headerText", "descriptionValue", "temperatureValue"}, new int[]{R.id.header_text, R.id.description_value, R.id.temperature_value});
        mWeatherList.setAdapter(simpleAdapter);
        getLocation();
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

    /**
     * Get city and state name by longitude and latitude
     */
    public void getLocation() {
        String serviceString = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager)getSystemService(serviceString);
        String provider = locationManager.GPS_PROVIDER;
        Location locationParam = locationManager.getLastKnownLocation(provider);
        double longitude = locationParam.getLongitude();
        double latitude = locationParam.getLatitude();
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses.size() > 0) {
                Log.d(TAG, addresses.get(0).getLocality());
                Log.d(TAG, addresses.get(0).getCountryName());
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    /**
     * Get weather data from Wunderground API
     */
    public List<Map<String, Object>> setData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        NetworkTask networkTask = new NetworkTask();
        try {
            String totalInfo = networkTask.execute("http://api.wunderground.com/api/b00e06c861ae9642/forecast/q/VA/Arlington.json").get();
            JSONObject jsonObject = new JSONObject(totalInfo);
            JSONObject forecastObject = new JSONObject(jsonObject.getString("forecast"));
            JSONObject simpleForecastObject = new JSONObject(forecastObject.getString("simpleforecast"));
            JSONArray forecastDayArray = simpleForecastObject.getJSONArray("forecastday");
            String headerText;
            for(int i=0;i<3;i++) {
                JSONObject dayObject = forecastDayArray.getJSONObject(i);
                String descriptionValue = dayObject.getString("conditions");
                JSONObject dateObject = dayObject.getJSONObject("date");
                String weekday = dateObject.getString("weekday");
                String monthname = dateObject.getString("monthname");
                String day = dateObject.getString("day");
                String year = dateObject.getString("year");

                JSONObject highObject = dayObject.getJSONObject("high");
                JSONObject lowObject = dayObject.getJSONObject("low");
                String temperatureValue = "(F:"+lowObject.getString("fahrenheit")+"-"+highObject.getString("fahrenheit")+") "
                                        +"(C:"+lowObject.getString("celsius")+"-"+highObject.getString("celsius")+")";

                Map<String, Object> map = new HashMap<String, Object>();
                if(i == 0)
                    headerText = "Today's weather ("+monthname+" "+day+", "+year+")";
                else
                    headerText = weekday +" ("+monthname+" "+day+", "+year+")";
                map.put("headerText", headerText);
                map.put("descriptionValue", descriptionValue);
                map.put("temperatureValue", temperatureValue);
                list.add(map);
            }
            mLocationText.setText("VA");

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return list;
    }

}

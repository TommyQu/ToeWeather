package com.toe.toeweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.toe.toeweather.R;
import com.toe.toeweather.model.WeatherData;
import com.toe.toeweather.utils.WeatherListAdapter;
import com.toe.toeweather.utils.WeatherLocationTask;
import com.toe.toeweather.utils.WeatherTask;
import com.toe.toeweather.utils.ZipCodeLocationTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements WeatherTask.WeatherTaskListener, WeatherLocationTask.WeatherLocationListener, ZipCodeLocationTask.ZipCodeLocationTaskListener{

    private final String TAG = "ToeMainActivity";
    private ListView mWeatherList;
    private TextView mLocationText;
    public ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));

        mLocationText = (TextView)findViewById(R.id.location_text);

        mWeatherList = (ListView)findViewById(R.id.weather_list);

        pd = ProgressDialog.show(this, "Loading", "Loading weather data...");
        pd.setCancelable(true);
        WeatherLocationTask weatherLocationTask = new WeatherLocationTask(MainActivity.this, MainActivity.this);
        weatherLocationTask.execute("http://api.wunderground.com/api/b00e06c861ae9642/geolookup/q/");
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getWeatherLocationSuccess(String locationInfo) {
        Toast.makeText(MainActivity.this, "Load GPS location successfully!", Toast.LENGTH_SHORT).show();
        WeatherTask weatherTask = new WeatherTask(MainActivity.this, MainActivity.this);
        weatherTask.execute("http://api.wunderground.com/api/b00e06c861ae9642/forecast/q/"+locationInfo+".json", "gpsSuccess");
        mLocationText.setText(locationInfo);
    }

    @Override
    public void getWeatherLocationFail() {
//        Log.d(TAG, "Location fail! ");
        Toast.makeText(MainActivity.this, "Fail to load GPS location! Load data from zip code.", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("WeatherPreference", Context.MODE_PRIVATE);
        String zipCode = sharedPreferences.getString("zipCode", "null");
        if(zipCode.equals(null)) {
            Toast.makeText(MainActivity.this, "No zip code found!", Toast.LENGTH_SHORT).show();
        }
        else {
            WeatherTask weatherTask = new WeatherTask(MainActivity.this, MainActivity.this);
            weatherTask.execute("http://api.wunderground.com/api/b00e06c861ae9642/forecast/q/"+zipCode+".json");
            ZipCodeLocationTask zipCodeLocationTask = new ZipCodeLocationTask(MainActivity.this, MainActivity.this);
            zipCodeLocationTask.execute("http://api.wunderground.com/api/b00e06c861ae9642/geolookup/q/"+zipCode+".json");
        }
    }

    @Override
    public void getWeatherDataSuccess(WeatherData weatherData) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<weatherData.getListWeatherItem().size();i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("headerText", weatherData.getListWeatherItem().get(i).getHeaderText());
            map.put("descriptionValue", weatherData.getListWeatherItem().get(i).getDescriptionValue());
            map.put("temperatureValue", weatherData.getListWeatherItem().get(i).getTemperatureValue());
            map.put("weatherImg", weatherData.getListWeatherItem().get(i).getImgUrl());
            list.add(map);
        }
        WeatherListAdapter mWeatherListAdapter = new WeatherListAdapter(this, list, R.layout.weather_item,
                new String[]{"headerText", "descriptionValue", "temperatureValue", "weatherImg"}, new int[]{R.id.header_text, R.id.description_value, R.id.temperature_value, R.id.weather_img});
        mWeatherList.setAdapter(mWeatherListAdapter);
        pd.dismiss();
        Toast.makeText(MainActivity.this, "Load weather data successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getWeatherDataFail() {
        pd.dismiss();
        Toast.makeText(MainActivity.this, "Fail to load weather data!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getLocationByZipCodeSuccess(String locationInfo) {
        mLocationText.setText(locationInfo);
    }

    @Override
    public void getLocationByZipCodeFail() {
        Toast.makeText(MainActivity.this, "Can't get location by zip code!", Toast.LENGTH_SHORT).show();
    }
}

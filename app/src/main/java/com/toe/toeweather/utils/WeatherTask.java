package com.toe.toeweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.toe.toeweather.model.WeatherData;
import com.toe.toeweather.model.WeatherItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kukentaira on 9/29/15.
 */
public class WeatherTask extends AsyncTask<String, String, WeatherData> {

    private static final String TAG = "ToeWeatherTask";
    private Context mContext;

    public WeatherTask(Context context) {
        mContext = context;
    }

    @Override
    protected WeatherData doInBackground(String... params) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("WeatherPreference", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "No language!");
        Integer displayDays = sharedPreferences.getInt("displayDays", 0);
        String zipCode = sharedPreferences.getString("zipCode", "null");
        String degreeType = sharedPreferences.getString("degreeType", "Fahrenheit");

        WeatherData weatherData = new WeatherData();
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
            JSONObject jsonObject = new JSONObject(totalInfo);
            JSONObject forecastObject = new JSONObject(jsonObject.getString("forecast"));
            JSONObject simpleForecastObject = new JSONObject(forecastObject.getString("simpleforecast"));
            JSONArray forecastDayArray = simpleForecastObject.getJSONArray("forecastday");
            String headerText;
            for(int i=0;i<displayDays;i++) {
                JSONObject dayObject = forecastDayArray.getJSONObject(i);
                String descriptionValue = dayObject.getString("conditions");
                String iconUrl = dayObject.getString("icon_url");
                JSONObject dateObject = dayObject.getJSONObject("date");
                String weekday = dateObject.getString("weekday");
                String monthname = dateObject.getString("monthname");
                String day = dateObject.getString("day");
                String year = dateObject.getString("year");

                JSONObject highObject = dayObject.getJSONObject("high");
                JSONObject lowObject = dayObject.getJSONObject("low");
                String temperatureValue;
                if(degreeType.equals("Fahrenheit") || degreeType.equals("华氏度"))
                    temperatureValue = "(°F:"+lowObject.getString("fahrenheit")+"-"+highObject.getString("fahrenheit")+") ";
                else
                    temperatureValue = "(°C:"+lowObject.getString("celsius")+"-"+highObject.getString("celsius")+")";
                if(i == 0)
                    headerText = "Today's weather ("+monthname+" "+day+", "+year+")";
                else
                    headerText = weekday +" ("+monthname+" "+day+", "+year+")";
                WeatherItem item = new WeatherItem();
                item.setHeaderText(headerText);
                item.setDescriptionValue(descriptionValue);
                item.setTemperatureValue(temperatureValue);
                item.setImgUrl(iconUrl);;
                weatherData.getListWeatherItem().add(item);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return weatherData;
    }

    @Override
    protected void onPostExecute(WeatherData weatherData) {
        super.onPostExecute(weatherData);
    }
}

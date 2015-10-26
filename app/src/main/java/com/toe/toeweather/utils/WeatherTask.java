package com.toe.toeweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.toe.toeweather.model.WeatherData;

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
    private WeatherTaskListener mWeatherTaskListener;
    private WeatherData mWeatherData;

    public WeatherTask(Context context, WeatherTaskListener weatherTaskListener) {
        mContext = context;
        mWeatherTaskListener = weatherTaskListener;
        mWeatherData = new WeatherData();
    }

    public interface WeatherTaskListener {
        public void getWeatherDataSuccess(WeatherData weatherData);
        public void getWeatherDataFail();
    }

    /**
     * Get weather data from Wunderground API
     */
    @Override
    protected WeatherData doInBackground(String... params) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("WeatherPreference", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "English");
        Integer displayDays = sharedPreferences.getInt("displayDays", 3);
        String zipCode = sharedPreferences.getString("zipCode", "null");
        String degreeType = sharedPreferences.getString("degreeType", "Fahrenheit");

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

            ParseWeatherData parseWeatherData = new ParseWeatherData(jsonObject);
            mWeatherData = parseWeatherData.parseData(displayDays, degreeType);


//            if(params[1].equals("gpsSuccess")) {
//                ParseWeatherData parseWeatherData = new ParseWeatherData(jsonObject);
//                mWeatherData = parseWeatherData.parseDataWithGPSSuccess(displayDays, degreeType);
//            }
//            else if(params[1].equals("gpsFail")) {
//                Log.d(TAG, "gpsFail");
//                ParseWeatherData parseWeatherData = new ParseWeatherData(jsonObject);
//                mWeatherData = parseWeatherData.parseDataWithGPSFail(displayDays, degreeType, zipCode);
//            }
        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return mWeatherData;
    }

    @Override
    protected void onPostExecute(WeatherData weatherData) {
        super.onPostExecute(weatherData);
        if(mWeatherTaskListener != null) {
            if(weatherData.getListWeatherItem().size() > 0) {
                mWeatherTaskListener.getWeatherDataSuccess(weatherData);
            }
            else {
                mWeatherTaskListener.getWeatherDataFail();
            }
        }
    }
}

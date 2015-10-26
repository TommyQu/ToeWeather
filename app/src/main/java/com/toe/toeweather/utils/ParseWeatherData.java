package com.toe.toeweather.utils;

import com.toe.toeweather.model.WeatherData;
import com.toe.toeweather.model.WeatherItem;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by TommyQu on 10/24/15.
 */
public class ParseWeatherData {

    private WeatherData mWeatherData;
    private JSONObject mObject;

    public ParseWeatherData(JSONObject object) {
        mObject = object;
        mWeatherData = new WeatherData();
    }

    public WeatherData parseData(Integer displayDays, String degreeType) {
        try {
            JSONObject forecastObject = new JSONObject(mObject.getString("forecast"));
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
                item.setImgUrl(iconUrl);
                mWeatherData.getListWeatherItem().add(item);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return mWeatherData;
    }
}

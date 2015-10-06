package com.toe.toeweather.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kukentaira on 10/5/15.
 */
public class WeatherData {

    private List<WeatherItem> listWeatherItem;

    public WeatherData() {
        listWeatherItem = new ArrayList<WeatherItem>();
    }

    public List<WeatherItem> getListWeatherItem() {
        return listWeatherItem;
    }

    public void setListWeatherItem(List<WeatherItem> listWeatherItem) {
        this.listWeatherItem = listWeatherItem;
    }
}

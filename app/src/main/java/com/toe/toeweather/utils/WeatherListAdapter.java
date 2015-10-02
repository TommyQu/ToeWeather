package com.toe.toeweather.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;
import com.toe.toeweather.R;

import java.util.List;
import java.util.Map;

/**
 * Created by kukentaira on 10/2/15.
 */
public class WeatherListAdapter extends SimpleAdapter {

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public WeatherListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        ImageView imgView = (ImageView)v.getTag();
        if(imgView == null) {
            imgView = (ImageView) v.findViewById(R.id.weather_img);
            v.setTag(imgView);
        }
        String url = ((Map<String, String>)getItem(position)).get("weatherImg");
        Picasso.with(v.getContext()).load(url).into(imgView);
        return v;
    }
}

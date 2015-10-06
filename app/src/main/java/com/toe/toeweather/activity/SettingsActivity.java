package com.toe.toeweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.toe.toeweather.R;

/**
 * Created by kukentaira on 10/2/15.
 */
public class SettingsActivity extends Activity {

    private static final String TAG = "ToeSettings";
    private BootstrapButton submitBtn;
    private BootstrapButton cancelBtn;
    private Spinner languageSpinner;
    private Spinner displayDaysSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languageSpinner = (Spinner)findViewById(R.id.language_spinner);
        displayDaysSpinner = (Spinner)findViewById(R.id.display_days_spinner);


        submitBtn = (BootstrapButton)findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageSpinner.getSelectedItem().toString();
                String displayDays = displayDaysSpinner.getSelectedItem().toString();
                SharedPreferences.Editor editor = getSharedPreferences("WeatherPreference", MODE_PRIVATE).edit();
                editor.putString("language", language);
                editor.putInt("displayDays", Integer.valueOf(displayDays));
                editor.commit();
                Toast.makeText(getApplicationContext(), "Update settings successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        cancelBtn = (BootstrapButton)findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

package com.toe.toeweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.toe.toeweather.R;

import java.util.Locale;

/**
 * Created by kukentaira on 10/2/15.
 */
public class SettingsActivity extends Activity {

    private static final String TAG = "ToeSettings";
    private BootstrapButton submitBtn;
    private BootstrapButton cancelBtn;
    private Spinner languageSpinner;
    private Spinner displayDaysSpinner;
    private EditText zipCodeValue;
    private Spinner degreeTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        languageSpinner = (Spinner)findViewById(R.id.language_spinner);
        displayDaysSpinner = (Spinner)findViewById(R.id.display_days_spinner);
        zipCodeValue = (EditText)findViewById(R.id.zip_code_value);
        degreeTypeSpinner = (Spinner)findViewById(R.id.degree_type_spinner);

        SharedPreferences sharedPreferences = getSharedPreferences("WeatherPreference", Context.MODE_PRIVATE);
        String currentLanguage = sharedPreferences.getString("language", "No language!");
        Integer currentDisplayDays = sharedPreferences.getInt("displayDays", 3);
        String currentZipCode = sharedPreferences.getString("zipCode", "null");
        String currentDegreeType = sharedPreferences.getString("degreeType", "Fahrenheit");
        if(currentLanguage.equals("English") || currentLanguage.equals("英语"))
            languageSpinner.setSelection(0);
        else
            languageSpinner.setSelection(1);
        displayDaysSpinner.setSelection(currentDisplayDays-1);
        zipCodeValue.setText(currentZipCode);
        if(currentDegreeType.equals("Fahrenheit") || currentDegreeType.equals("华氏度"))
            degreeTypeSpinner.setSelection(0);
        else
            degreeTypeSpinner.setSelection(1);


        submitBtn = (BootstrapButton)findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageSpinner.getSelectedItem().toString();
                String displayDays = displayDaysSpinner.getSelectedItem().toString();
                String zipCode = zipCodeValue.getText().toString();
                String degreeType = degreeTypeSpinner.getSelectedItem().toString();
                SharedPreferences.Editor editor = getSharedPreferences("WeatherPreference", MODE_PRIVATE).edit();
                editor.putString("language", language);
                editor.putInt("displayDays", Integer.valueOf(displayDays));
                editor.putString("zipCode", zipCode);
                editor.putString("degreeType", degreeType);
                editor.commit();

                /**
                 * Change app's language settings
                 */
                if(language.equals("English") || language.equals("英语"))
                    language = "en";
                else
                    language = "zh";
                Resources res = SettingsActivity.this.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = new Locale(language.toLowerCase());
                res.updateConfiguration(conf, dm);

                /**
                 * Show success alert and kill previous activity for implementing updated settings. Return to MainActivity.
                 */
                Toast.makeText(getApplicationContext(), "Update settings successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

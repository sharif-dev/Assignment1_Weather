package com.example.testweatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout textInputCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        textInputCity = findViewById(R.id.city_text);

        Button btn = findViewById(R.id.temp_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCity()) {
                    startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                }
            }
        });

    }

    private boolean validateCity(){
        String cityName = textInputCity.getEditText().getText().toString().trim();

        if (cityName.isEmpty()){
            textInputCity.setError("Field can't be empty");
            return false;
        }
        else if (cityName.equals("Isfahan")){
            textInputCity.setError("Isfahan is very big!!!");
            // TODO: 3/18/20 Check city name validate
            return false;
        }
        else {
            textInputCity.setError(null);
            return true;
        }

    }
}

package com.example.testweatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout textInputCity;
    private NetworkManager networkManager;
    private TextView mtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        networkManager = NetworkManager.getInstance(this);
        textInputCity = findViewById(R.id.city_text);
        Button btn = findViewById(R.id.temp_btn);
        mtext = findViewById(R.id.result);

        if(!NetworkManager.isNetworkAvailable(this)){
            Toast.makeText(this, "YOU ARE OFFLINE", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "NETWORK IS AVAILABLE", Toast.LENGTH_SHORT).show();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateCity()) {
                        startActivity(new Intent(MainActivity.this, WeatherActivity.class));
//                        sendRequest();
                    }
                }
            });
        }
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

    private void sendRequest(){
        String cityName = "newyork";
        final String accessToken = "access_token=pk.eyJ1Ijoic2VjdGlvbjE5OTEiLCJhIjoiY2s4MWRpNWoyMG9mZDNkcnVrYnc5cWI4OCJ9.1GiZajLOALly-iYcGgKaUQ";
        String url =  "https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json?" + accessToken;
        url = url.replace("{query}", cityName);
        networkManager.sendRequest(url, new ResponseListener() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    JSONArray features = response.getJSONArray("features");
                    for (int i = 0; i < features.length(); i++){
                        JSONObject city = features.getJSONObject(i);
                        String name = city.getString("place_name");
                        double longitude = city.getJSONArray("center").getDouble(0);
                        double latitude = city.getJSONArray("center").getDouble(1);

                        mtext.append(name + "  :  " + longitude + "  ,  " + latitude + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "GETS ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

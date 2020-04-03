package com.example.testweatherproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.testweatherproject.Adapter.WeatherRecyclerViewAdapter;
import com.example.testweatherproject.Model.Place;
import com.example.testweatherproject.Model.Weather;
import com.example.testweatherproject.Network.ErrorListener;
import com.example.testweatherproject.Network.NetworkManager;
import com.example.testweatherproject.Network.ResponseListener;
import com.example.testweatherproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    RecyclerView weatherRecyclerView;
    Place selectedPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().hide();

        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);

        if (getIntent().hasExtra("selected_place")){
            selectedPlace = getIntent().getParcelableExtra("selected_place");
            new Thread(new DarkSkyRequestSenderThread(String.valueOf(selectedPlace.getLongitude()),
                    String.valueOf(selectedPlace.getLatitude()))).start();
        }


    }


    class DarkSkyRequestSenderThread implements Runnable{
        String url = "https://api.darksky.net/forecast/bf297f60578d85e05b22f18e85b384a6/{longitude},{latitude}";


        DarkSkyRequestSenderThread(String longitude, String latitude){
            url = url.replace("{longitude}", longitude);
            url = url.replace("{latitude}", latitude);
        }

        @Override
        public void run() {
            NetworkManager.getInstance(getApplicationContext()).sendRequest(url, new ResponseListener() {
                @Override
                public void onResult(JSONObject response) {

                    final List<Weather> weatherList = new ArrayList<>();
                    try {
                        JSONObject dailyObject = response.getJSONObject("daily");
                        JSONArray dailyDataArray = dailyObject.getJSONArray("data");
                        for (int i = 0; i < dailyDataArray.length(); i++){
                            JSONObject day = dailyDataArray.getJSONObject(i);
                            String temperatureMin = String.valueOf(day.getDouble("temperatureMin"));
                            String temperatureMax = String.valueOf(day.getDouble("temperatureMax"));
                            String icon = day.getString("icon");

                            Weather weather = new Weather();
                            weather.setTemperatureMax(temperatureMax);
                            weather.setTemperatureMin(temperatureMin);
                            weather.setIcon(icon);

                            weatherList.add(weather);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(WeatherActivity.this, weatherList);
                            weatherRecyclerView.setLayoutManager(new LinearLayoutManager(WeatherActivity.this));
                            weatherRecyclerView.setAdapter(adapter);
                        }
                    });
                }
            }, new ErrorListener() {
                @Override
                public void onError() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "ERROR FROM DARKSKY", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

}

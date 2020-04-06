package com.example.testweatherproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testweatherproject.classes.City;
import com.example.testweatherproject.classes.CustomToast;
import com.example.testweatherproject.interfaces.ErrorListener;
import com.example.testweatherproject.classes.NetworkManager;
import com.example.testweatherproject.R;
import com.example.testweatherproject.interfaces.ResponseListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout textInputCity;
    private NetworkManager networkManager;
    ListView listView;
    List<String> cityArray = new ArrayList<String>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        networkManager = NetworkManager.getInstance(this);
        textInputCity = findViewById(R.id.city_text);
        Button btn = findViewById(R.id.temp_btn);
        listView = findViewById(R.id.city_list);

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, cityArray);
        listView.setAdapter(adapter);



        //test


        if(!NetworkManager.isNetworkAvailable(this)){
            new CustomToast().toast(MainActivity.this, "You are offline");
        }
        else {
            new CustomToast().toast(MainActivity.this, "Network is available");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cityArray.clear();

                    City city = City.getInstance();
                    city.setCityName("Isfahan");
                    city.setLongitude(51.5074);
                    city.setLatitude(0.1278);

                    if (validateCity()) {
                        startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                        sendCityListRequest(textInputCity.getEditText().getText().toString().trim());
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

    private void sendCityListRequest(String cityName){
        final String accessToken = "access_token=pk.eyJ1Ijoic2VjdGlvbjE5OTEiLCJhIjoiY2s4MWRpNWoyMG9mZDNkcnVrYnc5cWI4OCJ9.1GiZajLOALly-iYcGgKaUQ";
        String url =  "https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json?" + accessToken;
        url = url.replace("{query}", cityName);


        networkManager.sendRequest(url, new ResponseListener() {
            @Override
            public void onResult(JSONObject response) {
                try {
                    JSONArray features = response.getJSONArray("features");

                    if (features.length() == 0){
                        textInputCity.setError("No such city");
                    }

                    for (int i = 0; i < features.length(); i++){
                        JSONObject city = features.getJSONObject(i);
                        String name = city.getString("place_name");
                        double longitude = city.getJSONArray("center").getDouble(0);
                        double latitude = city.getJSONArray("center").getDouble(1);

                        cityArray.add(name);
//                        cityArray.add(name + ":\n" + longitude + ", " + latitude);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onError() {
                new CustomToast().toast(MainActivity.this, "City list download gets error");
            }
        });
    }


}
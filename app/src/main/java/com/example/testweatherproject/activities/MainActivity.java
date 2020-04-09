package com.example.testweatherproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String doneMessageKey = "Loading completed key";
    private String doneMessageValue = "Loading completed";

    private AutoCompleteTextView locationInput_tv;
    private NetworkManager networkManager;
    ListView listView;
    List<String> locations = new ArrayList<>();
    List<Double> longitudes = new ArrayList<>();
    List<Double> latitudes = new ArrayList<>();
    ArrayAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        progressBar = findViewById(R.id.city_list_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        networkManager = NetworkManager.getInstance(this);
        locationInput_tv = findViewById(R.id.locationInput_tv);

        listView = findViewById(R.id.city_list);
        adapter = new ArrayAdapter<>(this,
                R.layout.activity_listview, locations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                moveToSecondActivity("onlineMode", position);
            }
        });



        if (!NetworkManager.isNetworkAvailable(this)) {
            makeAToast("You are offline \n Showing the last available data \n Please wait...");
            moveToSecondActivity("offlineMode", 0);
        } else {
            locationInput_tv.addTextChangedListener(new TextWatcher() {
                private Timer timer = new Timer();

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(final Editable s) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (!s.toString().equals("")) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                new Thread(new SendGeoCodingRequest(s.toString())).start();
                            }
                        }, 300);
                    }
                }
            });
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler sendCityListRequestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString(doneMessageKey);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    private void makeAToast(String msg) {
        new CustomToast().toast(this, msg);
    }

    private void moveToSecondActivity(String mode, int position){
        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
        if (mode.equals("onlineMode")){
            City.getInstance().setCityName(locations.get(position));
            City.getInstance().setLatitude(latitudes.get(position));
            City.getInstance().setLongitude(longitudes.get(position));
        }
        else if (mode.equals("offlineMode")){

        }
        startActivity(intent);
    }

    class SendGeoCodingRequest implements Runnable {
        final String accessToken = "access_token=pk.eyJ1Ijoic2VjdGlvbjE5OTEiLCJhIjoiY2s4MWRpNWoyMG9mZDNkcnVrYnc5cWI4OCJ9.1GiZajLOALly-iYcGgKaUQ";
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json?" + accessToken;

        SendGeoCodingRequest(String location) {
            url = url.replace("{query}", location);
        }

        @Override
        public void run() {
            networkManager.sendRequest(url, new ResponseListener() {
                @Override
                public void onResult(JSONObject response) {
                    longitudes.clear();
                    latitudes.clear();
                    try {
                        final List<String> locationArr = new ArrayList<>();
                        JSONArray features = response.getJSONArray("features");
                        for (int i = 0; i < features.length(); i++) {
                            JSONObject city = features.getJSONObject(i);
                            String text = city.getString("text");
                            String place_name = city.getString("place_name");
                            double longitude = city.getJSONArray("center").getDouble(0);
                            double latitude = city.getJSONArray("center").getDouble(1);

                            locationArr.add(text);
                            longitudes.add(longitude);
                            latitudes.add(latitude);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locations.clear();
                                locations.addAll(locationArr);
                                adapter.notifyDataSetChanged();


                                Message msg = sendCityListRequestHandler.obtainMessage();
                                Bundle bundle = new Bundle();
                                bundle.putString(doneMessageKey, doneMessageValue);
                                msg.setData(bundle);
                                sendCityListRequestHandler.sendMessage(msg);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onError(VolleyError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (error instanceof TimeoutError){
                        makeAToast("Connection Timed out!");
                    }
                    else if (error instanceof NetworkError) {
                        makeAToast("No connection! \n check your connection and try again.");
                        moveToSecondActivity("offlineMode", 0);
                    }
                    else if (error instanceof AuthFailureError){
                        makeAToast("server couldn\'t find the authenticated request.");
                    }
                    else {
//                        makeAToast("An unknown error occurred! \n try again");
                    }
                }
            });
        }
    }


}
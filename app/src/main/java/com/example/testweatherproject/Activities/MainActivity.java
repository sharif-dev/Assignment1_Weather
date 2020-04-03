package com.example.testweatherproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


import com.example.testweatherproject.Adapter.RecyclerViewAdapter;
import com.example.testweatherproject.Model.Place;
import com.example.testweatherproject.Network.ErrorListener;
import com.example.testweatherproject.Network.NetworkManager;
import com.example.testweatherproject.Network.ResponseListener;
import com.example.testweatherproject.R;
import com.example.testweatherproject.StorageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnSearchResultClickListener {
    private NetworkManager networkManager;
    private StorageManager storageManager;

//    private Thread test;
//    private Thread checkInputThread;

//    private TextInputLayout textInputCity;
    private AutoCompleteTextView autoCompleteTextView;
//    ArrayAdapter<String> suggestionAdapter;
//    private TextView mtext;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        networkManager = NetworkManager.getInstance(this);
//        textInputCity = findViewById(R.id.city_text);
//        Button btn = findViewById(R.id.temp_btn);
//        mtext = findViewById(R.id.result);
        autoCompleteTextView = findViewById(R.id.search_suggestions);
//        suggestionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
//        autoCompleteTextView.setAdapter(suggestionAdapter);
        recyclerView = findViewById(R.id.RecyclerView);

        if (!NetworkManager.isNetworkAvailable(this)) {
            Toast.makeText(this, "YOU ARE OFFLINE", Toast.LENGTH_LONG).show();
            //todo start the second activity and show the last info
        } else {
            Toast.makeText(this, "NETWORK IS AVAILABLE", Toast.LENGTH_SHORT).show();
            autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(final Editable s) {
                    new Thread(new GeoCodingRequestSenderThread(s.toString())).start();
                }
            });
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (validateCity()) {
////                        startActivity(new Intent(suggestionAdapter.insert(name, i);MainActivity.this, WeatherActivity.class));
////                        sendRequest();
//                        test.start();
//                    }
//                }
//            });
        }
    }

    @Override
    public void onSearchResultClick(int position) {
        Intent intent = new Intent(this, WeatherActivity.class);

        startActivity(intent);

    }

//    private boolean validateCity() {
//        String cityName = textInputCity.getEditText().getText().toString().trim();
//
//        if (cityName.isEmpty()) {
//            textInputCity.setError("Field can't be empty");
//            return false;
//        } else if (cityName.equals("Isfahan")) {
//            textInputCity.setError("Isfahan is very big!!!");
//            // TODO: 3/18/20 Check city name validate
//            return false;
//        } else {
//            test = new Thread(new GeoCodingRequestSenderThread(cityName));
//            textInputCity.setError(null);
//            return true;
//        }
//
//    }

//    private void sendRequest() {
//        String cityName = "tehran";
//        final String accessToken = "access_token=pk.eyJ1Ijoic2VjdGlvbjE5OTEiLCJhIjoiY2s4MWRpNWoyMG9mZDNkcnVrYnc5cWI4OCJ9.1GiZajLOALly-iYcGgKaUQ";
//        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json?" + accessToken;
//        url = url.replace("{query}", cityName);
//        networkManager.sendRequest(url, new ResponseListener() {
//            @Override
//            public void onResult(JSONObject response) {
//                try {
//                    JSONArray features = response.getJSONArray("features");
//                    for (int i = 0; i < features.length(); i++) {
//                        JSONObject city = features.getJSONObject(i);
//                        String name = city.getString("place_name");
//                        double longitude = city.getJSONArray("center").getDouble(0);
//                        double latitude = city.getJSONArray("center").getDouble(1);
//
//                        mtext.append(name + "  :  " + longitude + "  ,  " + latitude + "\n\n");
//
//                        new StorageManager().writeOnMemory(MainActivity.this,
//                                Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name),
//                                "city.json",
//                                response.toString());
//                        new StorageManager().readFromMemory(MainActivity.this,
//                                Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name),
//                                "city.json");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new ErrorListener() {
//            @Override
//            public void onError() {
//                Toast.makeText(MainActivity.this, "GETS ERROR", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    class GeoCodingRequestSenderThread implements Runnable, RecyclerViewAdapter.OnSearchResultClickListener {
        private String inputText;
        final String accessToken = "access_token=pk.eyJ1Ijoic2VjdGlvbjE5OTEiLCJhIjoiY2s4MWRpNWoyMG9mZDNkcnVrYnc5cWI4OCJ9.1GiZajLOALly-iYcGgKaUQ";
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/{query}.json?" + accessToken;
        final List<Place> places = new ArrayList<>();

        private void makeUrl() {
            url = url.replace("{query}", inputText);
        }

        GeoCodingRequestSenderThread(String inputText) {
            this.inputText = inputText;
            makeUrl();
        }

        @Override
        public void run() {
            places.clear();
            NetworkManager.getInstance().sendRequest(url, new ResponseListener() {
                @Override
                public void onResult(JSONObject response) {
//                    mtext.setText("");

                    try {
                        JSONArray features = response.getJSONArray("features");
                        for (int i = 0; i < features.length(); i++) {
                            JSONObject city = features.getJSONObject(i);
                            final String name = city.getString("place_name");
                            double longitude = city.getJSONArray("center").getDouble(0);
                            double latitude = city.getJSONArray("center").getDouble(1);
//                            mtext.append(name + "  :  " + longitude + "  ,  " + latitude + "\n\n");
//                            result.add(name);

                            //recyclerVIew
                            Place place = new Place();
                            place.setText(city.getString("text"));
                            place.setPlace_name(name);
                            place.setLongitude(longitude);
                            place.setLatitude(latitude);
                            places.add(place);
                            //end

//                            new StorageManager().writeOnMemory(MainActivity.this,
//                                    Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name),
//                                    "city.json",
//                                    response.toString());
//                            new StorageManager().readFromMemory(MainActivity.this,
//                                    Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name),
//                                    "city.json");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                suggestionAdapter.clear();
//                                suggestionAdapter.addAll(result);
                                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, places, GeoCodingRequestSenderThread.this);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                recyclerView.setAdapter(recyclerViewAdapter);
                            }
                        });

                    } catch (JSONException e) {
//                        mtext.setText("");
                        e.printStackTrace();
                    }
                }
            }, new ErrorListener() {
                @Override
                public void onError() {
//                    mtext.setText("");
                }
            });

        }

        @Override
        public void onSearchResultClick(int position) {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            intent.putExtra("selected_place", places.get(position));
            startActivity(intent);
        }
    }


}

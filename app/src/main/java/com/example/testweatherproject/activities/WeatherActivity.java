package com.example.testweatherproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testweatherproject.classes.City;
import com.example.testweatherproject.classes.CustomToast;
import com.example.testweatherproject.interfaces.ErrorListener;
import com.example.testweatherproject.classes.NetworkManager;
import com.example.testweatherproject.R;
import com.example.testweatherproject.interfaces.ResponseListener;
import com.example.testweatherproject.classes.StorageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

public class WeatherActivity extends AppCompatActivity {
    private String doneMessageKey = "Loading completed key";
    private String doneMessageValue = "Loading completed";
    private NetworkManager networkManager;
    AlertDialog dialog;


    TextView cityName;

    TextView dayOneText;
    TextView dayOneHighTemperature;
    TextView dayOneLowTemperature;
    ImageView dayOneIcon;
    ImageView dayOneDetails;

    TextView dayTwoText;
    TextView dayTwoHighTemperature;
    TextView dayTwoLowTemperature;
    ImageView dayTwoIcon;
    ImageView dayTwoDetails;

    TextView dayThreeText;
    TextView dayThreeHighTemperature;
    TextView dayThreeLowTemperature;
    ImageView dayThreeIcon;
    ImageView dayThreeDetails;

    TextView dayFourText;
    TextView dayFourHighTemperature;
    TextView dayFourLowTemperature;
    ImageView dayFourIcon;
    ImageView dayFourDetails;

    TextView dayFiveText;
    TextView dayFiveHighTemperature;
    TextView dayFiveLowTemperature;
    ImageView dayFiveIcon;
    ImageView dayFiveDetails;

    TextView daySixText;
    TextView daySixHighTemperature;
    TextView daySixLowTemperature;
    ImageView daySixIcon;
    ImageView daySixDetails;

    TextView daySevenText;
    TextView daySevenHighTemperature;
    TextView daySevenLowTemperature;
    ImageView daySevenIcon;
    ImageView daySevenDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().hide();

        networkManager = NetworkManager.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
        LayoutInflater inflater = WeatherActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_layout, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();


        cityName = findViewById(R.id.city_name);

        dayOneText = findViewById(R.id.day_one_text);
        dayOneHighTemperature = findViewById(R.id.day_one_high_temperature);
        dayOneLowTemperature = findViewById(R.id.day_one_low_temperature);
        dayOneIcon = findViewById(R.id.day_one_icon);
        dayOneDetails = findViewById(R.id.day_one_details);

        dayTwoText = findViewById(R.id.day_two_text);
        dayTwoHighTemperature = findViewById(R.id.day_two_high_temperature);
        dayTwoLowTemperature = findViewById(R.id.day_two_low_temperature);
        dayTwoIcon = findViewById(R.id.day_two_icon);
        dayTwoDetails = findViewById(R.id.day_two_details);

        dayThreeText = findViewById(R.id.day_three_text);
        dayThreeHighTemperature = findViewById(R.id.day_three_high_temperature);
        dayThreeLowTemperature = findViewById(R.id.day_three_low_temperature);
        dayThreeIcon = findViewById(R.id.day_three_icon);
        dayThreeDetails = findViewById(R.id.day_three_details);

        dayFourText = findViewById(R.id.day_four_text);
        dayFourHighTemperature = findViewById(R.id.day_four_high_temperature);
        dayFourLowTemperature = findViewById(R.id.day_four_low_temperature);
        dayFourIcon = findViewById(R.id.day_four_icon);
        dayFourDetails = findViewById(R.id.day_four_details);

        dayFiveText = findViewById(R.id.day_five_text);
        dayFiveHighTemperature = findViewById(R.id.day_five_high_temperature);
        dayFiveLowTemperature = findViewById(R.id.day_five_low_temperature);
        dayFiveIcon = findViewById(R.id.day_five_icon);
        dayFiveDetails = findViewById(R.id.day_five_details);

        daySixText = findViewById(R.id.day_six_text);
        daySixHighTemperature = findViewById(R.id.day_six_high_temperature);
        daySixLowTemperature = findViewById(R.id.day_six_low_temperature);
        daySixIcon = findViewById(R.id.day_six_icon);
        daySixDetails = findViewById(R.id.day_six_details);

        daySevenText = findViewById(R.id.day_seven_text);
        daySevenHighTemperature = findViewById(R.id.day_seven_high_temperature);
        daySevenLowTemperature = findViewById(R.id.day_seven_low_temperature);
        daySevenIcon = findViewById(R.id.day_seven_icon);
        daySevenDetails = findViewById(R.id.day_seven_details);

        City city = City.getInstance();
        sendWeatherRequest(city.getLongitude(), city.getLatitude());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        City city = City.getInstance();
        city.setJsonObject(new JSONObject());
        city.setCityName(new String());
        city.setLongitude(new Double(0));
        city.setLatitude(new Double(0));

        Log.i("CompleteLevelsTag","city object nulled in on destroy");

    }

    @Override
    protected void onStop() {
        super.onStop();
        City city = City.getInstance();
        city.setJsonObject(new JSONObject());
        city.setCityName(new String());
        city.setLongitude(new Double(0));
        city.setLatitude(new Double(0));

        Log.i("CompleteLevelsTag","city object nulled in on stop");
    }

    private Thread setComponents = new Thread(new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
            City city = City.getInstance();

            JSONObject mainObject = new JSONObject();
            JSONObject dayOneObject = new JSONObject();
            JSONObject dayTwoObject = new JSONObject();
            JSONObject dayThreeObject = new JSONObject();
            JSONObject dayFourObject = new JSONObject();
            JSONObject dayFiveObject = new JSONObject();
            JSONObject daySixObject = new JSONObject();
            JSONObject daySevenObject = new JSONObject();
            try {
                mainObject = city.getJsonObject();
                JSONObject dailyObject = (JSONObject) mainObject.get("daily");
                JSONArray daysData = dailyObject.getJSONArray("data");

                dayOneObject = (JSONObject) daysData.get(1);
                dayTwoObject = (JSONObject) daysData.get(2);
                dayThreeObject = (JSONObject) daysData.get(3);
                dayFourObject = (JSONObject) daysData.get(4);
                dayFiveObject = (JSONObject) daysData.get(5);
                daySixObject = (JSONObject) daysData.get(6);
                daySevenObject = (JSONObject) daysData.get(7);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                cityName.setText(mainObject.getString("cityName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK);

            dayOneText.setText(getDayString((today + 1) % 7));
            dayTwoText.setText(getDayString((today + 2) % 7));
            dayThreeText.setText(getDayString((today + 3) % 7));
            dayFourText.setText(getDayString((today + 4) % 7));
            dayFiveText.setText(getDayString((today + 5) % 7));
            daySixText.setText(getDayString((today + 6) % 7));
            daySevenText.setText(getDayString((today + 7) % 7));


            try {
                dayOneHighTemperature.setText(FahrenheitToCelsius(dayOneObject.getInt("temperatureHigh")) + "°");
                dayOneLowTemperature.setText(FahrenheitToCelsius(dayOneObject.getInt("temperatureLow")) + "°");
                dayOneIcon.setImageDrawable(getDrawable(dayOneObject.getString("icon")));
                final JSONObject finalDayOneObject = dayOneObject;
                dayOneDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new CustomToast().toast(WeatherActivity.this, finalDayOneObject.getString("summary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                dayTwoHighTemperature.setText(FahrenheitToCelsius(dayTwoObject.getInt("temperatureHigh")) + "°");
                dayTwoLowTemperature.setText(FahrenheitToCelsius(dayTwoObject.getInt("temperatureLow")) + "°");
                dayTwoIcon.setImageDrawable(getDrawable(dayTwoObject.getString("icon")));
                final JSONObject finalDayTwoObject = dayTwoObject;
                dayTwoDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new CustomToast().toast(WeatherActivity.this, finalDayTwoObject.getString("summary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                dayThreeHighTemperature.setText(FahrenheitToCelsius(dayThreeObject.getInt("temperatureHigh")) + "°");
                dayThreeLowTemperature.setText(FahrenheitToCelsius(dayThreeObject.getInt("temperatureLow")) + "°");
                dayThreeIcon.setImageDrawable(getDrawable(dayThreeObject.getString("icon")));
                final JSONObject finalDayThreeObject = dayThreeObject;
                dayThreeDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new CustomToast().toast(WeatherActivity.this, finalDayThreeObject.getString("summary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                dayFourHighTemperature.setText(FahrenheitToCelsius(dayFourObject.getInt("temperatureHigh")) + "°");
                dayFourLowTemperature.setText(FahrenheitToCelsius(dayFourObject.getInt("temperatureLow")) + "°");
                dayFourIcon.setImageDrawable(getDrawable(dayFourObject.getString("icon")));
                final JSONObject finalDayFourObject = dayFourObject;
                dayFourDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new CustomToast().toast(WeatherActivity.this, finalDayFourObject.getString("summary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                dayFiveHighTemperature.setText(FahrenheitToCelsius(dayFiveObject.getInt("temperatureHigh")) + "°");
                dayFiveLowTemperature.setText(FahrenheitToCelsius(dayFiveObject.getInt("temperatureLow")) + "°");
                dayFiveIcon.setImageDrawable(getDrawable(dayFiveObject.getString("icon")));
                final JSONObject finalDayFiveObject = dayFiveObject;
                dayFiveDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new CustomToast().toast(WeatherActivity.this, finalDayFiveObject.getString("summary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                daySixHighTemperature.setText(FahrenheitToCelsius(daySixObject.getInt("temperatureHigh")) + "°");
                daySixLowTemperature.setText(FahrenheitToCelsius(daySixObject.getInt("temperatureLow")) + "°");
                daySixIcon.setImageDrawable(getDrawable(daySixObject.getString("icon")));
                final JSONObject finalDaySixObject = daySixObject;
                daySixDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new CustomToast().toast(WeatherActivity.this, finalDaySixObject.getString("summary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                daySevenHighTemperature.setText(FahrenheitToCelsius(daySevenObject.getInt("temperatureHigh")) + "°");
                daySevenLowTemperature.setText(FahrenheitToCelsius(daySevenObject.getInt("temperatureLow")) + "°");
                daySevenIcon.setImageDrawable(getDrawable(daySevenObject.getString("icon")));
                final JSONObject finalDaySevenObject = daySevenObject;
                daySevenDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new CustomToast().toast(WeatherActivity.this, finalDaySevenObject.getString("summary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

                }
            });

            Log.i("CompleteLevelsTag","components set");





            Message msg = setComponentsHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(doneMessageKey, doneMessageValue);
            msg.setData(bundle);
            setComponentsHandler.sendMessage(msg);

            Log.i("CompleteLevelsTag","message sent to setComponentsHandler");

            //send message
        }
    });


    @SuppressLint("HandlerLeak")
    private final Handler setComponentsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString(doneMessageKey);
//            Toast.makeText(WeatherActivity.this, string, Toast.LENGTH_SHORT).show();
            Log.i("CompleteLevelsTag","setComponentHandler dismissed the dialog");
            dialog.dismiss();
            //remove loading dialog
        }
    };


    @SuppressLint("HandlerLeak")
    private final Handler sendWeatherRequestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString(doneMessageKey);
            setComponents.start();
            Log.i("CompleteLevelsTag","set components called");
        }
    };


    private String getDayString(int i){
        if (i == 1)
            return getString(R.string.sunday);
        else if (i == 2)
            return getString(R.string.monday);
        else if (i == 3)
            return getString(R.string.tuesday);
        else if (i == 4)
            return getString(R.string.wednesday);
        else if (i == 5)
            return getString(R.string.thursday);
        else if (i == 6)
            return getString(R.string.friday);
        else if (i == 0)
            return getString(R.string.saturday);
        return "wrong number";
    }

    private int FahrenheitToCelsius(int fahrenheit){
        return (fahrenheit - 32) * 5 / 9;
    }

    private Drawable getDrawable(String status){
        if (status.equals("clear-day"))
            return getDrawable(R.drawable.clear_day);
        if (status.equals("rain"))
            return getDrawable(R.drawable.rain);
        if (status.equals("snow"))
            return getDrawable(R.drawable.snow);
        if (status.equals("sleet"))
            return getDrawable(R.drawable.sleet);
        if (status.equals("wind"))
            return getDrawable(R.drawable.wind);
        if (status.equals("fog"))
            return getDrawable(R.drawable.fog);
        if (status.equals("cloudy"))
            return getDrawable(R.drawable.cloudy);
        if (status.equals("partly-cloudy-day"))
            return getDrawable(R.drawable.partly_cloudy_day);
        return getDrawable(R.drawable.clear_day);
    }

    private void sendWeatherRequest(double longitude, double latitude){
        String url =  "https://api.darksky.net/forecast/5f86acbe11d543e188d49a03d14eb478/{longitude},{latitude}";

        Log.i("CompleteLevelsTag","weather request sends");
        url = url.replace("{longitude}", Double.toString(longitude));
        url = url.replace("{latitude}", Double.toString(latitude));

        if(!NetworkManager.isNetworkAvailable(this)){
            new CustomToast().toast(WeatherActivity.this, "You are offline");

            String stringFromFile = new String();
            try {
                stringFromFile = new StorageManager().readFromMemory(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name),
                        "weather.json");
            } catch (IOException e) {
                e.printStackTrace();
                new CustomToast().toast(WeatherActivity.this, "No data. connect to internet and try again");
                Log.i("CompleteLevelsTag","No internet. file can not loaded from memory");
                return;
            }

            JSONObject jsonFromFile = new JSONObject();
            try {
                jsonFromFile = new JSONObject(stringFromFile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            City.getInstance().setJsonObject(jsonFromFile);
            new CustomToast().toast(WeatherActivity.this, "Previous data loaded");

            Log.i("CompleteLevelsTag","No internet. file loaded from memory");

            Message msg = sendWeatherRequestHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(doneMessageKey, doneMessageValue);
            msg.setData(bundle);
            sendWeatherRequestHandler.sendMessage(msg);

            Log.i("CompleteLevelsTag","message sent to sendWeatherHandler");

        }
        else {
            networkManager.sendRequest(url, new ResponseListener() {
                @Override
                public void onResult(JSONObject response) {
                    try {
                        response.put("cityName", City.getInstance().getCityName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new StorageManager().writeOnMemory(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name),
                            "weather.json",
                            response);

                    Log.i("CompleteLevelsTag","weather file saved and city json object set response = " + response.toString());
                    City.getInstance().setJsonObject(response);

                    Message msg = sendWeatherRequestHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString(doneMessageKey, doneMessageValue);
                    msg.setData(bundle);
                    sendWeatherRequestHandler.sendMessage(msg);

//                    dialog.dismiss();
                    Log.i("CompleteLevelsTag","message sent to sendWeatherHandler");

                }
            }, new ErrorListener() {
                @Override
                public void onError() {
                    new CustomToast().toast(WeatherActivity.this, "weather download gets error");
                }
            });


        }

    }
}
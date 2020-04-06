package com.example.testweatherproject.classes;

import org.json.JSONObject;

public class City {
    private static City instance = null;
    private String cityName;
    private Double longitude;
    private double latitude;
    private JSONObject jsonObject;



    private City(){
        cityName = new String();
        longitude = new Double(0);
        latitude = new Double(0);
        jsonObject = new JSONObject();
    }

    public static City getInstance(){
        if (instance == null)
            instance = new City();
        return instance;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}

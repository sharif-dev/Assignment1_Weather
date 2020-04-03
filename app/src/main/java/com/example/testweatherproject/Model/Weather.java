package com.example.testweatherproject.Model;

public class Weather {
    private String TemperatureMax;
    private String TemperatureMin;
    private String icon;

    public Weather(String icon, String TemperatureMax, String TemperatureMin){
        this.icon = icon;
        this.TemperatureMax = TemperatureMax;
        this.TemperatureMin = TemperatureMin;
    }

    public Weather(){

    }

    public String getTemperatureMax() {
        return TemperatureMax;
    }

    public String getTemperatureMin() {
        return TemperatureMin;
    }

    public String getIcon() {
        return icon;
    }

    public void setTemperatureMax(String temperatureMax) {
        TemperatureMax = temperatureMax;
    }

    public void setTemperatureMin(String temperatureMin) {
        TemperatureMin = temperatureMin;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}

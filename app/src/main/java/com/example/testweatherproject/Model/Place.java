package com.example.testweatherproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    private String text;
    private String place_name;
    private double longitude;
    private double latitude;

    public Place(){

    }

    public Place(String text, String place_name, double longitude, double latitude) {
        this.text = text;
        this.place_name = place_name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected Place(Parcel in) {
        text = in.readString();
        place_name = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public String getText() {
        return text;
    }

    public String getPlace_name() {
        return place_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(place_name);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}

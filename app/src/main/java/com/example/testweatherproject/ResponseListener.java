package com.example.testweatherproject;

import org.json.JSONObject;

public interface ResponseListener {
    void onResult(JSONObject response);
}

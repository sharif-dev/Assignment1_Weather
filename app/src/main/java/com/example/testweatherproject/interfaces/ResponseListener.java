package com.example.testweatherproject.interfaces;

import org.json.JSONObject;

public interface ResponseListener {
    void onResult(JSONObject response);
}

package com.example.testweatherproject.Network;

import org.json.JSONObject;

public interface ResponseListener {
    void onResult(JSONObject response);
}

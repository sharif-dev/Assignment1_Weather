package com.example.testweatherproject.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testweatherproject.interfaces.ErrorListener;
import com.example.testweatherproject.interfaces.ResponseListener;

import org.json.JSONObject;


public class NetworkManager {
    private static NetworkManager instance = null;
    private RequestQueue mQueue;

    private NetworkManager(Context context){
        mQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context){
        if(instance == null)
            instance = new NetworkManager(context);
        return instance;
    }

    public static synchronized NetworkManager getInstance(){
//        if (instance == null){
//            //todo thorw an exception
//        }
        return instance;
    }

    public void sendRequest(String url, final ResponseListener responseListener, final ErrorListener errorListener){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseListener.onResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onError();
                    }
                });
    mQueue.add(jsonObjectRequest);
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo isAvailable = connectivityManager.getActiveNetworkInfo();
        return isAvailable != null && isAvailable.isConnectedOrConnecting();
    }

}

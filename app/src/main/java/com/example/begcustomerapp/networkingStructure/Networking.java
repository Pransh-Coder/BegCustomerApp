package com.example.begcustomerapp.networkingStructure;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;


public class Networking {

    public static Networking shared = new Networking();

    public YouTubeNetworkingInterface requestInterface;

    public <T> void fetch(final Class<T> type, String urlString, Activity activity) {
        RequestQueue mQueue;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                T receivedResponse = gson.fromJson(String.valueOf(response), type);

                requestInterface.fetchRequestPerformed(receivedResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("parsingyoutube", "Error in fetching ",error);
            }
        });
        mQueue = Volley.newRequestQueue(activity);
        mQueue.add(request);
    }

    private <T> void arrayRequest(final Class<T> typeClass, String urlString, Activity activity) {
        RequestQueue mQueue;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("storeDetails","details are "+response);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                T receivedResponse = gson.fromJson(String.valueOf(response), typeClass);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("parsingyoutube", "Error in fetching ",error);
            }
        });

        mQueue = Volley.newRequestQueue(activity);
        mQueue.add(request);
    }
}

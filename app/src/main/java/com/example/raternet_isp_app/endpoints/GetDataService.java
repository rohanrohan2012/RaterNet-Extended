package com.example.raternet_isp_app.endpoints;

import com.example.raternet_isp_app.models.IPInfo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface GetDataService {
    @GET("")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getIPInfo();

    @GET("/")
    @Headers("Content-Type: text/plain")
    Call<String> getIP();
}

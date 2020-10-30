package com.example.raternet_isp_app.endpoints;

import com.example.raternet_isp_app.models.IPInfo;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GetDataService {
    @GET("/json/{url}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getIPInfo(@Path("url") String url);

    @GET("/")
    @Headers("Content-Type: text/plain")
    Call<String> getIP();
}

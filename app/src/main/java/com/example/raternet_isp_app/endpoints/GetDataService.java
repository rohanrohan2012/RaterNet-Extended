package com.example.raternet_isp_app.endpoints;

import com.example.raternet_isp_app.models.IP;
import com.example.raternet_isp_app.models.IPInfo;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface GetDataService {
    @GET("")
    Call<IPInfo> getIPInfo();

    @GET("/")
    @Headers("Content-Type: application/json")
    Call<IP> getIP();
}

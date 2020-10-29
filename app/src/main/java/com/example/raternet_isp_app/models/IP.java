package com.example.raternet_isp_app.models;

import com.google.gson.annotations.SerializedName;

public class IP {
    @SerializedName("ip")
    private String IP;

    public IP(String IP) {
        this.IP = IP;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}

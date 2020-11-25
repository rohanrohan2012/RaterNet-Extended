package com.example.raternet_isp_app.models;

public class Company {
    private String Name;
    private String Url;
    private String Number;
    private String Address;
    private String TypeofService;
    private Integer noofUsers;
    private Integer avgRating;

    public Company(String name, String url, String number, String address, String typeofService, Integer noofUsers, Integer avgRating) {
        Name = name;
        Url = url;
        Number = number;
        Address = address;
        TypeofService = typeofService;
        this.noofUsers = noofUsers;
        this.avgRating = avgRating;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTypeofService() {
        return TypeofService;
    }

    public void setTypeofService(String typeofService) {
        TypeofService = typeofService;
    }

    public Integer getNoofUsers() {
        return noofUsers;
    }

    public void setNoofUsers(Integer noofUsers) {
        this.noofUsers = noofUsers;
    }

    public Integer getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Integer avgRating) {
        this.avgRating = avgRating;
    }
}

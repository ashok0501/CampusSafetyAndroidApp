package com.sriher.campussafetyapp;

public class CardModel {
    private double lat,lon;
    private String name;
    private String phone;

    public CardModel(double lat,double lon, String name, String phone) {
        this.lat = lat;
        this.lon=lon;
        this.name = name;
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

}

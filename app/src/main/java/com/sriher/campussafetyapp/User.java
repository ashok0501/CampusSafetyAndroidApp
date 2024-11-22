package com.sriher.campussafetyapp;

public class User {
    public String uid;
    public String name;
    public String phno;
    public double latitude;
    public int status;
    public double longitude;
    public String timestamp;

    public User(String uid, String name, double latitude, double longitude, String timestamp,String phno) {
        this.uid = uid;
        this.phno=phno;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        status=0;
    }

    public User() {
status=0;
    }
}

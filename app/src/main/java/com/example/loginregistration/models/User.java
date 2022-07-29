package com.example.loginregistration.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int user_id;

    public String name;
    private String phoneNo;
    public String email;
    private String password;



    private Double lat;
    private Double lon;




    public User(String name, String phoneNo, String email, String password, Double lat, Double lon) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
        this.lat = lat;
        this.lon = lon;

    }

    public User() {
    }



    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


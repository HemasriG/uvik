package com.example.uvik.model;

import java.io.Serializable;

public class NormalizedInput implements Serializable {

    private String city;
    private String zip;
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

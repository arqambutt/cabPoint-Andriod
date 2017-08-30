package com.apps.cabpoint.cabigate.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Abdul Ghani on 7/19/2017.
 */

public class MarkerInfo {
    private LatLng location;
    private String address;

    public MarkerInfo() {
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "MarkerInfo{" +
                "location=" + location +
                ", address='" + address + '\'' +
                '}';
    }
}

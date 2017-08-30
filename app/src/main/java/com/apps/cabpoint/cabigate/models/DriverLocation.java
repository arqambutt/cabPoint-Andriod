package com.apps.cabpoint.cabigate.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Abdul Ghani on 8/20/2017.
 */

public class DriverLocation {

    private int driverId;
    private LatLng location;


    public DriverLocation(int driverId, LatLng location) {
        this.driverId = driverId;
        this.location = location;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DriverLocation{" +
                "driverId=" + driverId +
                ", location=" + location +
                '}';
    }
}

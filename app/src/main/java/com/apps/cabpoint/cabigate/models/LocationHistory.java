package com.apps.cabpoint.cabigate.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Abdul Ghani on 7/10/2017.
 */

public class LocationHistory {
    private String location;
    private String locationDescription;
    private int resource;
    private LatLng locationLatLng;

    public LocationHistory() {
    }

    public LocationHistory(String location, String locationDescription, int resource, LatLng locationLatLng) {
        this.location = location;
        this.locationDescription = locationDescription;
        this.resource = resource;
        this.locationLatLng = locationLatLng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
    public LatLng getLocationLatLng() {
        return locationLatLng;
    }

    public void setLocationLatLng(LatLng locationLatLng) {
        this.locationLatLng = locationLatLng;
    }
}

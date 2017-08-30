package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 6/7/2017.
 */


public class TrackDriver {
    private String latitude;
    private String longitude;
    private double etaTime;
    private double distance;

    public TrackDriver(String latitude, String longitude, double etaTime, double distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.etaTime = etaTime;
        this.distance = distance;
    }

    public TrackDriver() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getEtaTime() {
        return etaTime;
    }

    public void setEtaTime(double etaTime) {
        this.etaTime = etaTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "TrackDriver{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", etaTime=" + etaTime +
                ", distance=" + distance +
                '}';
    }
}

package com.apps.cabpoint.cabigate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Ghani on 7/15/2017.
 */

public class Fare {
    @SerializedName("total_fare")
    private String fare;
    @SerializedName("total_duration")
    private String duration;
    private String distance;
    private String unit;
    @SerializedName("max_passenger")
    private String maxPassenger;
    @SerializedName("max_luggage")
    private String maxLuggage;
    @SerializedName("availability_status")
    private int availableStatus;
    @SerializedName("availability_msg")
    private String message;

    public Fare() {
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMaxPassenger() {
        return maxPassenger;
    }

    public void setMaxPassenger(String maxPassenger) {
        this.maxPassenger = maxPassenger;
    }

    public String getMaxLuggage() {
        return maxLuggage;
    }

    public void setMaxLuggage(String maxLuggage) {
        this.maxLuggage = maxLuggage;
    }

    public int getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(int availableStatus) {
        this.availableStatus = availableStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Fare{" +
                "fare='" + fare + '\'' +
                ", duration='" + duration + '\'' +
                ", distance='" + distance + '\'' +
                ", unit='" + unit + '\'' +
                ", maxPassenger='" + maxPassenger + '\'' +
                ", maxLuggage='" + maxLuggage + '\'' +
                ", availableStatus=" + availableStatus +
                '}';
    }
}

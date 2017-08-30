package com.apps.cabpoint.cabigate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Ghani on 5/23/2017.
 */

public class NearestDrivers {

    @SerializedName("driver_id")
    private int driverId;
    @SerializedName("driver_name")
    private String driverName;
    @SerializedName("taxi_model")
    private String taxiModel;
    @SerializedName("phone")
    private String phone;
    @SerializedName("Callsign")
    private String callSign;
    @SerializedName("taxi_type")
    private String taxiType;
    @SerializedName("taxi_color")
    private String taxiColor;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longtude;
    @SerializedName("company_serial")
    private String companySerial;
    @SerializedName("icon")
    private String carIcon;
    @SerializedName("distance")
    private double distance;
    @SerializedName("eta_time")
    private double estimateTime;

    public NearestDrivers() {
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getTaxiModel() {
        return taxiModel;
    }

    public void setTaxiModel(String taxiModel) {
        this.taxiModel = taxiModel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getTaxiType() {
        return taxiType;
    }

    public void setTaxiType(String taxiType) {
        this.taxiType = taxiType;
    }

    public String getTaxiColor() {
        return taxiColor;
    }

    public void setTaxiColor(String taxiColor) {
        this.taxiColor = taxiColor;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtude() {
        return longtude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    public String getCompanySerial() {
        return companySerial;
    }

    public void setCompanySerial(String companySerial) {
        this.companySerial = companySerial;
    }

    public String getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(String carIcon) {
        this.carIcon = carIcon;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(double estimateTime) {
        this.estimateTime = estimateTime;
    }

    @Override
    public String toString() {
        return "NearestDrivers{" +
                "driverId=" + driverId +
                ", driverName='" + driverName + '\'' +
                ", taxiModel='" + taxiModel + '\'' +
                ", phone='" + phone + '\'' +
                ", callSign='" + callSign + '\'' +
                ", taxiType='" + taxiType + '\'' +
                ", taxiColor='" + taxiColor + '\'' +
                ", latitude=" + latitude +
                ", longtude=" + longtude +
                ", companySerial='" + companySerial + '\'' +
                ", carIcon='" + carIcon + '\'' +
                ", distance=" + distance +
                ", estimateTime=" + estimateTime +
                '}';
    }
}

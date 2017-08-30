package com.apps.cabpoint.cabigate.models;

import java.io.Serializable;

/**
 * Created by Abdul Ghani on 6/3/2017.
 */


public class RideHistory implements Serializable{
    private String jobId;
    private String pickUpLocation;
    private String dropLocation;
    private String totalFare;
    private String passengerCount;
    private String pickUpDate;
    private String distance;
    private String status;
    private String duration;
    private String paymentMethod;
    private String pick_lat;
    private String pick_lng;
    private String drop_lat;
    private String drop_lng;
    private String driverImage;
    private String userName;
    private String callSign;
    private String driverName;
    private String vehicleImage;
    private String taxiModel;
    private String driverRating;
    private String driverPhone;
    private String vehiclePlateNumber;


    public String getPick_lat() {
        return pick_lat;
    }

    public void setPick_lat(String pick_lat) {
        this.pick_lat = pick_lat;
    }

    public String getPick_lng() {
        return pick_lng;
    }

    public void setPick_lng(String pick_lng) {
        this.pick_lng = pick_lng;
    }

    public String getDrop_lat() {
        return drop_lat;
    }

    public void setDrop_lat(String drop_lat) {
        this.drop_lat = drop_lat;
    }

    public String getDrop_lng() {
        return drop_lng;
    }

    public void setDrop_lng(String drop_lng) {
        this.drop_lng = drop_lng;
    }

    public RideHistory() {
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(String passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getTaxiModel() {
        return taxiModel;
    }

    public void setTaxiModel(String taxiModel) {
        this.taxiModel = taxiModel;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    @Override
    public String toString() {
        return "RideHistory{" +
                "jobId='" + jobId + '\'' +
                ", pickUpLocation='" + pickUpLocation + '\'' +
                ", dropLocation='" + dropLocation + '\'' +
                ", totalFare='" + totalFare + '\'' +
                ", passengerCount='" + passengerCount + '\'' +
                ", pickUpDate='" + pickUpDate + '\'' +
                ", distance='" + distance + '\'' +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", pick_lat='" + pick_lat + '\'' +
                ", pick_lng='" + pick_lng + '\'' +
                ", drop_lat='" + drop_lat + '\'' +
                ", drop_lng='" + drop_lng + '\'' +
                ", driverImage='" + driverImage + '\'' +
                ", userName='" + userName + '\'' +
                ", callSign='" + callSign + '\'' +
                ", driverName='" + driverName + '\'' +
                ", vehicleImage='" + vehicleImage + '\'' +
                ", taxiModel='" + taxiModel + '\'' +
                ", driverRating='" + driverRating + '\'' +
                ", driverPhone='" + driverPhone + '\'' +
                ", vehiclePlateNumber='" + vehiclePlateNumber + '\'' +
                '}';
    }
}

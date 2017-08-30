package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 5/31/2017.
 */

public class Driver {

    private String driverId;
    private String vehicleId;
    private String driverName;
    private String firstName;
    private String callSign;
    private String driverImage;
    private String vehicleImage;
    private String phone;

    private double rating;

    public Driver() {
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", callSign='" + callSign + '\'' +
                ", driverImage='" + driverImage + '\'' +
                ", vehicleImage='" + vehicleImage + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

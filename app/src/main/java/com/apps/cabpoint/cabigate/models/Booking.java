package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 6/2/2017.
 */


public class Booking {
    private String companySerial;
    private String driverId;
    private String passengerId;
    private String firstName;
    private String currentLocation;
    private String dropLocation;
    private int passengerCount;
    private String pickUpDate;
    private int taxiModel;
    private String paymentMethod;
    private String totalFare;
    private String pickUpLatitude;
    private String pickUpLongitude;
    private String dropLatitude;
    private String dropLongitude;
    private String payPalTransId;

    public Booking() {
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getCompanySerial() {
        return companySerial;
    }

    public void setCompanySerial(String companySerial) {
        this.companySerial = companySerial;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public int getTaxiModel() {
        return taxiModel;
    }

    public void setTaxiModel(int taxiModel) {
        this.taxiModel = taxiModel;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public String getPickUpLatitude() {
        return pickUpLatitude;
    }

    public void setPickUpLatitude(String pickUpLatitude) {
        this.pickUpLatitude = pickUpLatitude;
    }

    public String getPickUpLongitude() {
        return pickUpLongitude;
    }

    public void setPickUpLongitude(String pickUpLongitude) {
        this.pickUpLongitude = pickUpLongitude;
    }

    public String getDropLatitude() {
        return dropLatitude;
    }

    public void setDropLatitude(String dropLatitude) {
        this.dropLatitude = dropLatitude;
    }

    public String getDropLongitude() {
        return dropLongitude;
    }

    public void setDropLongitude(String dropLongitude) {
        this.dropLongitude = dropLongitude;
    }

    public String getPayPalTransId() {
        return payPalTransId;
    }

    public void setPayPalTransId(String payPalTransId) {
        this.payPalTransId = payPalTransId;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "companySerial='" + companySerial + '\'' +
                ", driverId='" + driverId + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                ", dropLocation='" + dropLocation + '\'' +
                ", passengerCount=" + passengerCount +
                ", pickUpDate='" + pickUpDate + '\'' +
                ", taxiModel=" + taxiModel +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", totalFare=" + totalFare +
                ", pickUpLatitude=" + pickUpLatitude +
                ", pickUpLongitude=" + pickUpLongitude +
                ", dropLatitude=" + dropLatitude +
                ", dropLongitude=" + dropLongitude +
                '}';
    }
}

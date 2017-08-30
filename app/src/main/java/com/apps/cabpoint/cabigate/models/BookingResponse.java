package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 6/2/2017.
 */

public class BookingResponse {
    private int jobId;
    private String phone;
    private String driverId;
    private String vehicalId;
    private String userName;
    private String firstName;
    private String callSign;
    private String driverImage;
    private String vehicalImage;
    private String message;

    public BookingResponse() {
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicalId() {
        return vehicalId;
    }

    public void setVehicalId(String vehicalId) {
        this.vehicalId = vehicalId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getVehicalImage() {
        return vehicalImage;
    }

    public void setVehicalImage(String vehicalImage) {
        this.vehicalImage = vehicalImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BookingResponse{" +
                "jobId=" + jobId +
                ", phone='" + phone + '\'' +
                ", driverId='" + driverId + '\'' +
                ", vehicalId='" + vehicalId + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", callSign='" + callSign + '\'' +
                ", driverImage='" + driverImage + '\'' +
                ", vehicalImage='" + vehicalImage + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

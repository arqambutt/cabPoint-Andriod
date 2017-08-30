package com.apps.cabpoint.cabigate.models;

import android.content.Context;
import android.util.Log;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 6/21/2017.
 */

public class PassengerNotification {
    private String passengerId;
    private String jobId;
    private String status;
    private String pickUPLocation;
    private String destinationLocation;
    private String pickUpLat;
    private String pickUpLng;
    private String dropLat;
    private String dropLng;
    private String driverId;
    private String driverName;
    private String driverContact;
    private String driverImage;
    private double driverRating;
    private String vehicleId;
    private String vehicleName;
    private String vehicleModel;
    private String vehiclePlate;
    private String vehicleImage;
    private static String TAG = "Passenger Notification";
    private static Context context;

    public PassengerNotification(Context context) {
        PassengerNotification.context = context;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPickUPLocation() {
        return pickUPLocation;
    }

    public void setPickUPLocation(String pickUPLocation) {
        this.pickUPLocation = pickUPLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getPickUpLat() {
        return pickUpLat;
    }

    public void setPickUpLat(String pickUpLat) {
        this.pickUpLat = pickUpLat;
    }

    public String getPickUpLng() {
        return pickUpLng;
    }

    public void setPickUpLng(String pickUpLng) {
        this.pickUpLng = pickUpLng;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getDropLng() {
        return dropLng;
    }

    public void setDropLng(String dropLng) {
        this.dropLng = dropLng;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public double getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(double driverRating) {
        this.driverRating = driverRating;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    @Override
    public String toString() {
        return "PassengerNotification{" +
                "passengerId='" + passengerId + '\'' +
                ", jobId='" + jobId + '\'' +
                ", status='" + status + '\'' +
                ", pickUPLocation='" + pickUPLocation + '\'' +
                ", destinationLocation='" + destinationLocation + '\'' +
                ", pickUpLat='" + pickUpLat + '\'' +
                ", pickUpLng='" + pickUpLng + '\'' +
                ", dropLat='" + dropLat + '\'' +
                ", dropLng='" + dropLng + '\'' +
                ", driverId='" + driverId + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverContact='" + driverContact + '\'' +
                ", driverImage='" + driverImage + '\'' +
                ", driverRating=" + driverRating +
                ", vehicleId='" + vehicleId + '\'' +
                ", vehicleName='" + vehicleName + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehiclePlate='" + vehiclePlate + '\'' +
                ", vehicleImage='" + vehicleImage + '\'' +
                '}';
    }

    public static PassengerNotification parseJsonFromString(Context context,String notificationString){
        PassengerNotification notification = new PassengerNotification(context);
        try{
            JSONArray jsonArray = new JSONArray(notificationString);
            JSONObject object = (JSONObject) jsonArray.get(0);
            JSONObject namePairValues = object.getJSONObject(PaxApiCall.namePairValues);
            notification.setPassengerId(namePairValues.getString(PaxApiCall.passengerIdNotfication));
            JSONObject details = namePairValues.getJSONObject(PaxApiCall.detailsNoti);
            JSONObject npv = details.getJSONObject(PaxApiCall.namePairValues);
            notification.setJobId(npv.getString(PaxApiCall.jobId));
            notification.setStatus(npv.getString(PaxApiCall.status));
            notification.setPickUPLocation(npv.getString(PaxApiCall.pickUpLocNotfi));
            notification.setDestinationLocation(npv.getString(PaxApiCall.destinationLocNotifi));
            notification.setPickUpLat(npv.getString(PaxApiCall.pickUpLatitude));
            notification.setPickUpLng(npv.getString(PaxApiCall.pickUpLongitude));
            notification.setDropLat(npv.getString(PaxApiCall.dropLocLatNotifi));
            notification.setDropLng(npv.getString(PaxApiCall.dropLocLngNotifi));
            notification.setDriverId(npv.getString(PaxApiCall.driverId));
            notification.setDriverName(npv.getString(PaxApiCall.driverName));
            notification.setDriverContact(npv.getString(PaxApiCall.driverContact));
            notification.setDriverImage(npv.getString(PaxApiCall.driverImage));
            notification.setDriverRating(npv.getDouble(PaxApiCall.driverRating));
            notification.setVehicleId(npv.getString(PaxApiCall.vehicalId));
            notification.setVehicleName(npv.getString(PaxApiCall.vehicleName));
            notification.setVehicleModel(npv.getString(PaxApiCall.vehicleModel));
            notification.setVehiclePlate(npv.getString(PaxApiCall.vehiclePlate));
            notification.setVehicleImage(npv.getString(PaxApiCall.vehicleImage));
        }catch(Exception e){
            Log.d(TAG,e.toString());
        }
        return notification;
    }
}

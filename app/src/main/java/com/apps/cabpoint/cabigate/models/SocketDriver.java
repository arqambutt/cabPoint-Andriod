package com.apps.cabpoint.cabigate.models;

import android.util.Log;

import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 6/19/2017.
 */

public class SocketDriver {
//    {"lat":51.5084596,"lng":-0.0594974,"bearing":0,"speed":1.1670912504196167,"username":"khurrams","driverid":"191","companyid":"2105"}}]
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;
    @SerializedName("bearing")
    private double bearing;
    @SerializedName("speed")
    private String speed;
    @SerializedName("username")
    private String name;
    @SerializedName("driverid")
    private int id;
    @SerializedName("companyid")
    private String companyId;

    public SocketDriver() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "SocketDriver{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", bearing=" + bearing +
                ", speed=" + speed +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", companyId='" + companyId + '\'' +
                '}';
    }

    public static SocketDriver stringToSocketDriver(String driverString){
        SocketDriver socketDriver = null;
        try {
            JSONArray jsonArray = new JSONArray(driverString);
            if(jsonArray.length()>0){
                JSONObject object = (JSONObject) jsonArray.get(0);
                JSONObject namePairValues = object.getJSONObject(PaxApiCall.namePairValues);
                Gson gson = new Gson();
                socketDriver = gson.fromJson(namePairValues.toString(),SocketDriver.class);
            }
        } catch (Exception e) {
            Log.d("Driver",e.toString());
        }
        return socketDriver;
    }
}

package com.apps.cabpoint.cabigate.map;

import android.content.Context;
import android.util.Log;

import com.apps.cabpoint.cabigate.models.NearestDrivers;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiDrawCallListner;
import com.apps.cabpoint.cabigate.utils.utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abdul Ghani on 5/23/2017.
 */

public class GetNearestDrivers {

    public static ArrayList<NearestDrivers> driversArrayList = null;
    private static String TAG = "GetNearestDrivers";
    private Context context;
    private String latitude;
    private String longitude;
    private String taxiModel;
    private NearestDriversListner nearestDriversListner;
    private static double driverRange = 10;
    private static Double minETA;

    public GetNearestDrivers(Context context,String latitude, String longitude, String taxiModel, NearestDriversListner nearestDriversListner) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taxiModel = taxiModel;
        this.nearestDriversListner = nearestDriversListner;
    }

    public void getDriverList(){
        PaxApiCall.requestDrawCarApi(PaxApiCall.NEAREST_DRIVERS,PaxApiCall.getNearestDriversParams(latitude,longitude,taxiModel),paxApiDrawCallListner);
    }

    private PaxApiDrawCallListner paxApiDrawCallListner = new PaxApiDrawCallListner() {
        @Override
        public void onDrawCarRequestResult(JSONObject jsonObject) {
            onResponseOfNearestDriver(jsonObject);
        }
    };

    private void onResponseOfNearestDriver(JSONObject jsonObject){
        Log.d("Driver",jsonObject.toString());
        try{
            minETA = null;
            int  status = jsonObject.getInt(PaxApiCall.status);
            if(status == 1){
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                driversArrayList = new ArrayList<>();
                JSONArray driverList = response.getJSONArray(PaxApiCall.list);
                Gson gson = new Gson();
                for(int i = 0; i < driverList.length(); i++){
                    JSONObject driver = (JSONObject) driverList.get(i);
                    driversArrayList.add(gson.fromJson(driver.toString(),NearestDrivers.class));
                }
                nearestDriversListner.onNearestDriversSet(driversArrayList);
            }else {
                Log.d(TAG,jsonObject.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private LatLng getLatLng(String latitude,String longitude){
        return new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
    }

    private Boolean isCarInRange(LatLng passengerLatLng, LatLng carLatLng){
        return driverRange >= utils.getDistance(passengerLatLng, carLatLng);
    }
    private void getMinETA(double eta){
        if(minETA == null){
            minETA = eta;
        }else {
            if(minETA > eta){
                minETA = eta;
            }
        }
    }
}

package com.apps.cabpoint.cabigate.map;

import android.os.Handler;

import com.apps.cabpoint.cabigate.models.TrackDriver;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiDrawCallListner;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 6/7/2017.
 */

public class GetDriverTrack {
    private String driverId;
    private String passengerId;
    private String latitude;
    private String longitude;
    private TrackDriverListner trackDriverListner;

    public GetDriverTrack(String driverId, String passengerId, String latitude, String longitude, TrackDriverListner trackDriverListner) {
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.trackDriverListner = trackDriverListner;
    }

    private PaxApiDrawCallListner paxApiDrawCallListner = new PaxApiDrawCallListner() {
        @Override
        public void onDrawCarRequestResult(JSONObject jsonObject) {
            parseTrackDriverResponse(jsonObject);
        }
    };

    public void getResult(){
        PaxApiCall.requestDrawCarApi(PaxApiCall.TRACK_DRIVER,PaxApiCall.getTrackDriverParams(driverId,passengerId,longitude,latitude),paxApiDrawCallListner);
    }

    private void parseTrackDriverResponse(JSONObject jsonObject){
        try{
            int status = jsonObject.getInt(PaxApiCall.status);
            if(status == 1){
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                TrackDriver trackDriver = new TrackDriver();
                trackDriver.setLatitude(response.getString(PaxApiCall.driverLatitude));
                trackDriver.setLongitude(response.getString(PaxApiCall.driverLongitude));
                trackDriver.setDistance(response.getDouble(PaxApiCall.distance));
                trackDriver.setEtaTime(response.getDouble(PaxApiCall.estimateTime));
                trackDriverListner.onTrackDriverResponse(trackDriver);
            }
        }catch (Exception e){
            e.printStackTrace();
            onExceptionRequestAgain();
        }
    }

    private void onExceptionRequestAgain(){
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getResult();
            }
        },1000);
    }
}

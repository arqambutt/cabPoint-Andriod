package com.apps.cabpoint.cabigate.map;

import android.app.Activity;
import android.os.Handler;

import com.apps.cabpoint.cabigate.models.DriverLocation;
import com.apps.cabpoint.cabigate.models.SocketDriver;
import com.apps.cabpoint.cabigate.models.TrackDriver;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

/**
 * Created by Abdul Ghani on 6/22/2017.
 */

public class GetTrackDirver {

    private GoogleMap googleMap;
    private LatLng location;
    private Activity context;
    private String driverId;
    private int id;
    private String passengerId;
    private DrawCarOnMap driverCar;
    private String imageUrl;
    private boolean cancel;
    private DriverLocation driverLocation;
    private DrawCarOnMap carOnMapClone;
    private static int REQUEST_WAIT_TIME = 5000;

    public GetTrackDirver(Activity context, GoogleMap googleMap, LatLng location, String driverId, String passengerId, String imageUrl) {
        this.googleMap = googleMap;
        this.location = location;
        this.context = context;
        this.driverId = driverId;
        this.id = Integer.parseInt(driverId);
        this.passengerId = passengerId;
        driverCar = null;
        driverLocation = null;
        this.imageUrl = imageUrl;
        cancel = false;
    }

    public void getDriver() {
        GetDriverTrack getDriverTrack = new GetDriverTrack(driverId, passengerId, location.latitude + "", location.longitude + "", trackDriverListner);
        getDriverTrack.getResult();
    }

    private TrackDriverListner trackDriverListner = new TrackDriverListner() {
        @Override
        public void onTrackDriverResponse(TrackDriver trackDriver) {
            drawDriverCarOnMap(trackDriver);
        }
    };


    private void drawDriverCarOnMap(TrackDriver trackDriver) {
        if (driverCar == null) {
            driverCar = new DrawCarOnMap(context, id, googleMap, getLocation(trackDriver), false, imageUrl);
            drawTrackCar(driverCar);
            getDriver();
        } else {
            driverCar.onLocationChanged(getLocation(trackDriver));
        }
        drawTrackCar(driverCar);
        repeatTask();
    }

    private LatLng getLocation(TrackDriver trackDriver) {
        return new LatLng(Double.parseDouble(trackDriver.getLatitude()), Double.parseDouble(trackDriver.getLongitude()));
    }

    private void drawTrackCar(DrawCarOnMap drawCarOnMap) {
        drawCarOnMap.drawCar();
        LatLng latLng = drawCarOnMap.getNewLocation();
        if (latLng != null && ((MapActivity) context).drawPolyLine != null && ((MapActivity) context).drawPolyLine.getPolyLine() != null) {
            ((MapActivity) context).drawPolyLine.removePolyLineAtLocation(drawCarOnMap.getNewLocation());
        }
    }

    public void updateDriverWithSocketData(final Object... args) {
        Gson gson = new Gson();
        String json = gson.toJson(args);
        SocketDriver socketDriver = SocketDriver.stringToSocketDriver(json);
        if (socketDriver.getId() == id) {
            if(driverLocation==null){
                driverLocation = new DriverLocation(id, new LatLng(socketDriver.getLatitude(), socketDriver.getLongitude()));
            }else {
                driverLocation.setLocation(new LatLng(socketDriver.getLatitude(), socketDriver.getLongitude()));
            }
//            LatLng updateLocation = new LatLng(socketDriver.getLatitude(), socketDriver.getLongitude());
//            if (driverCar != null)
//                driverCar.onLocationChanged(updateLocation);
        }
    }

    private void repeatTask() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCancel()) {
                    if(driverLocation!=null && driverCar!=null){
                        driverCar.onLocationChanged(driverLocation.getLocation());
                        drawTrackCar(driverCar);
                    }
                    repeatTask();
                } else {
                    driverCar.setHideCar(true);
                    driverCar.getCarMarker().remove();
                }
            }
        }, REQUEST_WAIT_TIME);
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public LatLng getDriverLocation() {
        if (driverCar != null) {
            return driverCar.getNewLocation();
        } else {
            return null;
        }
    }
}

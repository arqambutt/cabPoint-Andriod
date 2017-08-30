package com.apps.cabpoint.cabigate.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;

import com.apps.cabpoint.cabigate.models.DriverLocation;
import com.apps.cabpoint.cabigate.models.NearestDrivers;
import com.apps.cabpoint.cabigate.models.SocketDriver;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Abdul Ghani on 6/20/2017.
 */

public class GetCars {
    private LatLng location;
    private GoogleMap googleMap;
    private ArrayList<DrawCarOnMap> carOnMapArrayList;
    private Activity context;
    private HashMap<Integer, DriverLocation> socketDriverLocation;

    public static String taxiModel;
    private static boolean drawCarsCompleted = false;
    private boolean isCancel;

    @SuppressLint("UseSparseArrays")
    public GetCars(Activity context, LatLng location, GoogleMap googleMap) {
        this.location = location;
        this.googleMap = googleMap;
        carOnMapArrayList = null;
        socketDriverLocation = new HashMap<>();
        this.context = context;
        taxiModel = null;
        drawCarsCompleted = false;
        isCancel = false;
    }

    private NearestDriversListner nearestDriversListner = new NearestDriversListner() {
        @Override
        public void onNearestDriversSet(ArrayList<NearestDrivers> driversArrayList) {
            if (driversArrayList != null) {
                setCancel(false);
                drawCars(driversArrayList);
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNearestDrivers();
                    }
                }, 1000);
            }
        }
    };

    public void getNearestDrivers() {
        drawCarsCompleted = false;
        GetNearestDrivers getNearestDrivers = new GetNearestDrivers(context, location.latitude + "", location.longitude + "", taxiModel, nearestDriversListner);
        getNearestDrivers.getDriverList();
    }

    private void drawCars(ArrayList<NearestDrivers> driversArrayList) {
        carOnMapArrayList = new ArrayList<>();
        for (NearestDrivers driver : driversArrayList) {
            LatLng latLng = new LatLng(driver.getLatitude(), driver.getLongtude());
            DrawCarOnMap drawCarOnMap = new DrawCarOnMap(context, driver.getDriverId(), googleMap, latLng, false, driver.getCarIcon());
            carOnMapArrayList.add(drawCarOnMap);
        }
        drawCarsFromList(carOnMapArrayList);
        repeatTask();
    }

    private void drawCarsFromList(final ArrayList<DrawCarOnMap> carOnMaps) {
        if (drawCarsCompleted) {
            for (int i = 0; i < carOnMaps.size(); i++) {

                for (int j = 0; j < socketDriverLocation.size(); j++) {
                    DriverLocation driverLocation = socketDriverLocation.get(carOnMaps.get(i).getDriverId());
                    if (driverLocation != null) {
                        carOnMaps.get(i).onLocationChanged(driverLocation.getLocation());
//                        if(carOnMaps.get(i).getDriverId() == 7)
//                        if (((MapActivity) context).drawPolyLine != null && ((MapActivity) context).drawPolyLine.getPolyLine() != null) {
//                            ((MapActivity) context).drawPolyLine.removePolyLineAtLocation(driverLocation.getLocation());
//                        }
                        break;
                    }
                }
            }
        }
        for (DrawCarOnMap drawCarOnMap : carOnMaps) {
            drawCarOnMap.drawCar();
        }
        drawCarsCompleted = true;
    }

    private void disableAllCarsOnMap(ArrayList<DrawCarOnMap> carOnMaps) {
        for (DrawCarOnMap drawCarOnMap : carOnMaps) {
            drawCarOnMap.setHideCar(true);
        }
    }

    private void removeDisableCars(ArrayList<DrawCarOnMap> carOnMaps) {
        ArrayList<Integer> itemsToRemove = new ArrayList<>();
        for (int i = 0; i < carOnMaps.size(); i++) {
            if (carOnMaps.get(i).getHideCar()) {
                itemsToRemove.add(i);
            }
        }
        Collections.sort(itemsToRemove);
        for (int i = itemsToRemove.size() - 1; i >= 0; i--) {
            carOnMaps.get(i).getCarMarker().remove();
            carOnMaps.remove(i);
        }
    }

    private void removeAllCars() {
        disableAllCarsOnMap(carOnMapArrayList);
        removeDisableCars(carOnMapArrayList);
    }

    private void removeCars(ArrayList<DrawCarOnMap> carOnMaps) {
        for (int i = 0; i < carOnMaps.size(); i++) {
            carOnMaps.get(i).getCarMarker().remove();
//            carOnMaps.remove(i);
        }
    }

    public void updateCarOnMapWithSocketData(final Object... args) {
        Gson gson = new Gson();
        String json = gson.toJson(args);
        SocketDriver socketDriver = SocketDriver.stringToSocketDriver(json);
        DriverLocation driverLocation = new DriverLocation(socketDriver.getId(), new LatLng(socketDriver.getLatitude(), socketDriver.getLongitude()));
        socketDriverLocation.put(driverLocation.getDriverId(), driverLocation);
    }

    public boolean isCancel() {
        if (isCancel) {
            removeCars(carOnMapArrayList);
        }
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public String getTaxiModel() {
        return taxiModel;
    }

    public void setTaxiModel(String taxiModel) {
        GetCars.taxiModel = taxiModel;
    }

    private void repeatTask() {
        final Handler handler = new Handler();
        int REQUEST_WAIT_TIME = 2000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCancel()) {
                    if (carOnMapArrayList != null)
                        drawCarsFromList(carOnMapArrayList);
                    repeatTask();
                }
            }
        }, REQUEST_WAIT_TIME);
    }
}

package com.apps.cabpoint.cabigate.map;

import android.app.Activity;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.utils.PicassoMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdul Ghani on 5/23/2017.
 */

public class DrawCarOnMap implements Cloneable {
    private int driverId;
    private LatLng previousLocation;
    private LatLng newLocation;
    private Boolean hideCar;
    private Marker carMarker;
    private GoogleMap googleMap = null;
    private Boolean isMarkerRotating;
    private Activity context;
    private String url;
    public static Boolean isCompleted = true;
    private static List<Target> targets = null;
    private static int rotateDuration = 1700;
    private static int animateDuration = 1900;


    @Override
    public String toString() {
        return "DrawCarOnMap{" +
                "driverId='" + driverId + '\'' +
                ", previousLocation=" + previousLocation +
                ", newLocation=" + newLocation +
                ", hideCar=" + hideCar +
                '}';
    }

    public DrawCarOnMap(Activity context, int driverId, GoogleMap googleMap, LatLng newLocation, Boolean hideCar, String url) {
        if (targets == null) {
            targets = new ArrayList<>();
        }
        this.context = context;
        this.driverId = driverId;
        this.googleMap = googleMap;
        this.previousLocation = null;
        this.newLocation = newLocation;
        this.hideCar = hideCar;
        this.carMarker = googleMap.addMarker(getMarkerWithPicasso());
        this.url = url;
        Target picassoMarker = new PicassoMarker(context, carMarker);
        targets.add(picassoMarker);
        Picasso.with(context).load(url).resize(context.getResources().getDimensionPixelOffset(R.dimen._30sdp), 0).into(picassoMarker);
        isMarkerRotating = false;
    }

    @Override
    protected DrawCarOnMap clone() throws CloneNotSupportedException {
        DrawCarOnMap clone = null;
        try {
            clone = (DrawCarOnMap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }


    private GoogleMap getGoogleMap() {
        return googleMap;
    }

    private LatLng getPreviousLocation() {
        return previousLocation;
    }

    private void setPreviousLocation(LatLng previousLocation) {
        this.previousLocation = previousLocation;
    }

    public LatLng getNewLocation() {
        return newLocation;
    }

    private void setNewLocation(LatLng newLocation) {
        this.newLocation = newLocation;
    }

    public Boolean getHideCar() {
        return hideCar;
    }

    public void setHideCar(Boolean hideCar) {
        this.hideCar = hideCar;
    }

    public Marker getCarMarker() {
        return carMarker;
    }

    public void onLocationChanged(LatLng location) {
        setPreviousLocation(getNewLocation());
        setNewLocation(location);
    }

    private void animateMarker() {
        final Handler handler = new Handler();
        final long start = getSystemTime();
        Projection proj = getGoogleMap().getProjection();
        Point startPoint = proj.toScreenLocation(getCarMarker().getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);

        final Interpolator interpolator = new LinearInterpolator();
        final LatLng newLocation = getNewLocation();
        if (newLocation == null)
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = getSystemTime() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / animateDuration);
                double lng = t * newLocation.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * newLocation.latitude + (1 - t)
                        * startLatLng.latitude;
                getCarMarker().setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (getHideCar()) {
                        getCarMarker().setVisible(false);
                    } else {
                        getCarMarker().setVisible(true);
                    }
                    setCompleted(true);
                }
            }
        });
    }

    public void drawCar() {
        if (googleMap != null) {
            if (hasLocations()) {
                if (isDistanceToMove(getPreviousLocation(), getNewLocation())) {
                    rotateMarker();
                    animateMarker();
                }
            }
        } else {
            String TAG = "DrawCarOnMap";
            Log.d(TAG, "Map is null, set map!!!");
        }
    }

    private Boolean isLocationChanged() {
        if (hasLocations()) {
            return changeInLocation(getPreviousLocation(), getNewLocation());
        } else {
            return true;
        }
    }

    private Boolean changeInLocation(LatLng previousLocation, LatLng newLocation) {
        double changeLatitude, changeLongitude, change;
        change = 0.0;
        changeLatitude = newLocation.latitude - previousLocation.latitude;
        changeLongitude = newLocation.longitude - previousLocation.longitude;
        if (getPositiveValue(changeLatitude) > change && getPositiveValue(changeLongitude) > change) {
            return true;
        } else {
            return false;
        }
    }

    private MarkerOptions getMarkerWithPicasso() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(getNewLocation());
        return markerOptions;
    }

    private double bearingBetweenLocations() {

        double PI = 3.14159;
        double lat1 = getPreviousLocation().latitude * PI / 180;
        double long1 = getPreviousLocation().longitude * PI / 180;
        double lat2 = getNewLocation().latitude * PI / 180;
        double long2 = getNewLocation().longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    private void rotateMarker() {
        setCompleted(false);
        if (!isMarkerRotating) {

            if (getNewLocation() == null)
                return;
            final double bearing = bearingBetweenLocations();
            if (bearing == 0.0)
                return;

            final Handler handler = new Handler();
            final long start = getSystemTime();
            final float startRotation = getCarMarker().getRotation();
            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = getSystemTime() - start;
                    float t = interpolator.getInterpolation((float) elapsed / rotateDuration);

                    float rot = (float) (t * bearing + (1 - t) * startRotation);

                    getCarMarker().setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    @NonNull
    private Boolean hasLocations() {
        return (getNewLocation() != null && getPreviousLocation() != null);
    }

    private double getPositiveValue(double v) {
        if (v < 0) {
            v = -v;
        }
        return v;
    }

    private long getSystemTime() {
        return SystemClock.uptimeMillis();
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public boolean isDistanceToMove(LatLng current, LatLng destination) {// distance in KM
        try {
            Location locationA = new Location("point A");
            locationA.setLatitude(current.latitude);
            locationA.setLongitude(current.longitude);
            Location locationB = new Location("point B");
            locationB.setLatitude(destination.latitude);
            locationB.setLongitude(destination.longitude);
//            Log.d("distance   ", "    " + locationA.distanceTo(locationB) + "    bearing     " + bearingBetweenLocations()+"   "+driverId);
            return locationA.distanceTo(locationB) >= 5;
        } catch (Exception e) {
            return false;
        }
    }
}

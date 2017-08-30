package com.apps.cabpoint.cabigate.map;

import android.content.Context;
import android.util.Log;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.utils.utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Abdul Ghani on 5/15/2017.
 */

public class DrawPolyLine {
    private String duration = null;
    private String distance = null;
    private static Polyline polyline = null;
    private GoogleMap googleMap;
    private LatLng origin;
    private LatLng destination;
    private Context context;
    private float width;
    private static int count = 0;

    public DrawPolyLine(Context context, GoogleMap googleMap, LatLng origin, LatLng destination) {
        this.googleMap = googleMap;
        this.context = context;
        this.origin = origin;
        this.destination = destination;
        this.context = context;
        width = context.getResources().getDimensionPixelOffset(R.dimen._3sdp);
    }

    public void drawPolyLineToMap() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(getDirectionsUrl(origin, destination), new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    // routesArray contains ALL routes
                    JSONArray routesArray = jsonObject.getJSONArray("routes");
                    // Grab the first route
                    JSONObject route = routesArray.getJSONObject(0);
                    // Take all legs from the route
                    JSONArray legs = route.getJSONArray("legs");
                    // Grab first leg
                    JSONObject leg = legs.getJSONObject(0);

                    if (polyline != null) {
                        polyline.remove();
                    }
                    JSONObject overviewPolylines = route.getJSONObject("overview_polyline");
                    String encodedString = overviewPolylines.getString("points");
                    List<LatLng> decodedPath = PolyUtil.decode(encodedString);
                    polyline = googleMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(0xff00b2ff).width(width));

//                    JSONObject durationObject = leg.getJSONObject("duration");
//                    duration = durationObject.getString("text");
//                    durationTxt.setText(durationString(duration));
//
//                    JSONObject distanceObject = leg.getJSONObject("distance");
//                    distance = distanceObject.getString("text");
//                    Log.d("Distance",distance);
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null) {
                    Log.d("Result", errorResponse.toString());
                }
            }
        });
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String parameters = str_origin + "&" + str_dest;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    public String getDistance() {
        return distance;
    }

    public Polyline getPolyLine() {
        return polyline;
    }

    public void removePolyLine() {
        if (polyline != null) {
            polyline.remove();
        }
    }

    public void removePolyLineAtLocation(LatLng myLocation) {
//        updateCameraCarAndLoaction(myLocation,MapActivity.destinationLocation);
        updateCameraAtCar(myLocation);
        List<LatLng> polylinePoints = polyline.getPoints();
        double firstDistance = -1;
        double secondDistance = -1;

        double tolerance = 200;
        if (PolyUtil.isLocationOnPath(myLocation, polylinePoints, true, tolerance)) {
            for (int i = 0; i < polylinePoints.size() - 1; i++) {

                firstDistance = utils.getDistance(myLocation, polylinePoints.get(i));
                secondDistance = utils.getDistance(myLocation, polylinePoints.get(i + 1));
                if (firstDistance < secondDistance) {
                    firstDistance = i;
                    break;
                }
            }
            if (firstDistance != -1 && firstDistance < polylinePoints.size()) {
                polyline.setPoints(polylinePoints.subList((int) firstDistance, polylinePoints.size()));
            } else {
                if (firstDistance == -1) {
                    origin = myLocation;
                    drawPolyLineToMap();
                }
            }
        } else {
            origin = myLocation;
            drawPolyLineToMap();
            Log.d("Draw", "Poly Line drawen");
        }
    }

    private void updateCameraAtCar(LatLng latLng) {
        if (count == 3) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15.1f);
            ((MapActivity) context).gMap.animateCamera(cameraUpdate);
            count = 0;
        } else {
            count++;
        }
    }

    private void updateCameraCarAndLoaction(LatLng car, LatLng location) {
        if (location != null && car != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(car);
            builder.include(location);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, context.getResources().getDimensionPixelOffset(R.dimen._50sdp));
            ((MapActivity) context).gMap.animateCamera(cu);
        }
    }
}

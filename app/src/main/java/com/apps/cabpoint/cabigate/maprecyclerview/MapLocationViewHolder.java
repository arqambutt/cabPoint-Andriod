/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apps.cabpoint.cabigate.maprecyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.dialog.CancelBookingDialog;
import com.apps.cabpoint.cabigate.dialog.DialogListner;
import com.apps.cabpoint.cabigate.models.RideHistory;
import com.apps.cabpoint.cabigate.ui.HistoryDetailActivity;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapLocationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback, View.OnClickListener {
    public MobiTextView title;
    public MobiTextView description;

    protected GoogleMap mGoogleMap;
    protected MapLocation mMapLocation;

    public MapView mapView;
    public Context mContext;
    public ImageView driverImage;
    public MobiTextView rating, driverName, carModel, carNumber, date, fare;
    LinearLayout rideDetails, cancleBooking;
    private LinearLayout bottomPanel;

    public MapLocationViewHolder(Context context, View view) {
        super(view);

        mContext = context;

/*      title = (MobiTextView) view.findViewById(R.id.title);*/
        mapView = (MapView) view.findViewById(R.id.map);
        driverImage = (ImageView) view.findViewById(R.id.driver_image);
        rating = (MobiTextView) view.findViewById(R.id.driver_rating);
        driverName = (MobiTextView) view.findViewById(R.id.driver_name);
        carModel = (MobiTextView) view.findViewById(R.id.car_model);
        carNumber = (MobiTextView) view.findViewById(R.id.car_number);
        date = (MobiTextView) view.findViewById(R.id.history_date);
        fare = (MobiTextView) view.findViewById(R.id.history_fare);
        rideDetails = (LinearLayout) view.findViewById(R.id.ride_params);
        cancleBooking = (LinearLayout) view.findViewById(R.id.cancel_request_history);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getLayoutPosition(); // gets item position
        final RideHistory rideHistory = ((MapListActivity) mContext).rideHistories.get(position);
        if (((MapListActivity) mContext).isPast) {
            Intent intent = new Intent(mContext, HistoryDetailActivity.class);
            intent.putExtra("history", rideHistory);
            ((MapListActivity) mContext).startActivityForResult(intent, 11);
        } else {
            cancelBooking(rideHistory);
        }
    }


    public void setMapLocation(MapLocation mapLocation) {
        mMapLocation = mapLocation;

        // If the map is ready, update its content.
        if (mGoogleMap != null) {
            updateMapContents();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        MapsInitializer.initialize(mContext);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        // If we have map data, update the map content.
        if (mMapLocation != null) {
            updateMapContents();
        }
    }

    protected void updateMapContents() {
        mGoogleMap.clear();
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.addMarker(new MarkerOptions()
                .icon(utils.getShapeXmlIcon(mContext, R.drawable.shape_circle_green))
                .anchor(0.5f, 0.5f).position(mMapLocation.getCurrentLocation()));
        mGoogleMap.addMarker(new MarkerOptions()
                .icon(utils.getShapeXmlIcon(mContext, R.drawable.shape_circle_red))
                .anchor(0.5f, 0.5f).position(mMapLocation.getDestination()));
        mGoogleMap.addPolyline(new PolylineOptions()
                .add(mMapLocation.getCurrentLocation(),
                        mMapLocation.getDestination()).color(0xff00b2ff).width(mContext.getResources().getDimensionPixelOffset(R.dimen._3sdp)));
//        setCamera(mGoogleMap);
        updateCamera(mGoogleMap, mMapLocation.currentLocation, mMapLocation.destination);
    }

    private void setCamera(GoogleMap mGoogleMap) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        float expandBound = 0.060f;
        builder.include(mMapLocation.currentLocation);
        builder.include(mMapLocation.destination);
        LatLngBounds bounds = builder.build();
        LatLng center = bounds.getCenter();
        builder.include(new LatLng(center.latitude - expandBound, center.longitude - expandBound));
        builder.include(new LatLng(center.latitude + expandBound, center.longitude + expandBound));
        bounds = builder.build();

        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void updateCamera(GoogleMap map, LatLng current, LatLng destination) {
        if (destination != null && current != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(current);
            builder.include(destination);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, mContext.getResources().getDimensionPixelOffset(R.dimen._120sdp));
            map.animateCamera(cu);
        }
    }

    private void cancelBooking(RideHistory rideHistory){
        CancelBookingDialog cancelBookingDialog = new CancelBookingDialog(mContext, rideHistory.getJobId(), new DialogListner() {
            @Override
            public void getDialogValue(String value) {
                if (value.equals("Cancel")) {
                    ((MapListActivity) mContext).onConnected();
                }
            }
        });
        cancelBookingDialog.show();
    }
}

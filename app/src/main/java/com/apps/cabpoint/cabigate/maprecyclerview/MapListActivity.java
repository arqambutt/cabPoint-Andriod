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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.RideHistory;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.ui.BaseActivity;
import com.apps.cabpoint.cabigate.utils.ConnectionListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MapListActivity extends BaseActivity implements ConnectionListner {

    protected MapLocationAdapter mListAdapter;
    protected RecyclerView mRecyclerView;
    protected RelativeLayout noHistoryView;
    public ArrayList<RideHistory> rideHistories = null;
    private Context context = MapListActivity.this;
    MobiTextView trip;
    public static final int RIDE_AGAIN = 11;
    private ImageView backBtn;
    private boolean enableWifiListner, enableMobileDataListner = false;
    private TextView past, future;
    public boolean isPast = true;
    Handler handler;
    Runnable runnable;

    private void updateUI(final MobiTextView view, final String label, String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                noHistoryView.setVisibility(View.VISIBLE);
            }
        }, 500);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);
        initViews();
        initClickLisners();
        setConnectionListner(this);
    }

    void initViews() {
        trip = (MobiTextView) findViewById(R.id.select_trip);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        backBtn = (ImageView) findViewById(R.id.recycler_nav_back_btn);
        noHistoryView = (RelativeLayout) findViewById(R.id.no_history_view);
        past = (TextView) findViewById(R.id.past_tv);
        future = (TextView) findViewById(R.id.future_tv);
        highlightView(isPast);
        int rows = getResources().getInteger(R.integer.map_grid_cols);
        GridLayoutManager layoutManager = new GridLayoutManager(this, rows, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        handler = new Handler();
        if (utils.getActiveInternet(context)) {
            onConnected();
        } else {
            onDisconnected();
        }
    }

    void initClickLisners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPast = true;
                onConnected();
            }
        });
        future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPast = false;
                onConnected();
            }
        });
    }

    private void highlightView(boolean isPast) {
        if (isPast) {
            past.setTextColor(context.getResources().getColor(R.color.white));
            past.setBackground(context.getResources().getDrawable(R.drawable.square_btn_bg));
            future.setBackground(null);
            future.setTextColor(context.getResources().getColor(R.color.cab_text));
        } else {
            future.setTextColor(context.getResources().getColor(R.color.white));
            future.setBackground(context.getResources().getDrawable(R.drawable.square_btn_bg));
            past.setBackground(null);
            past.setTextColor(context.getResources().getColor(R.color.cab_text));
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mListAdapter != null) {
            for (MapView m : mListAdapter.getMapViews()) {
                m.onLowMemory();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mListAdapter != null) {
            for (MapView m : mListAdapter.getMapViews()) {
                m.onDestroy();
            }
        }
        super.onDestroy();
        removeConnectionListner();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void checkAPI() {
        String type;
        highlightView(isPast);
        if (isPast) {
            type = PaxApiCall.historyOld;
        } else {
            type = PaxApiCall.historyFuture;
        }
        if(mListAdapter!=null){
            mListAdapter.clear();
        }
        ApiCalls.rideHistory(StoragePreference.getUserId(context), StoragePreference.getTOKEN(context), type, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                parseRideHistoryResponse(jsonObject);
            }
        });
    }

    void parseRideHistoryResponse(JSONObject jsonObject) {
        try {
            rideHistories = new ArrayList<>();
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                JSONArray ridesHistoryArray = response.getJSONArray(PaxApiCall.list);
                for (int i = 0; i < ridesHistoryArray.length(); i++) {
                    JSONObject ride = (JSONObject) ridesHistoryArray.get(i);
                    RideHistory rideHistory = new RideHistory();
                    rideHistory.setJobId(ride.getString(PaxApiCall.job_Id));
                    rideHistory.setPickUpLocation(ride.getString(PaxApiCall.pickUpLocation));
                    rideHistory.setDropLocation(ride.getString(PaxApiCall.dropLocation));
                    rideHistory.setTotalFare(ride.getString(PaxApiCall.totalFare));
                    rideHistory.setPassengerCount(ride.getString(PaxApiCall.passengers));
                    rideHistory.setPickUpDate(ride.getString(PaxApiCall.pickUpDateTime));
                    rideHistory.setDistance(ride.getString(PaxApiCall.distance));
                    rideHistory.setStatus(ride.getString(PaxApiCall.status));
                    rideHistory.setDuration(ride.getString(PaxApiCall.duration));
                    rideHistory.setPaymentMethod(ride.getString(PaxApiCall.paymentMethod));
                    rideHistory.setPick_lat(ride.getString(PaxApiCall.pickUpLatitude));
                    rideHistory.setPick_lng(ride.getString(PaxApiCall.pickUpLongitude));
                    rideHistory.setDrop_lat(ride.getString(PaxApiCall.dropLatitude));
                    rideHistory.setDrop_lng(ride.getString(PaxApiCall.dropLongitude));
                    rideHistory.setDriverImage(ride.getString(PaxApiCall.driverImage));
                    rideHistory.setUserName(ride.getString(PaxApiCall.userName));
                    rideHistory.setDriverName(ride.getString(PaxApiCall.driverName));
                    rideHistory.setVehicleImage(ride.getString(PaxApiCall.vehicleImage));
                    rideHistory.setTaxiModel(ride.getString(PaxApiCall.taxiModel));
                    rideHistory.setDriverRating(ride.getString(PaxApiCall.driverRating));
                    rideHistory.setDriverPhone(ride.getString(PaxApiCall.driverPhone));
                    rideHistory.setCallSign(ride.getString(PaxApiCall.callSign));
                    rideHistory.setVehiclePlateNumber(ride.getString(PaxApiCall.plateNumber));
                    rideHistories.add(rideHistory);
                }
                if (rideHistories.size() > 0) {
                    getLocations(rideHistories);
                } else {
                    updateUI(trip, "No Trips Available", "Oops!");
                }

            } else {
                updateUI(trip, "No Trips Available", "Oops!");
            }
        } catch (Exception e) {
            updateUI(trip, "No Trips Available", "Oops!");
        }
    }

    void getLocations(ArrayList<RideHistory> rideHistories) {
        ArrayList<MapLocation> mapLocations = new ArrayList<>();
        MapLocation mapLocation;
        mListAdapter = new MapLocationAdapter();
        for (RideHistory rideHistory : rideHistories) {
            mapLocation = new MapLocation();
            LatLng current = new LatLng(Double.parseDouble(rideHistory.getPick_lat()), Double.parseDouble(rideHistory.getPick_lng()));
            LatLng destination = new LatLng(Double.parseDouble(rideHistory.getDrop_lat()), Double.parseDouble(rideHistory.getDrop_lng()));
            mapLocation.setCurrentLocation(current);
            mapLocation.setDestination(destination);
            mapLocation.setFare(rideHistory.getTotalFare());
            mapLocation.setDate(rideHistory.getPickUpDate());
            mapLocation.setDriverImage(rideHistory.getDriverImage());
            mapLocation.setRating(rideHistory.getDriverRating());
            mapLocation.setName(rideHistory.getDriverName());
            mapLocation.setCarModel(rideHistory.getTaxiModel());
            mapLocation.setCarNumber(rideHistory.getVehiclePlateNumber());
            mapLocations.add(mapLocation);
            mListAdapter.setMapLocations(mapLocations);
            mRecyclerView.setAdapter(mListAdapter);
        }
    }

    String getDateInSpecficFormate(String dateString) {
        String fromatedDate = dateString;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date startDate;
        try {
            startDate = df.parse(dateString);
            fromatedDate = new SimpleDateFormat("EEEE 'at' hh:mm a", Locale.US).format(startDate);
            return fromatedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return fromatedDate;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RIDE_AGAIN)
            if (resultCode == Activity.RESULT_OK) {
                RideHistory rideHistory = (RideHistory) data.getSerializableExtra("history");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("history", rideHistory);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
    }

    void sendAPIRequest() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (utils.getActiveInternet(context)) {
                    checkAPI();
                } else {
                    sendAPIRequest();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void onConnected() {
        noHistoryView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if(utils.getActiveInternet(context)){
            checkAPI();
        }else {
            sendAPIRequest();
        }
    }


    public void onDisconnected() {
        if (mListAdapter != null) {
            if (rideHistories != null) {
                rideHistories.clear();
                mListAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mListAdapter);
            }
        }
        mRecyclerView.setVisibility(View.GONE);
        noHistoryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWifiConnected() {
        if (enableWifiListner) {
            onConnected();
        } else {
            enableWifiListner = true;
        }
    }

    @Override
    public void onWifiDisconnected() {
        if (enableWifiListner) {
            onDisconnected();
        } else {
            enableWifiListner = true;
        }
    }

    @Override
    public void onMobileDataConnected() {
        if (enableMobileDataListner) {
            onConnected();
        } else {
            enableMobileDataListner = true;
        }
    }

    @Override
    public void onMobileDataDisconnected() {
        if (enableMobileDataListner) {
            onDisconnected();
        } else {
            enableMobileDataListner = true;
        }
    }
}

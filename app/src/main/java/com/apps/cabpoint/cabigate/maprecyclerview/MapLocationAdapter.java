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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.utils.CircleTransform;
import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

public class MapLocationAdapter extends RecyclerView.Adapter<MapLocationViewHolder> {

    protected HashSet<MapView> mMapViews = new HashSet<>();
    protected ArrayList<MapLocation> mMapLocations;


    public void setMapLocations(ArrayList<MapLocation> mapLocations) {
        mMapLocations = mapLocations;
    }

    @Override
    public MapLocationViewHolder onCreateViewHolder(final ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_recyclerview_list_item, viewGroup, false);
        MapLocationViewHolder viewHolder = new MapLocationViewHolder(viewGroup.getContext(), view);
        mMapViews.add(viewHolder.mapView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MapLocationViewHolder viewHolder, final int position) {
        final MapLocation mapLocation = mMapLocations.get(position);

        Picasso.with(viewHolder.mContext).load(mapLocation.getDriverImage()).transform(new CircleTransform()).into(viewHolder.driverImage);
        viewHolder.rating.setText(mapLocation.getRating());
        viewHolder.driverName.setText(mapLocation.getName());
        viewHolder.carModel.setText(mapLocation.getCarModel());
        viewHolder.carNumber.setText(mapLocation.getCarNumber());
        viewHolder.date.setText(mapLocation.getDate());
        viewHolder.fare.setText(mapLocation.getFare());
        viewHolder.setMapLocation(mapLocation);
        if(((MapListActivity)viewHolder.mContext).isPast){
            viewHolder.rideDetails.setVisibility(View.VISIBLE);
            viewHolder.cancleBooking.setVisibility(View.GONE);
        }else {
            viewHolder.rideDetails.setVisibility(View.GONE);
            viewHolder.cancleBooking.setVisibility(View.VISIBLE);
        }
    }

    public void clear() {
        int size = mMapLocations.size();
        mMapLocations.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return mMapLocations == null ? 0 : mMapLocations.size();
    }

    public HashSet<MapView> getMapViews() {
        return mMapViews;
    }
}

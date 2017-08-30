package com.apps.cabpoint.cabigate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.models.LocationHistory;
import com.apps.cabpoint.cabigate.ui.SettingActivity;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import java.util.List;

/**
 * Created by Abdul Ghani on 7/10/2017.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<LocationHistory> locations;
    private Context context;

    public LocationAdapter(List<LocationHistory> locations) {
        this.locations = locations;
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {

        LinearLayout listItem;
        ImageView icon;
        MobiTextView location, locationDescription;

        public LocationViewHolder(View itemView) {
            super(itemView);
            listItem = (LinearLayout) itemView.findViewById(R.id.location_layout);
            icon = (ImageView) itemView.findViewById(R.id.location_icon);
            location = (MobiTextView) itemView.findViewById(R.id.top_location);
            locationDescription = (MobiTextView) itemView.findViewById(R.id.location_description);
        }
    }

    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_view, parent, false);
        context = v.getContext();
        return new LocationAdapter.LocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LocationAdapter.LocationViewHolder holder, final int position) {

        holder.icon.setImageResource(locations.get(position).getResource());
        holder.location.setText(locations.get(position).getLocation());
        if (locations.get(position).getLocationDescription() != null) {
            holder.locationDescription.setVisibility(View.VISIBLE);
            holder.locationDescription.setText(locations.get(position).getLocationDescription());
        } else {
            holder.locationDescription.setVisibility(View.GONE);
        }
        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locations.get(position).getResource() == R.drawable.ic_add_circle_outline_black_36dp) {
                    context.startActivity(new Intent(context, SettingActivity.class));
                    ((MapActivity) context).placeLayout.setVisibility(View.INVISIBLE);
                    ((MapActivity) context).placeLayoutVisibility = false;
                } else {
                    ((MapActivity) context).setLocation(locations.get(position).getLocationDescription(), locations.get(position).getLocationLatLng());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}

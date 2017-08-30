package com.apps.cabpoint.cabigate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.NeedHelp;
import com.apps.cabpoint.cabigate.models.RideHistory;
import com.apps.cabpoint.cabigate.ui.HelpDescriptionActivity;
import com.apps.cabpoint.cabigate.ui.NeedHelpActivity;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import java.util.ArrayList;

/**
 * Created by Abdul Ghani on 7/17/2017.
 */

public class NeedHelpAdapter extends RecyclerView.Adapter<NeedHelpAdapter.NeedHelpViewHolder> {

    private ArrayList<NeedHelp> needHelps;

    public static class NeedHelpViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MobiTextView heading;

        public NeedHelpViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_need_help);
            heading = (MobiTextView) itemView.findViewById(R.id.heading_need_help);
        }
    }

    public NeedHelpAdapter(ArrayList<NeedHelp> needHelps) {
        this.needHelps = needHelps;
    }

    @Override
    public NeedHelpAdapter.NeedHelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.need_help_list_view, parent, false);
        return new NeedHelpAdapter.NeedHelpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NeedHelpAdapter.NeedHelpViewHolder holder, final int position) {
        final Context context = holder.cardView.getContext();
        holder.heading.setText(needHelps.get(position).getHeading());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HelpDescriptionActivity.class);
                NeedHelp needHelp = ((NeedHelpActivity) context).needHelps.get(position);
                RideHistory rideHistory = ((NeedHelpActivity) context).rideHistory;
                intent.putExtra("help", needHelp);
                intent.putExtra("history", rideHistory);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return needHelps.size();
    }
}

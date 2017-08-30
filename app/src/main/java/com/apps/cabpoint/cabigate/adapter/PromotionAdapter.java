package com.apps.cabpoint.cabigate.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.Promotion;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import java.util.ArrayList;

/**
 * Created by Abdul Ghani on 7/17/2017.
 */

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder>{

    private ArrayList<Promotion> promotions;

    public static class PromotionViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MobiTextView code,description,expiry;

        public PromotionViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.promotion_card);
            code = (MobiTextView) itemView.findViewById(R.id.txt_code);
            description = (MobiTextView) itemView.findViewById(R.id.txt_description);
            expiry = (MobiTextView) itemView.findViewById(R.id.txt_expiry);
        }
    }
    public PromotionAdapter(ArrayList<Promotion> promotions){
        this.promotions = promotions;
    }

    @Override
    public PromotionAdapter.PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.promtion_cardview, parent, false);
        return new PromotionAdapter.PromotionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PromotionAdapter.PromotionViewHolder holder, final int position) {
        final Context context = holder.cardView.getContext();
        holder.code.setText(promotions.get(position).getCode());
        holder.description.setText(promotions.get(position).getDescription());
        holder.expiry.setText(context.getString(R.string.expiry_string)+" "+promotions.get(position).getExpiry());
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }
}

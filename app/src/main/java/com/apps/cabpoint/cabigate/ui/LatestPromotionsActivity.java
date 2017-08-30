package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.adapter.PromotionAdapter;
import com.apps.cabpoint.cabigate.models.Promotion;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.ConnectionListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abdul Ghani on 8/12/2017.
 */

public class LatestPromotionsActivity extends BaseActivity implements ConnectionListner{

    private RecyclerView recyclerView;
    private ImageView back;
    private Context context = LatestPromotionsActivity.this;
    private ArrayList<Promotion> promotions = null;
    private RelativeLayout noHistoryView;
    private boolean enableWifiListner, enableMobileDataListner = false;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_promotions_layout);
        initViews();
        initClickLisners();
        if (utils.getActiveInternet(context)) {
            requestPromotionsAPI();
        } else {
            onDisconnected();
        }
        setConnectionListner(this);
    }

    @Override
    protected void initViews() {
        back = (ImageView) findViewById(R.id.back_btn_promotion);
        recyclerView = (RecyclerView) findViewById(R.id.promotions_recycler);
        noHistoryView = (RelativeLayout) findViewById(R.id.no_history_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        handler = new Handler();
    }

    @Override
    void initClickLisners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void requestPromotionsAPI() {
        ApiCalls.getLatestPromotions(StoragePreference.getUserId(context), new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                promotions = PaxApiCall.parsePromotionResponse(jsonObject);
                if (promotions != null) {
                    PromotionAdapter promotionAdapter = new PromotionAdapter(promotions);
                    recyclerView.setAdapter(promotionAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noHistoryView.setVisibility(View.VISIBLE);
                    TextView first = (TextView) noHistoryView.findViewById(R.id.txt_one);
                    TextView second = (TextView) noHistoryView.findViewById(R.id.txt_two);
                    first.setText("No Promotion Yet");
                    second.setVisibility(View.GONE);
                }
            }
        });
    }

    void sendAPIRequest() {
        if(promotions==null){
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (utils.getActiveInternet(context)) {
                        requestPromotionsAPI();
                    } else {
                        sendAPIRequest();
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeConnectionListner();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void onConnected() {
        recyclerView.setVisibility(View.VISIBLE);
        noHistoryView.setVisibility(View.GONE);
        sendAPIRequest();
    }


    public void onDisconnected() {
        recyclerView.setVisibility(View.GONE);
        noHistoryView.setVisibility(View.VISIBLE);
        TextView first = (TextView) noHistoryView.findViewById(R.id.txt_one);
        TextView second = (TextView) noHistoryView.findViewById(R.id.txt_two);
        first.setText(getString(R.string.no_internet));
        second.setVisibility(View.GONE);
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

package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.adapter.NeedHelpAdapter;
import com.apps.cabpoint.cabigate.models.NeedHelp;
import com.apps.cabpoint.cabigate.models.RideHistory;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abdul Ghani on 7/17/2017.
 */

public class NeedHelpActivity extends BaseActivity {

    private ImageView back;
    private RecyclerView recyclerView;
    private Context context = NeedHelpActivity.this;
    public ArrayList<NeedHelp> needHelps;
    public RideHistory rideHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_need_help);
        initViews();
        initClickLisners();
        getIntentData();
        getDataFromAPI();
    }

    @Override
    protected void initViews() {
        back = (ImageView) findViewById(R.id.back_btn_need_help);
        recyclerView = (RecyclerView) findViewById(R.id.need_help_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
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

    void getDataFromAPI() {
        ApiCalls.needHelp(StoragePreference.getUserId(context), new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                needHelps = PaxApiCall.parseNeedHelpResponse(jsonObject);
                if (needHelps != null) {
                    NeedHelpAdapter needHelpAdapter = new NeedHelpAdapter(needHelps);
                    recyclerView.setAdapter(needHelpAdapter);
                } else {
                    needHelps = null;
                    utils.showInfoDialog(context, "Failed", false);
                }
            }
        });
    }

    void getIntentData() {
        Intent intent = getIntent();
        rideHistory = (RideHistory) intent.getSerializableExtra("history");
    }
}

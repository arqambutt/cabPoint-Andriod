package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.FreeRide;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.ConnectionListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 7/17/2017.
 */

public class FreeRideActivity extends BaseActivity implements ConnectionListner{

    MobiTextView sendInvitation, howItWork, promocode,screenMessage,balance,expiryDate;
    ImageView back;
    FreeRide freeRide;
    LinearLayout frontView;
    Context context = FreeRideActivity.this;
    private boolean enableWifiListner,enableMobileDataListner = false;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_free_ride);
        initViews();
        initClickLisners();
        if (utils.getActiveInternet(context)) {
            requestFreeRide();
        } else {
            onDisconnected();
        }
        setConnectionListner(this);
    }

    @Override
    void initViews() {
        sendInvitation = (MobiTextView) findViewById(R.id.send_invitation_free_ride);
        howItWork = (MobiTextView) findViewById(R.id.how_it_work);
        promocode = (MobiTextView) findViewById(R.id.promo_code_edit_txt);
        back = (ImageView) findViewById(R.id.back_free_ride);
        frontView = (LinearLayout) findViewById(R.id.front_view);
        screenMessage = (MobiTextView) findViewById(R.id.screen_message);
        balance = (MobiTextView) findViewById(R.id.balance);
        expiryDate = (MobiTextView) findViewById(R.id.expire_date);
        handler = new Handler();
    }

    @Override
    void initClickLisners() {
        sendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (freeRide != null) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, freeRide.getInvitationMessage() + "" + freeRide.getInvitationLink());
                    startActivity(Intent.createChooser(sharingIntent, "Send via"));
                } else {
                    utils.showInfoDialog(context, "Try Again", false);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        howItWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(frontView);
            }
        });
    }

    void requestFreeRide() {
        ApiCalls.getFreeRide(StoragePreference.getUserId(getApplicationContext()), new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                freeRide = PaxApiCall.parseFreeRideResponse(jsonObject);
                promocode.setText(freeRide.getPromoCode());
                screenMessage.setText(freeRide.getScreenMessage());
                balance.setText(getString(R.string.balance)+""+freeRide.getFreeRideValue()+" "+freeRide.getCurrency());
                expiryDate.setText(getString(R.string.expiry_string)+" "+freeRide.getExpiry());
                sendInvitation.setEnabled(true);
            }
        });
    }

    void toggleView(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    void sendAPIRequest(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if(utils.getActiveInternet(context)){
                    requestFreeRide();
                }else {
                    sendAPIRequest();
                }
            }
        };
        handler.postDelayed(runnable,1000);
    }

    @Override
    public void onBackPressed() {
        if (frontView.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            frontView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeConnectionListner();
        if(runnable!=null){
            handler.removeCallbacks(runnable);
        }
    }

    public void onConnected() {
        sendInvitation.setText(getString(R.string.send_invitation));
        sendInvitation.setEnabled(true);
        sendAPIRequest();
    }


    public void onDisconnected() {
        sendInvitation.setText(getString(R.string.no_internet));
        sendInvitation.setEnabled(false);
    }

    @Override
    public void onWifiConnected() {
        if(enableWifiListner){
            onConnected();
        }else {
            enableWifiListner = true;
        }
    }

    @Override
    public void onWifiDisconnected() {
        if(enableWifiListner){
            onDisconnected();
        }else {
            enableWifiListner = true;
        }
    }

    @Override
    public void onMobileDataConnected() {
        if(enableMobileDataListner){
            onConnected();
        }else {
            enableMobileDataListner = true;
        }
    }

    @Override
    public void onMobileDataDisconnected() {
        if(enableMobileDataListner){
            onDisconnected();
        }else {
            enableMobileDataListner = true;
        }
    }
}

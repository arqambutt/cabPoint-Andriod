package com.apps.cabpoint.cabigate.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.utils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 8/4/2017.
 */

public class ChangeCustomPlaceActivity extends BaseActivity {

    private ImageView back;
    private TextView home, work;
    private Button updateBtn;
    private Passenger passenger = null;
    private Context context = this;
    private static String homeAddress, workAddress = null;
    private static final int HOME = 1;
    private static final int WORK = 2;
    private LatLng homeLatLng, workLatLng = null;
    private ImageView homeCancel,workCancel;
    private boolean isNo = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_custom_places);
        initViews();
        initClickLisners();
        getIntentData();
    }

    @Override
    void initViews() {
        back = (ImageView) findViewById(R.id.back);
        home = (TextView) findViewById(R.id.home_custom_place);
        work = (TextView) findViewById(R.id.work_custom_place);
        updateBtn = (Button) findViewById(R.id.update_custom_place);
        homeCancel = (ImageView) findViewById(R.id.home_remove_btn);
        workCancel = (ImageView) findViewById(R.id.work_remove_btn);
    }

    @Override
    void initClickLisners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceAPIActivity.class);
                intent.putExtra("add", HOME);
                startActivityForResult(intent, HOME);
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceAPIActivity.class);
                intent.putExtra("add", WORK);
                startActivityForResult(intent, HOME);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonAction();
            }
        });
        homeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.setText(" ");
            }
        });
        workCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work.setText(" ");
            }
        });
    }

    void getIntentData() {
        Intent intent = getIntent();
        passenger = (Passenger) intent.getSerializableExtra(PaxApiCall.passengers);
        setData();
    }

    void setData() {
        if (passenger != null) {
            homeAddress = (passenger.getHomeAddress().equals("")) ? getString(R.string.add_home) : passenger.getHomeAddress();
            workAddress = (passenger.getWorkAddress().equals("")) ? getString(R.string.add_work) : passenger.getWorkAddress();
            home.setText(homeAddress);
            work.setText(workAddress);
        }
    }

    boolean isDataChanged() {
        if (homeAddress.equals(home.getText().toString()) && workAddress.equals(work.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    void updateButtonAction() {
            if (isDataChanged()) {
                if (homeLatLng != null) {
                    passenger.setHomeAddress(home.getText().toString());
                    if(passenger.getHomeAddress().equals(" ")){
                        passenger.setHomeLongitude("0.0000");
                        passenger.setHomeLatitude("0.0000");
                    }else {
                        passenger.setHomeLongitude(homeLatLng.longitude + "");
                        passenger.setHomeLatitude(homeLatLng.latitude + "");
                    }
                }
                if (workLatLng != null) {
                    passenger.setWorkAddress(work.getText().toString());
                    if(passenger.getWorkAddress().equals(" ")){
                        passenger.setWorkLatitude("0.0000");
                        passenger.setWorkLongitude("0.0000");
                    }else {
                        passenger.setWorkLatitude(workLatLng.latitude + "");
                        passenger.setWorkLongitude(workLatLng.longitude + "");
                    }
                }
                if(utils.getActiveInternet(context)) {
                    ApiCalls.updateUserData(passenger, paxApiCallListner);
                }else {
                    utils.updateUI(updateBtn, getString(R.string.no_internet));
                }
            } else {
                utils.updateUI(updateBtn, "Data not changed");
            }
    }

    private PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
        @Override
        public void onApiRequestResult(JSONObject jsonObject) {
            try {
                int status = jsonObject.getInt(PaxApiCall.status);
                if (status == 1) {
                    updateUI(updateBtn, "Successfully Updated");
                    homeAddress = passenger.getHomeAddress();
                    workAddress = passenger.getWorkAddress();
                } else {
                    updateUI(updateBtn, "Update Failed");
                }
            } catch (Exception e) {
                updateUI(updateBtn, "Update Failed");
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (isDataChanged() && isNo) {
            warningDialog();
        } else {
            Intent intent = new Intent();
            intent.putExtra(PaxApiCall.passengers, passenger);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
    }

    void warningDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Do you want to save data?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                updateButtonAction();
            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        isNo = false;
                        onBackPressed();
                    }
                });
        alert.show();
    }

    private void updateUI(final Button button, String label) {
        final String previousLabel = button.getText().toString();
        button.setText(label);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setText(previousLabel);
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HOME) {
            if (resultCode == Activity.RESULT_OK) {
                String location = data.getStringExtra("location");
                LatLng latLng = new LatLng(data.getDoubleExtra("lat", 0.0), data.getDoubleExtra("lng", 0.0));
                int add = data.getIntExtra("add", 0);
                if (add == 1) {
                    if (location != null && !location.equals("")) {
                        home.setText(location);
                        homeLatLng = latLng;
                    }
                } else {
                    if (location != null && !location.equals("")) {
                        work.setText(location);
                        workLatLng = latLng;
                    }
                }
            }
        }
    }
}

package com.apps.cabpoint.cabigate.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 8/4/2017.
 */

public class ChangeNameActivity extends BaseActivity {

    private ImageView back;
    private EditText firstName, lastName;
    private Button updateBtn;
    private Passenger passenger = null;
    private Context context = this;
    private boolean isNo = true;
    private String fname,lname= null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_change_name);
        initViews();
        initClickLisners();
        getIntentData();
    }

    @Override
    void initViews() {
        back = (ImageView) findViewById(R.id.back);
        firstName = (EditText) findViewById(R.id.fname_change);
        lastName = (EditText) findViewById(R.id.lname_change);
        updateBtn = (Button) findViewById(R.id.update_change);
    }

    @Override
    void initClickLisners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonAction();
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
            firstName.setText(passenger.getFirstName());
            lastName.setText(passenger.getLastName());
            fname = passenger.getFirstName();
            lname = passenger.getLastName();
        }
    }

    void updateButtonAction() {
        if (isDataChanged()) {
                passenger.setFirstName(firstName.getText().toString());
                passenger.setLastName(lastName.getText().toString());
            if(utils.getActiveInternet(context)){
                ApiCalls.updateUserData(passenger, paxApiCallListner);
            }else {
                utils.updateUI(updateBtn,getString(R.string.no_internet));
            }
        } else {
            utils.updateUI(updateBtn, "Data not changed");
        }
    }

    PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
        @Override
        public void onApiRequestResult(JSONObject jsonObject) {
            try {
                int status = jsonObject.getInt(PaxApiCall.status);
                if (status == 1) {
                    updateUI(updateBtn, "Successfully Updated");
                    fname = passenger.getFirstName();
                    lname = passenger.getLastName();
                } else {
                    updateUI(updateBtn, "Update Failed");
                }
            } catch (Exception e) {
                updateUI(updateBtn, "Update Failed");
            }
        }
    };

    boolean isDataChanged() {
        if (fname.equals(firstName.getText().toString()) && lname.equals(lastName.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

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
}

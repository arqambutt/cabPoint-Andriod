package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 8/4/2017.
 */

public class ChangePhoneActivity extends BaseActivity {

    private IntlPhoneInput phoneInputView;
    private Button updateBtn;
    private String passengerId;
    private String phoneNumber;
    private ImageView back;
    private static final int ACTIVITY_FOR_RESULT = 30;
    private Passenger passenger = null;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_change_phone);
        initViews();
        initClickLisners();
        getIntentData();
    }

    @Override
    void initViews() {
        phoneInputView = (IntlPhoneInput) findViewById(R.id.mobile_number_dialog);
        phoneInputView.setDefault();
        updateBtn = (Button) findViewById(R.id.update_btn);
        back = (ImageView) findViewById(R.id.back_btn);
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
                if (phoneInputView.isValid()) {
                    updatePhone();
                } else {
                    updateUI(updateBtn, "Invalid Number");
                }
            }
        });
    }

    void getIntentData() {
        Intent intent = getIntent();
        passenger = (Passenger) intent.getSerializableExtra(PaxApiCall.passengers);
        if(passenger!=null){
            phoneNumber = passenger.getPhone();
            passengerId = passenger.getUserId();
        }
    }

    private void updatePhone() {
        phoneNumber = phoneInputView.getNumber();
        ApiCalls.updatePhone(passengerId, phoneNumber, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                parseAPIResponse(jsonObject);
            }
        });
    }

    private void parseAPIResponse(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject userData = jsonObject.getJSONObject(PaxApiCall.response);
                String keyMatch = userData.getString(PaxApiCall.keyMatch);
                String opt = userData.getString(PaxApiCall.oPT);
                String token = userData.getString(PaxApiCall.token);
                String firstName = phoneNumber;
                String password = StoragePreference.notAvailable;

                Intent intentToVerifyPhone = new Intent(context, VerifyPhoneActivity.class);
                intentToVerifyPhone.putExtra(PaxApiCall.userId, passengerId);
                intentToVerifyPhone.putExtra(PaxApiCall.keyMatch, keyMatch);
                intentToVerifyPhone.putExtra(PaxApiCall.oPT, opt);
                intentToVerifyPhone.putExtra(PaxApiCall.token, token);
                intentToVerifyPhone.putExtra(PaxApiCall.firstName, firstName);
                intentToVerifyPhone.putExtra(PaxApiCall.password, password);
                startActivityForResult(intentToVerifyPhone, ACTIVITY_FOR_RESULT);
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                updateUI(updateBtn, msg);
            }
        } catch (Exception e) {
        }
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
        if (requestCode == ACTIVITY_FOR_RESULT) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                passenger.setPhone(phoneNumber);
                intent.putExtra(PaxApiCall.passengers, passenger);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}

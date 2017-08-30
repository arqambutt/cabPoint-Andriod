package com.apps.cabpoint.cabigate.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyPhoneActivity extends BaseActivity {

    EditText verifyNumber;
    Button verifyButton;
    String userId, keyMatch, token, opt, firstName, password,companyId;
    Context context = VerifyPhoneActivity.this;
    String TAG = "VerifyPhone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        registerReceiver(phoneReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
        getIntentData();
        initViews();
        initClickLisners();
    }

    @Override
    protected void initViews() {
        verifyButton = (Button) findViewById(R.id.verifyBtn);
        verifyNumber = (EditText) findViewById(R.id.verifyNumber);
        verifyButton.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        verifyNumber.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
    }

    @Override
    void initClickLisners() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyMobileNumber();
            }
        });
    }

    void getIntentData() {
        Intent intent = getIntent();
        userId = intent.getStringExtra(PaxApiCall.userId);
        keyMatch = intent.getStringExtra(PaxApiCall.keyMatch);
        opt = intent.getStringExtra(PaxApiCall.oPT);
        token = intent.getStringExtra(PaxApiCall.token);
        firstName = intent.getStringExtra(PaxApiCall.firstName);
        password = intent.getStringExtra(PaxApiCall.password);
        companyId = intent.getStringExtra(PaxApiCall.companyId);
    }

    void verifyMobileNumber() {
        if (utils.validInput(verifyNumber) && verifyNumber.getText().toString().length() == 5) {
            String newOPT = opt + verifyNumber.getText().toString();
            if (password.equals(StoragePreference.notAvailable)) {
                ApiCalls.verifyUpdatePhone(userId, firstName, keyMatch, newOPT, paxApiCallListner);
            } else {
                PaxApiCall.requestAPI(PaxApiCall.VERIFY_PHONE, PaxApiCall.getVerifyPhoneParameters(userId, keyMatch, newOPT, token), paxApiCallListner);
            }
        }
    }

    PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
        @Override
        public void onApiRequestResult(JSONObject jsonObject) {
            onVerifyResponse(jsonObject);
        }
    };

    void onVerifyResponse(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {

                JSONObject responseObject = jsonObject.getJSONObject(PaxApiCall.response);
                String statusString = responseObject.getString(PaxApiCall.status);
                if (!password.equals(StoragePreference.notAvailable)) {
                    String token = responseObject.getString(PaxApiCall.token);
                    String userid = responseObject.getString(PaxApiCall.userId);
                    StoragePreference.writeUserData(context, userid, token, firstName, password,companyId);
                    if (statusString.equals(PaxApiCall.verified)) {
                        Intent mapIntent = new Intent(context, MapActivity.class);
                        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mapIntent);
                        finish();
                    } else {
                        utils.showInfoDialog(context, "Verification Failed\nTry Again", false);
                    }
                } else {
                    if (statusString.equals(PaxApiCall.verified)) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

            } else {
                utils.showInfoDialog(context, "Verification Failed\nTry Again", false);
                verifyNumber.setText("");
                Log.d(TAG, jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver phoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Log.e(TAG, "Inside EXTRA_STATE_RINGING");
                    String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    Log.e(TAG, "incoming number : " + number);
                    getNumber(number);
                }
            }
        }
    };

    void getNumber(String number) {
        int length = number.length();
        verifyNumber.setText(number.substring(length - 5, length));
        verifyButton.performClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        unregisterReceiver(phoneReceiver);
    }
}

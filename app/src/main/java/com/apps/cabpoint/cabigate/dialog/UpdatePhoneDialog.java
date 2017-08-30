package com.apps.cabpoint.cabigate.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.ui.VerifyPhoneActivity;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 7/16/2017.
 */

public class UpdatePhoneDialog extends Dialog {

    private IntlPhoneInput phoneInputView;
    private Button updateBtn;
    private String passengerId;
    private String phoneNumber;
    private ImageView back;
    private Activity context;

    public UpdatePhoneDialog(Activity context, String passengerId) {
        super(context);
        this.context = context;
        this.passengerId = passengerId;
        setContentView(R.layout.cab_change_phone);
        initViews();
    }

    private void initViews() {
        phoneInputView = (IntlPhoneInput) findViewById(R.id.mobile_number_dialog);
        phoneInputView.setDefault();
        updateBtn = (Button) findViewById(R.id.update_btn);
        back = (ImageView) findViewById(R.id.back_btn);
        updateBtn.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneInputView.isValid()) {
                    updatePhone();
                } else {
                    utils.showInfoDialog(context, "No Rides", false);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
            Log.d("verify phone", jsonObject.toString());
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
                context.startActivity(intentToVerifyPhone);
                dismiss();
                context.finish();
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                utils.showInfoDialog(context, msg, false);
            }
        } catch (Exception e) {
        }
    }
}

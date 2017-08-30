package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 8/7/2017.
 */

public class AddPromoActivity extends BaseActivity {

    private ImageView back;
    private EditText promoCode;
    private Button verifyPromoCode;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_promo_code_layout);
        initViews();
        initClickLisners();
    }

    @Override
    void initViews() {
        back = (ImageView) findViewById(R.id.back);
        promoCode = (EditText) findViewById(R.id.enter_promo_code);
        verifyPromoCode = (Button) findViewById(R.id.verify_promo);
    }

    @Override
    void initClickLisners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        verifyPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.validInput(promoCode)) {
                    ApiCalls.validatePromoCode(StoragePreference.getUserId(context), promoCode.getText().toString(), new PaxApiCallListner() {
                        @Override
                        public void onApiRequestResult(JSONObject jsonObject) {
                            parseValidateApiReponse(jsonObject);
                        }
                    });
                } else {
                    updateUI(verifyPromoCode, promoCode.getHint().toString());
                }
            }
        });
    }

    public static void updateUI(final Button button, String label) {
        final String previousLabel = button.getText().toString();
        button.setText(label);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setText(previousLabel);
            }
        }, 2500);
    }

    private void parseValidateApiReponse(JSONObject jsonObject){
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                String message = response.getString(PaxApiCall.message);
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            } else {
                Toast.makeText(context,"Invalid promo",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context,"Invalid promo",Toast.LENGTH_SHORT).show();
        }
    }
}

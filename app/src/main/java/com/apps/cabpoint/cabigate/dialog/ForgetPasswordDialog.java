package com.apps.cabpoint.cabigate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 7/26/2017.
 */

public class ForgetPasswordDialog extends Dialog {

    private EditText email;
    private Button recover;
    private Context context;
    private ImageView back;

    public ForgetPasswordDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_forgot_password);
        email = (EditText) findViewById(R.id.email_forget);
        recover = (Button) findViewById(R.id.recover_forget);
        back = (ImageView) findViewById(R.id.back_btn);
        email.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"clanpro_news.ttf"));
        recover.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"clanpro_news.ttf"));
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.isEmailValid(email))
                    ApiCalls.forgetPassword(email.getText().toString(), new PaxApiCallListner() {
                        @Override
                        public void onApiRequestResult(JSONObject jsonObject) {
                            parseForgetPasswordResponse(jsonObject);
                        }
                    });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void parseForgetPasswordResponse(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                String msg = response.getString(PaxApiCall.message);
                updateUI(recover,msg);
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                updateUI(recover,msg);
            }
        } catch (Exception e) {

        }
    }

    private void updateUI(final Button button, String label){
        final String previousLabel = button.getText().toString();
        button.setText(label);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setText(previousLabel);
            }
        },3000);
    }
}

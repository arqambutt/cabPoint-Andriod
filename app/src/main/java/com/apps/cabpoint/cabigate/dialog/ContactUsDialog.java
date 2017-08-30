package com.apps.cabpoint.cabigate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.PaymentInfo;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.ConnectionListner;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 7/24/2017.
 */

public class ContactUsDialog extends Dialog{

    private Button call, email;
    private PaymentInfo paymentInfo;
    private Context context;

    public ContactUsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_contact_us);
        initViews();
        setClickListners();
        if(utils.getActiveInternet(context)){
            getData();
        }
    }

    private void initViews() {
        call = (Button) findViewById(R.id.btn_yes);
        email = (Button) findViewById(R.id.btn_no);
        call.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "clanpro_news.ttf"));
        email.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "clanpro_news.ttf"));
        call.setText("PHONE");
        email.setText("EMAIL");
        paymentInfo = null;
    }

    private void setClickListners() {
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentInfo != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", paymentInfo.getContactPhone(), null));
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context,context.getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                }
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentInfo != null) {
                    utils.sendEmail(context, paymentInfo.getContactEmail());
                }else {
                    Toast.makeText(context,context.getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData() {
        ApiCalls.paymentInfo(PaxApiCall.companyIdValue, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                paymentInfo = PaxApiCall.parsePaymentResponse(jsonObject);
            }
        });
    }
}

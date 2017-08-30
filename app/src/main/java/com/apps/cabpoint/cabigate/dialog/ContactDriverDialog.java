package com.apps.cabpoint.cabigate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.Driver;

/**
 * Created by Abdul Ghani on 5/31/2017.
 */

public class ContactDriverDialog extends Dialog {
    private String driverNumber;
    private Context context;
    private Button messageDriver,callDriver;
    public ContactDriverDialog(@NonNull Context context, Driver driver) {
        super(context);
        this.context = context;
        driverNumber = driver.getPhone();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        initViews();
        setClickListners();
    }
    private void initViews(){
        messageDriver = (Button) findViewById(R.id.btn_msg_driver);
        callDriver = (Button) findViewById(R.id.btn_call_driver);
        messageDriver.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        callDriver.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
    }
    private void setClickListners(){
        messageDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",driverNumber, null)));
            }
        });
        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", driverNumber, null));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}

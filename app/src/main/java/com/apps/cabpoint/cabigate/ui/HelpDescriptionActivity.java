package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.NeedHelp;
import com.apps.cabpoint.cabigate.models.RideHistory;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 7/17/2017.
 */

public class HelpDescriptionActivity extends BaseActivity {

    private ImageView backBtn;
    private MobiTextView detailDescription;
    private EditText message;
    private Button submitBtn;
    private LinearLayout messageView;
    Context context = HelpDescriptionActivity.this;
    RideHistory rideHistory;
    NeedHelp needHelp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_help_description_layout);
        initViews();
        initClickLisners();
        getIntentData();
    }

    @Override
    protected void initViews() {
        backBtn = (ImageView) findViewById(R.id.back_btn_need_help_description);
        detailDescription = (MobiTextView) findViewById(R.id.help_description_details);
        message = (EditText) findViewById(R.id.message_need_help_description);
        submitBtn = (Button) findViewById(R.id.submit_btn_description);
        messageView = (LinearLayout) findViewById(R.id.message_view);
        message.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        submitBtn.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
    }

    @Override
    void initClickLisners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needHelp.getFeedBack().equals(PaxApiCall.yes)) {
                    if (utils.validInput(message)) {
                        reportProblem();
                    }
                } else {
                    reportProblem();
                }
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        needHelp = (NeedHelp) intent.getSerializableExtra("help");
        rideHistory = (RideHistory) intent.getSerializableExtra("history");
        detailDescription.setText(needHelp.getDetails());
        if (needHelp.getFeedBack().equals(PaxApiCall.no)) {
            message.setVisibility(View.GONE);
            messageView.setVisibility(View.GONE);
        }
    }

    private void reportProblem() {
        String comment;
        if (needHelp.getFeedBack().equals(PaxApiCall.yes)) {
            comment = message.getText().toString();
        } else {
            comment = "";
        }
        ApiCalls.reportProblem(rideHistory.getJobId(), StoragePreference.getUserId(context), comment, needHelp.getId(), new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(PaxApiCall.status);
                    if (status == 1) {
                        JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                        String msg = response.getString(PaxApiCall.message);
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        if (needHelp.getFeedBack().equals(PaxApiCall.yes)) {
                            message.setText("");
                        }
                    } else {
                        String msg = jsonObject.getString(PaxApiCall.errorMessage);
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }
}

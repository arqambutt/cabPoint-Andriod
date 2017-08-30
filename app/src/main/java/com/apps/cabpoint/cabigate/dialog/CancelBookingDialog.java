package com.apps.cabpoint.cabigate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 6/22/2017.
 */

public class CancelBookingDialog extends Dialog {
    private Button backBtn;
    private Button submit;
    private EditText reason;
    private String jobId, userId, token;
    private Context context;
    private DialogListner dialogListner;

    public CancelBookingDialog(@NonNull Context context, String jobId, DialogListner dialogListner) {
        super(context);
        this.context = context;
        this.jobId = jobId;
        userId = StoragePreference.getUserId(context);
        token = StoragePreference.getTOKEN(context);
        this.dialogListner = dialogListner;

        setContentView(R.layout.cab_booking_dialog);
        initViews();
        setClickLIstners();
    }

    private void initViews() {
        backBtn = (Button) findViewById(R.id.btn_no);
        submit = (Button) findViewById(R.id.btn_yes);
        reason = (EditText) findViewById(R.id.et_reason);
        backBtn.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        submit.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        reason.setTypeface(Typeface.createFromAsset(context.getAssets(),"clanpro_news.ttf"));
        reason.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        reason.setMaxLines(1);
    }

    private void setClickLIstners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialogListner.getDialogValue("Not");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });
    }

    private void cancelBooking() {
            ApiCalls.cancelBooking(userId, jobId, reason.getText().toString(), token, new PaxApiCallListner() {
                @Override
                public void onApiRequestResult(JSONObject jsonObject) {
                    try {
                        int status = jsonObject.getInt(PaxApiCall.status);
                        if (status == 1) {
                            utils.showInfoDialog(context, "Booking Cancel Successful", false);
                            dialogListner.getDialogValue("Cancel");
                            dismiss();
                        }
                    } catch (JSONException e) {
                        utils.showInfoDialog(context, "Booking Cancel Failed", false);
                        dialogListner.getDialogValue("Not");
                        dismiss();
                        e.printStackTrace();
                    }
                }
            });
    }
}

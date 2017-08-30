package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by Abdul Ghani on 5/29/2017.
 */

public class CreditCardActivity extends BaseActivity {

    EditText creditCardNumebr, month, year, creditCardVerification, postalCode;
    Button addCardLater;
    MobiTextView addCard;
    Context context = CreditCardActivity.this;
    AVLoadingIndicatorView avLoadingIndicatorView;
    ImageView backBtn;
    View loadingView;
    ArrayList<com.apps.cabpoint.cabigate.models.CreditCard> creditCards;
    private static int MY_SCAN_REQUEST_CODE = 99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_add_card);
        initViews();
        initClickLisners();
    }

    @Override
    protected void initViews() {
        creditCardNumebr = (EditText) findViewById(R.id.et_card_number);
        month = (EditText) findViewById(R.id.et_month);
        year = (EditText) findViewById(R.id.et_year);
        creditCardNumebr.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        month.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        year.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        creditCardVerification = (EditText) findViewById(R.id.et_ccv);
        addCard = (MobiTextView) findViewById(R.id.tv_save);
        addCardLater = (Button) findViewById(R.id.btn_scan_card);
        loadingView = findViewById(R.id.loading_indicator);
        postalCode = (EditText) findViewById(R.id.et_zip);
        avLoadingIndicatorView = (AVLoadingIndicatorView) loadingView.findViewById(R.id.avi);
        backBtn = (ImageView) findViewById(R.id.backarrow);
        creditCardNumebr.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        addCard.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        addCardLater.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        postalCode.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
    }

    @Override
    void initClickLisners() {
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
        addCardLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanPress();
            }
        });
        postalCode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkFields();
                    return true;
                }else {
                    return false;
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void addCard() {
        PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                parseCreditCardResponse(jsonObject);
            }
        };
        avLoadingIndicatorView.show();
        String userId = StoragePreference.getUserId(context);
        int exprieMonth = Integer.parseInt(month.getText().toString());
        int expireYear = Integer.parseInt(year.getText().toString());
        String cardNumber = creditCardNumebr.getText().toString();
        String ccv = creditCardVerification.getText().toString();
        String postCode = postalCode.getText().toString();
        String token = StoragePreference.getTOKEN(context);
        ApiCalls.addCardData(userId, exprieMonth, expireYear, cardNumber, ccv, postCode,token, paxApiCallListner);
    }

    void parseCreditCardResponse(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                Toast.makeText(context,"Successfully Added",Toast.LENGTH_SHORT).show();
                utils.showInfoDialog(context, "Successfully Added", false);
                clearFields();
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                utils.showInfoDialog(context, msg, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context,"Error/Try Again",Toast.LENGTH_SHORT).show();
            utils.showInfoDialog(context, "Error/Try Again", false);
        }
        avLoadingIndicatorView.hide();
    }

    void checkFields() {
        if (utils.isValidNumber(creditCardNumebr) && utils.isValidNumber(month) && utils.isValidNumber(year) && utils.isValidNumber(creditCardVerification)) {
            addCard();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void clearFields() {
        setHints(creditCardNumebr);
        setHints(month);
        setHints(year);
        setHints(creditCardVerification);
        setHints(postalCode);
    }

    void setHints(EditText editText) {
        editText.setText("");
    }

    public void onScanPress() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }


    void setCardData(String creditCardNumber, String month, String year, String ccv) {
        this.creditCardNumebr.setText(creditCardNumber);
        this.month.setText(month);
        this.year.setText(year);
        this.creditCardVerification.setText(ccv);
        postalCode.requestFocus();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                if (scanResult.isExpiryValid()) {
                    setCardData(scanResult.getFormattedCardNumber().replaceAll(" ", ""), scanResult.expiryMonth + "", scanResult.expiryYear + "", scanResult.cvv);
                }
            } else {
            }
        }
    }
}

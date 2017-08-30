package com.apps.cabpoint.cabigate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.PaymentInfo;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Abdul Ghani on 8/2/2017.
 */

public class AddPaymentMethodActivity extends BaseActivity {

    ImageView backBtn;
    TextView creditCard, paypal;
    Context context = this;
    private String fare, clientId;
    private PaymentInfo paymentInfo;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 100;
    private static final int REQUEST_CODE_PROFILE_SHARING = 10;
    private static final int PAYMENT = 10;
    private PayPalConfiguration config;
    private String metadataId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_method);
        initViews();
        initClickLisners();
        getIntentData();
        getPaymentInfo();
    }

    @Override
    protected void initViews() {
        backBtn = (ImageView) findViewById(R.id.back);
        creditCard = (TextView) findViewById(R.id.credit_card_add_payment);
        paypal = (TextView) findViewById(R.id.paypal_add_payment);
        clientId = "";
        paymentInfo = null;
    }

    @Override
    void initClickLisners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CreditCardActivity.class));
            }
        });
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileSharingPressed();
            }
        });
    }

    void getIntentData() {
        Intent data = getIntent();
        fare = data.getStringExtra(PaxApiCall.totalFare);
        if (fare != null) {
//            payPalBtnAction();
        }
    }

    void getPaymentInfo() {
        ApiCalls.paymentInfo(PaxApiCall.companyIdValue, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                paymentInfo = PaxApiCall.parsePaymentResponse(jsonObject);
                if (paymentInfo != null) {
                    clientId = paymentInfo.getClientId();
                    config = new PayPalConfiguration()
                            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
                            .clientId(clientId).merchantName("Cabpoint")
                            .merchantPrivacyPolicyUri(Uri.parse(paymentInfo.getTermsAndCondition()))
                            .merchantUserAgreementUri(Uri.parse(paymentInfo.getTermsAndCondition() + "/legal"));
                    startPayPalService();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPayPalService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                    try {
//                        Log.i("paymentExample", confirm.toJSONObject().toString(4));
//                        parsePayPalResponse(confirm.toJSONObject());
//                        // TODO: send 'confirm' to your server for verification.
//                        // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
//                        // for more details.
//
//                    } catch (JSONException e) {
//                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i("paymentExample", "The user canceled.");
//            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
//        if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                PayPalAuthorization auth = data
//                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
//                if (auth != null) {
//                    try {
////                        sendAuthorizationToServer(auth);
//
//                    } catch (Exception e) {
//                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i("FuturePaymentExample", "The user canceled.");
//            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i("FuturePaymentExample",
//                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
//            }
//        }
        if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.d("Profile Auth Code", auth.getAuthorizationCode());
                        sendAuthorizationToServer(auth);

                    } catch (Exception e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
        String passengerId = StoragePreference.getUserId(context);
        ApiCalls.payPalAuthCode(passengerId, authorization.getAuthorizationCode(), metadataId, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(PaxApiCall.status);
                    if (status == 1) {
                        JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                        String msg = response.getString(PaxApiCall.message);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void startPayPalService() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    void stopPayPalService() {
        stopService(new Intent(this, PayPalService.class));
    }

//    void payPalBtnAction() {
//        if (fare == null) {
//            onProfileSharingPressed();
//        } else {
//            PayPalPayment payment = new PayPalPayment(new BigDecimal(fare), "GBP", "Cab Point Ride Fare",
//                    PayPalPayment.PAYMENT_INTENT_SALE);
//            Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
//            // send the same configuration for restart resiliency
//            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//            intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
//            startActivityForResult(intent, PAYMENT);
//        }
//    }

//    public void onFuturePaymentPressed(View pressed) {
//        metadataId = PayPalConfiguration.getClientMetadataId(this);
//        Intent intent = new Intent(this, PayPalFuturePaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
//        Log.d("Auth meta", metadataId);
//    }

//    void parsePayPalResponse(JSONObject jsonObject) {
//        try {
//            JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
//            String state = response.getString("state");
//            String transactionId = response.getString("id");
//            if (state.equals("approved")) {
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra(PaxApiCall.payPalTransId, transactionId);
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public void onProfileSharingPressed() {
        if(paymentInfo!=null){
            metadataId = PayPalConfiguration.getClientMetadataId(this);
            Intent intent = new Intent(this, PayPalProfileSharingActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());
            startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
        }else {
            Toast.makeText(context,"Try Later",Toast.LENGTH_SHORT).show();
        }
    }

    private PayPalOAuthScopes getOauthScopes() {
        Set<String> scopes = new HashSet<>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS, PayPalOAuthScopes.PAYPAL_SCOPE_FUTURE_PAYMENTS));
        return new PayPalOAuthScopes(scopes);
    }
}

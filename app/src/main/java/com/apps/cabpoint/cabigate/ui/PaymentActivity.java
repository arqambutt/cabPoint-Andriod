package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.adapter.PaymentMethodAdapter;
import com.apps.cabpoint.cabigate.models.CreditCard;
import com.apps.cabpoint.cabigate.models.PayPal;
import com.apps.cabpoint.cabigate.models.PaymentMethod;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.ConnectionListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdul Ghani on 7/24/2017.
 */

public class PaymentActivity extends BaseActivity implements ConnectionListner {

    private MobiTextView paymentHeading, addPromoCode;
    private Context context = this;
    private ImageView back, payPalSelection, cashSelection, cardSelection;
    private Button addPaymentBtn;
    private RecyclerView paymentRecyclerView;
    private List<CreditCard> creditCards = null;
    private List<PayPal> payPalList = null;
    private boolean enableWifiListner, enableMobileDataListner = false;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_layout);
        initViews();
        initClickLisners();
        if (utils.getActiveInternet(context)) {
            getUserCards();
        } else {
            onDisconnected();
        }
        setConnectionListner(this);
    }

    @Override
    protected void initViews() {
        paymentHeading = (MobiTextView) findViewById(R.id.payment_method_heading);
        back = (ImageView) findViewById(R.id.back_payment);
        addPromoCode = (MobiTextView) findViewById(R.id.add_promo_code);
        addPaymentBtn = (Button) findViewById(R.id.add_payment_btn);
        paymentRecyclerView = (RecyclerView) findViewById(R.id.payment_method_recycler);
        handler = new Handler();
    }

    @Override
    void initClickLisners() {
        addPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddPaymentMethodActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,PromotionsActivity.class));
            }
        });
    }

    void setDefaultPaymentMethod() {
        cashSelection.setVisibility(View.INVISIBLE);
        payPalSelection.setVisibility(View.INVISIBLE);
        cardSelection.setVisibility(View.INVISIBLE);
        String method = StoragePreference.getPaymentMethod(context);
        if (!method.equals(StoragePreference.notAvailable)) {
            if (method.equals(PaxApiCall.cash)) {
                cashSelection.setVisibility(View.VISIBLE);
            } else if (method.equals(PaxApiCall.paypal)) {
                payPalSelection.setVisibility(View.VISIBLE);
            } else if (method.equals(PaxApiCall.credit)) {
                cardSelection.setVisibility(View.VISIBLE);
            }
        }
    }

    void setRecyclerViewData(List<CreditCard> creditCardList,List<PayPal> payPals) {

        String paymentMethod = StoragePreference.getPaymentMethod(context);
        List<PaymentMethod> paymentMethods = new ArrayList<>();

        paymentMethods.add(new PaymentMethod(PaymentMethod.cash, "Cash", paymentMethod.equals(PaxApiCall.cash)));
        if (creditCardList != null) {
            for (CreditCard creditCard : creditCardList) {
                if (paymentMethod.equals(PaxApiCall.credit)) {
                    if (creditCard.getDefaultCard().equals("1")) {
                        paymentMethods.add(new PaymentMethod(PaymentMethod.credit, "******" + creditCard.getLastFourDigits(), true, creditCard.getCardId()));
                    } else {
                        paymentMethods.add(new PaymentMethod(PaymentMethod.credit, "******" + creditCard.getLastFourDigits(), false, creditCard.getCardId()));
                    }
                } else {
                    paymentMethods.add(new PaymentMethod(PaymentMethod.credit, "******" + creditCard.getLastFourDigits(), false, creditCard.getCardId()));
                }
            }
        }
        if(payPalList !=null){
            for (PayPal payPal: payPals) {
                if (paymentMethod.equals(PaxApiCall.paypal)) {
                    if (payPal.getIsDefault().equals("1")) {
                        paymentMethods.add(new PaymentMethod(PaymentMethod.paypal, payPal.getEmail(), true,payPal.getPayPalId()));
                    } else {
                        paymentMethods.add(new PaymentMethod(PaymentMethod.paypal, payPal.getEmail(), false,payPal.getPayPalId()));
                    }
                } else {
                    paymentMethods.add(new PaymentMethod(PaymentMethod.paypal, payPal.getEmail(), false,payPal.getPayPalId()));
                }
            }
        }
        PaymentMethodAdapter paymentMethodAdapter = new PaymentMethodAdapter(paymentMethods);
        paymentRecyclerView.setAdapter(paymentMethodAdapter);
    }

    void getUserCards() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        paymentRecyclerView.setLayoutManager(linearLayoutManager);
        payPalList = null;
        creditCards = null;
        ApiCalls.storeCardList(StoragePreference.getUserId(context), StoragePreference.getTOKEN(context), userCardsListner);
    }

    PaxApiCallListner userCardsListner = new PaxApiCallListner() {
        @Override
        public void onApiRequestResult(JSONObject jsonObject) {
            parseCardList(jsonObject);
        }
    };

    void parseCardList(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                creditCards = new ArrayList<>();
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                JSONArray cardArray = response.getJSONArray(PaxApiCall.list);
                for (int i = 0; i < cardArray.length(); i++) {
                    JSONObject card = (JSONObject) cardArray.get(i);
                    Gson gson = new Gson();
                    CreditCard creditCard = gson.fromJson(card.toString(), CreditCard.class);
                    creditCards.add(creditCard);
                }
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                utils.showInfoDialog(context, msg, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            getPayPalAccounts();
        }
    }

    void getPayPalAccounts(){
        ApiCalls.listPaypalAccouts(StoragePreference.getUserId(context),payPayListner);
    }

    PaxApiCallListner payPayListner = new PaxApiCallListner() {
        @Override
        public void onApiRequestResult(JSONObject jsonObject) {
            parsePayPalResponse(jsonObject);
        }
    };

    void parsePayPalResponse(JSONObject jsonObject){
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                payPalList = new ArrayList<>();
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                JSONArray paypalArray = response.getJSONArray(PaxApiCall.list);
                for (int i = 0; i < paypalArray.length(); i++) {
                    JSONObject card = (JSONObject) paypalArray.get(i);
                    Gson gson = new Gson();
                    PayPal payPal = gson.fromJson(card.toString(), PayPal.class);
                    payPalList.add(payPal);
                }
                setRecyclerViewData(creditCards,payPalList);
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                utils.showInfoDialog(context, msg, false);
                setRecyclerViewData(creditCards,payPalList);
            }
        } catch (JSONException e) {
            setRecyclerViewData(creditCards,payPalList);
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserCards();
    }

    void sendAPIRequest() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (utils.getActiveInternet(context)) {
                    getUserCards();
                } else {
                    sendAPIRequest();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void onConnected() {
        addPaymentBtn.setEnabled(true);
        addPaymentBtn.setText("Add");
        paymentHeading.setText(getString(R.string.payment_methods));
        sendAPIRequest();
    }

    public void onDisconnected() {
        addPaymentBtn.setEnabled(false);
        addPaymentBtn.setText("Not Available");
        paymentHeading.setText("Unable to get " + getString(R.string.payment_methods));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeConnectionListner();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onWifiConnected() {
        if (enableWifiListner) {
            onConnected();
        } else {
            enableWifiListner = true;
        }
    }

    @Override
    public void onWifiDisconnected() {
        if (enableWifiListner) {
            onDisconnected();
        } else {
            enableWifiListner = true;
        }
    }

    @Override
    public void onMobileDataConnected() {
        if (enableMobileDataListner) {
            onConnected();
        } else {
            enableMobileDataListner = true;
        }
    }

    @Override
    public void onMobileDataDisconnected() {
        if (enableMobileDataListner) {
            onDisconnected();
        } else {
            enableMobileDataListner = true;
        }
    }
}

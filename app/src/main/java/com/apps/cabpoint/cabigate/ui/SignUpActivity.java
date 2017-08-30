package com.apps.cabpoint.cabigate.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    EditText userName, email, password;
    MobiTextView txt_already;
    Button signUpBtn;
    IntlPhoneInput phoneInputView;
    Context context = SignUpActivity.this;
    private static final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_signup);
        initViews();
        initClickLisners();
        checkPermissions();
    }

    @Override
    protected void initViews() {
        userName = (EditText) findViewById(R.id.userName);
        email = (EditText) findViewById(R.id.userEmail);
        password = (EditText) findViewById(R.id.password);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        txt_already = (MobiTextView) findViewById(R.id.txt_already);
        phoneInputView = (IntlPhoneInput) findViewById(R.id.mobileNumber);
        userName.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        email.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        password.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));

        phoneInputView.setDefault();

        txt_already.setOnClickListener(this);
    }

    @Override
    void initClickLisners() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    void signUp() {
        if(utils.getActiveInternet(context)){
            if (utils.validInput(userName) && utils.validInput(password)) {
                String myInternationalNumber;
                if (phoneInputView.isValid()) {
                    myInternationalNumber = phoneInputView.getNumber();
                    PaxApiCall.requestAPI(PaxApiCall.SIGN_UP, PaxApiCall.getSignupParameters(get(userName), get(userName), myInternationalNumber, get(password), get(email)), paxApiCallListner);
                } else {
                    Toast.makeText(context, "Invalid Number", Toast.LENGTH_LONG).show();
                }
            }
        }else {
            Toast.makeText(context,R.string.no_internet,Toast.LENGTH_LONG).show();
        }
    }

    String get(EditText editText) {
        return editText.getText().toString();
    }

    PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
        @Override
        public void onApiRequestResult(JSONObject jsonObject) {
            onSignUpResponse(jsonObject);
        }
    };

    void onSignUpResponse(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                sendResponseToVerifyPhone(jsonObject);
            } else {
                String error = jsonObject.getString("error_msg");
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void sendResponseToVerifyPhone(JSONObject jsonObject) {
        JSONObject responseObject = null;
        try {

            responseObject = jsonObject.getJSONObject(PaxApiCall.response);
            String userId = responseObject.getString(PaxApiCall.userId);
            String firstName = responseObject.getString(PaxApiCall.firstName);
            String keyMatch = responseObject.getString(PaxApiCall.keyMatch);
            String opt = responseObject.getString(PaxApiCall.oPT);
            String token = responseObject.getString(PaxApiCall.token);
            String companyId = responseObject.getString(PaxApiCall.companyId);


            Intent intentToVerifyPhone = new Intent(context, VerifyPhoneActivity.class);
            intentToVerifyPhone.putExtra(PaxApiCall.userId, userId);
            intentToVerifyPhone.putExtra(PaxApiCall.keyMatch, keyMatch);
            intentToVerifyPhone.putExtra(PaxApiCall.oPT, opt);
            intentToVerifyPhone.putExtra(PaxApiCall.token, token);
            intentToVerifyPhone.putExtra(PaxApiCall.firstName, firstName);
            intentToVerifyPhone.putExtra(PaxApiCall.password, password.getText().toString());
            intentToVerifyPhone.putExtra(PaxApiCall.companyId,companyId);
            intentToVerifyPhone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentToVerifyPhone);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v == txt_already) {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void checkPermissions() {
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        if (!utils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ALL && permissions.length > 0 && grantResults.length > 0) {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
            if (!utils.hasPermissions(this, PERMISSIONS)) {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

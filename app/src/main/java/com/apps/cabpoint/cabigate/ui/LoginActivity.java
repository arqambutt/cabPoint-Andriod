package com.apps.cabpoint.cabigate.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.dialog.ForgetPasswordDialog;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    EditText email, password;
    MobiTextView login, signUp;
    Context context = LoginActivity.this;
    String TAG = "Login";
    ImageView img_icon;
    MobiTextView forgetPassword;
    private static final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_login);
        initViews();
        initClickLisners();
        checkPermissions();
    }

    public void showSnackBar(String msg, double seconds) {
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl_snackBar);
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
            MobiTextView textView = (MobiTextView) findViewById(R.id.msg);
            textView.setText(msg);
            Animation slideInOut = getAnimation(R.anim.slide_in_up);
            slideInOut.setDuration(250);
            layout.startAnimation(slideInOut);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout.startAnimation(getAnimation(R.anim.slide_out_down));
                    layout.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }, (long) (seconds * 1000));
        }
    }

    public Animation getAnimation(int animId) {
        return AnimationUtils.loadAnimation(this, animId);
    }

    @Override
    protected void initViews() {
        email = (EditText) findViewById(R.id.userEmail);
        password = (EditText) findViewById(R.id.userPassword);
        login = (MobiTextView) findViewById(R.id.loginBtn);
        email.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        password.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        //fbLogin = (Button) findViewById(R.id.fbLoginBtn);
        signUp = (MobiTextView) findViewById(R.id.sign_up);

        img_icon = (ImageView) findViewById(R.id.img_icon);
        forgetPassword = (MobiTextView) findViewById(R.id.forgot_password);

        //utils.animation(this,rel_bk,img_icon);
    }

    private void updateUI(final MobiTextView view, final String label, String msg) {
        view.setText(msg.toUpperCase());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setText(label);
            }
        }, 5000);

    }

    @Override
    void initClickLisners() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAction();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPhonePermission();
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ForgetPasswordDialog(context).show();
            }
        });
    }

    PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
        @Override
        public void onApiRequestResult(JSONObject jsonObject) {
            onResultResponse(jsonObject);
        }
    };

    private void loginAction() {
        if (utils.getActiveInternet(context)) {
            if (email.length() != 0) {
                if (password.getText().toString().trim().isEmpty()) {
                    showSnackBar("Password Required", 2);
                } else {
                    login.setText("LOGGING IN...");
                    PaxApiCall.requestAPI(PaxApiCall.SIGN_IN, PaxApiCall.getSignInParameters(email.getText().toString(), password.getText().toString()), paxApiCallListner);
                }
            } else {
                showSnackBar("Email Required", 2);
            }
        } else {
            showSnackBar(getString(R.string.no_internet), 2);
        }
    }

    void onResultResponse(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                String userId, firstName, token, companyId;
                userId = response.getString(PaxApiCall.userId);
                token = response.getString(PaxApiCall.token);
                firstName = response.getString(PaxApiCall.firstName);
                companyId = response.getString(PaxApiCall.companyId);
                StoragePreference.writeUserData(context, userId, token, firstName, password.getText().toString(),companyId);
                login.setText("success".toUpperCase());
                updateUI(login, login.getText().toString(), "login");
                Intent intent = new Intent(context, MapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                login.setText("Login");
                String error = jsonObject.getString("error_msg");
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                password.setText("");

            }
        } catch (Exception e) {
            // utils.showInfoDialog(context, "Login Failed,Try Again", false);
            showSnackBar("Login Failed Try Again", 3);
            e.printStackTrace();
        }
    }

    void checkPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE}, 10);
            } else {
                showSnackBar("Phone Permission Required", 3);
                //  utils.showInfoDialog(context, "Phone Permission Required", false);
            }
        } else {
            startActivity(new Intent(context, SignUpActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10 && permissions.length > 0 && grantResults.length > 0) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                showSnackBar("Permission Denied", 3);
            } else {
                startActivity(new Intent(context, SignUpActivity.class));
                finish();
            }
        }
        if (requestCode == PERMISSION_ALL && permissions.length > 0 && grantResults.length > 0) {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
            if (!utils.hasPermissions(this, PERMISSIONS)) {
                showSnackBar("Permission Denied", 3);
            }
        }
    }

    void checkPermissions() {
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        if (!utils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
}

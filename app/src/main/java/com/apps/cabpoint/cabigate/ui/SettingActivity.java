package com.apps.cabpoint.cabigate.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.models.PaymentInfo;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.CircleTransform;
import com.apps.cabpoint.cabigate.utils.ConnectionListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static android.R.attr.enabled;
import static android.R.attr.viewportHeight;

/**
 * Created by Abdul Ghani on 8/3/2017.
 */

public class SettingActivity extends BaseActivity implements ConnectionListner {

    private ImageView profilePic, back;
    private TextView userName, name, location, email, mobile, termsAndConditons;
    private LinearLayout namelayout, mobileLayout, workLayout, homeLayout;
    private Context context = this;
    private Passenger passenger;
    private static final int PROFILE_CHOSER_REQUEST_CODE = 5;
    private static final int PROFILE_PERMISSION_REQUEST_CODE = 10;
    private static final int UPDATE_PHONE_PERMISSION = 20;
    private static final int ACTIVITY_FOR_RESULT = 30;
    private static final int SIZE_OF_FILE = 250;
    private static final String TAG = SettingActivity.class.getName();
    private boolean enableWifiListner, enableMobileDataListner = false;
    private View internetError;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout_new);
        initViews();
        initClickLisners();
        setInitialData();
        setConnectionListner(this);
    }

    @Override
    void initViews() {
        profilePic = (ImageView) findViewById(R.id.profile_pic_settings);
        back = (ImageView) findViewById(R.id.back_settings);
        userName = (TextView) findViewById(R.id.user_name_setting);
        location = (TextView) findViewById(R.id.location_setting);
        name = (TextView) findViewById(R.id.name_setting);
        email = (TextView) findViewById(R.id.email_setting);
        mobile = (TextView) findViewById(R.id.mobile_number_settting);
        termsAndConditons = (TextView) findViewById(R.id.terms_conditions);
        namelayout = (LinearLayout) findViewById(R.id.name_layout_setting);
        mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout_setting);
        workLayout = (LinearLayout) findViewById(R.id.work_layout_setting);
        homeLayout = (LinearLayout) findViewById(R.id.home_layout_setting);
        internetError = findViewById(R.id.internet_error_layout);
        passenger = new Passenger();
        handler = new Handler();
        enableViews(false);
    }

    @Override
    void initClickLisners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        termsAndConditons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.getActiveInternet(context))
                    termsAndConditionAction();
                else
                    Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        });
        mobileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhonePermission();
            }
        });
        namelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameAction();
            }
        });
        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCustomPlaces();
            }
        });
        workLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePicAction();
            }
        });
    }

    void setInitialData() {
        Intent data = getIntent();
        String name = data.getStringExtra(PaxApiCall.firstName);
        String locationString = data.getStringExtra("location");
        userName.setText(name);
        location.setText(locationString);
        if (utils.getActiveInternet(context)) {
            getUserData();
        } else {
            onDisconnected();
        }
    }

    void setPreSavedUser() {
        Passenger passenger = StoragePreference.getPASSENGER(context);
        if (passenger != null) {
            setUserData(passenger);
        }
    }

    void enableViews(boolean enable) {
        termsAndConditons.setEnabled(enable);
        mobileLayout.setEnabled(enable);
        namelayout.setEnabled(enable);
        homeLayout.setEnabled(enable);
        workLayout.setEnabled(enable);
    }

    void getUserData() {
        String userId = StoragePreference.getUserId(context);
        String token = StoragePreference.getTOKEN(context);
        PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                parseUserJSON(jsonObject);
            }
        };
        ApiCalls.getUserProfileData(userId, token, paxApiCallListner);
    }

    void parseUserJSON(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject userData = jsonObject.getJSONObject(PaxApiCall.response);
                passenger.setFirstName(userData.getString(PaxApiCall.firstName));
                passenger.setLastName(userData.getString(PaxApiCall.lastName));
                passenger.setEmail(userData.getString(PaxApiCall.email));
                passenger.setPhone(userData.getString(PaxApiCall.phone));
                passenger.setProfile(userData.getString(PaxApiCall.profilePic));
                passenger.setHomeAddress(userData.getString(PaxApiCall.homeAddress));
                passenger.setHomeLatitude(userData.getString(PaxApiCall.homeLatitide));
                passenger.setHomeLongitude(userData.getString(PaxApiCall.homeLongitude));
                passenger.setWorkAddress(userData.getString(PaxApiCall.workAddress));
                passenger.setWorkLatitude(userData.getString(PaxApiCall.workLatitiude));
                passenger.setWorkLongitude(userData.getString(PaxApiCall.workLongitude));
                passenger.setUserId(StoragePreference.getUserId(context));
                passenger.setToken(StoragePreference.getTOKEN(context));
                passenger.setPassword(StoragePreference.getPASSWORD(context));
                enableViews(true);
                setUserData(passenger);
            } else {
                utils.showInfoDialog(context, "Data Not Found", false);
            }
        } catch (Exception e) {
            utils.showInfoDialog(context, "Error/Try Again", false);
        }
    }

    void setUserData(Passenger passenger) {
        userName.setText(passenger.getFirstName() + " " + passenger.getLastName());
        name.setText(passenger.getFirstName() + " " + passenger.getLastName());
        email.setText(passenger.getEmail());
        if (!passenger.getHomeAddress().equals("")) {
            LatLng latLng = new LatLng(Double.parseDouble(passenger.getHomeLatitude()), Double.parseDouble(passenger.getHomeLongitude()));
            StoragePreference.setHomeLocation(context, passenger.getHomeAddress(), latLng);
        }
        if (!passenger.getWorkAddress().equals("")) {
            LatLng latLng = new LatLng(Double.parseDouble(passenger.getWorkLatitude()), Double.parseDouble(passenger.getWorkLongitude()));
            StoragePreference.setWorkLocation(context, passenger.getWorkAddress(), latLng);
        }
        Picasso.with(context).load(passenger.getProfile()).error(R.drawable.user_image).transform(new CircleTransform()).into(profilePic);
        mobile.setText(passenger.getPhone());
    }

    void termsAndConditionAction() {
        ApiCalls.paymentInfo(PaxApiCall.companyIdValue, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                PaymentInfo paymentInfo = PaxApiCall.parsePaymentResponse(jsonObject);
                if (paymentInfo != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(paymentInfo.getTermsAndCondition()));
                    startActivity(i);
                } else {
                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void profilePicAction() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PROFILE_PERMISSION_REQUEST_CODE);
            } else {
                utils.showInfoDialog(context, "Phone Permission Required", false);
            }
        } else {
            utils.selectImage(SettingActivity.this, PROFILE_CHOSER_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PROFILE_PERMISSION_REQUEST_CODE && permissions.length > 0 && grantResults.length > 0) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                utils.showInfoDialog(context, "Permissions Denied", false);
            } else {
                profilePicAction();
            }
        }
        if (requestCode == UPDATE_PHONE_PERMISSION && permissions.length > 0 && grantResults.length > 0) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                utils.showInfoDialog(context, "Permissions Denied", false);
            } else {
                checkPhonePermission();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PROFILE_CHOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                final String path = utils.getImagePathFromURI(context, uri);
                if (utils.lengthOfFile(path) <= SIZE_OF_FILE) {
                    uploadPicture(path);
                } else {
                    try {
                        compressAndUpload(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == ACTIVITY_FOR_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                Passenger newPassenger = (Passenger) data.getSerializableExtra(PaxApiCall.passengers);
                if (newPassenger != null) {
                    passenger = newPassenger;
                    setUserData(newPassenger);
                }
            }
        }
    }

    void checkPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, UPDATE_PHONE_PERMISSION);
            } else {
                utils.showInfoDialog(context, "Phone Permission Required", false);
            }
        } else {
            changePhoneNumber();
        }
    }

    void uploadPicture(final String path) {
        ApiCalls.updateProfilePic(StoragePreference.getUserId(context), path, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(PaxApiCall.status);
                    if (status == 1) {
                        Picasso.with(context).load(new File(path)).transform(new CircleTransform()).into(profilePic);
                    } else {
                        String msg = jsonObject.getString(PaxApiCall.errorMessage);
                        utils.showInfoDialog(context, msg, false);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        });
    }

    void compressAndUpload(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        bitmap = utils.getResizedBitmap(bitmap, SIZE_OF_FILE);
        File profile = utils.getFileFromBitmap(context, bitmap);
        uploadPicture(profile.getPath());
    }

    void changePhoneNumber() {
        Intent intent = new Intent(context, ChangePhoneActivity.class);
        intent.putExtra(PaxApiCall.passengers, passenger);
        startActivityForResult(intent, ACTIVITY_FOR_RESULT);
    }

    void sendAPIRequest() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (utils.getActiveInternet(context)) {
                    getUserData();
                } else {
                    sendAPIRequest();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    void changeCustomPlaces() {
        Intent intent = new Intent(context, ChangeCustomPlaceActivity.class);
        intent.putExtra(PaxApiCall.passengers, passenger);
        startActivityForResult(intent, ACTIVITY_FOR_RESULT);
    }

    void changeNameAction() {
        Intent intent = new Intent(context, ChangeNameActivity.class);
        intent.putExtra(PaxApiCall.passengers, passenger);
        startActivityForResult(intent, ACTIVITY_FOR_RESULT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeConnectionListner();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    public void onConnected() {
        internetError.setVisibility(View.INVISIBLE);
        sendAPIRequest();
    }


    public void onDisconnected() {
        internetError.setVisibility(View.VISIBLE);
        ImageView iconLocation = (ImageView) internetError.findViewById(R.id.img_wifioff);
        ImageView back = (ImageView) internetError.findViewById(R.id.nav_menu);
        TextView topText = (TextView) internetError.findViewById(R.id.txt_connectivity);
        TextView textTwo = (TextView) internetError.findViewById(R.id.txt_connectivity_two);
        topText.setText(R.string.no_connectivity);
        textTwo.setText(R.string.please_check_your_internet_connection);
        iconLocation.setImageResource(R.drawable.ic_no_wifi);
        back.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        setPreSavedUser();
        enableViews(false);
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

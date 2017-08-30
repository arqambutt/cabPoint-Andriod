package com.apps.cabpoint.cabigate.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;

public class MainActivity extends BaseActivity {

    Context context = MainActivity.this;
    private static final int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        if (!utils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            checkSession();
        }
    }

    @Override
    protected void initViews() {
    }

    @Override
    void initClickLisners() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ALL && permissions.length > 0 && grantResults.length > 0) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                utils.showInfoDialog(context, "Permission Denied", false);
                finish();
            } else {
                checkSession();
            }
        }
    }

    void checkSession() {
        String session = StoragePreference.readUserData(context);
        if (!session.equals(StoragePreference.notAvailable)) {
            PaxApiCall.companyIdValue = StoragePreference.getCompanyId(context);
            startActivity(new Intent(context, MapActivity.class));
            finish();
        } else {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }
}

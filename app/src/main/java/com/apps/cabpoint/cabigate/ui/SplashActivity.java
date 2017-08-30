package com.apps.cabpoint.cabigate.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    Context context = SplashActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.front_view_layout);
        if (StoragePreference.isFirstTime(context)) {
            startActivity(new Intent(context, Activity_Intro.class));
            finish();
        } else {
            checkIfSessionExist();
        }
    }

    void checkIfSessionExist() {
        if (StoragePreference.isSessionExist(context)) {
            startActivity(new Intent(context, MapActivity.class));
            finish();
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}

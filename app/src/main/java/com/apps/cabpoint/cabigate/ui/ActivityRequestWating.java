package com.apps.cabpoint.cabigate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Abdul Ghani on 7/8/2017.
 */

public class ActivityRequestWating extends BaseActivity {

    private MobiTextView current,destination;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_wating_layout);
        initViews();
        getIntentData();
    }

    @Override
    protected void initViews() {
        current = (MobiTextView) findViewById(R.id.request_wating_from_text);
        destination = (MobiTextView) findViewById(R.id.request_wating_to_txt);
        View loadingView = findViewById(R.id.loading_indicator);
        AVLoadingIndicatorView avLoadingIndicatorView = (AVLoadingIndicatorView) loadingView.findViewById(R.id.avi);
        avLoadingIndicatorView.show();
    }

    @Override
    void initClickLisners() {

    }

    void getIntentData(){
        Intent intent = getIntent();
        current.setText(intent.getStringExtra("current"));
        destination.setText(intent.getStringExtra("destination"));
    }
}

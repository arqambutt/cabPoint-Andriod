package com.apps.cabpoint.cabigate.ui;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.CircleTransform;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 7/12/2017.
 */

public class DriverRatingActivity extends BaseActivity {
    private ImageView driverImage;
    private MobiTextView driverName;
    private RatingBar driverRating;
    private EditText comment;
    private Button submit;
    private String jobId, passengerId;

    void getIntentData() {
        Intent intent = getIntent();
        String image, name;
        image = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        jobId = intent.getStringExtra("jobid");
        passengerId = intent.getStringExtra("pid");
        Picasso.with(this).load(image).transform(new CircleTransform()).into(driverImage);
        driverName.setText(name);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_rating);
        initViews();
        initClickLisners();
        getIntentData();
    }

    @Override
    protected void initViews() {
        driverImage = (ImageView) findViewById(R.id.driver_image_rating);
        driverName = (MobiTextView) findViewById(R.id.driver_name_rating);
        driverRating = (RatingBar) findViewById(R.id.driver_rating_bar);
        comment = (EditText) findViewById(R.id.driver_comment_rating);
        submit = (Button) findViewById(R.id.submit_rating);
        driverRating.setRating(2.5f);
        comment.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        submit.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        comment.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    @Override
    void initClickLisners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDriver();
            }
        });
        comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    rateDriver();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void rateDriver() {
        ApiCalls.rateAndPay(jobId, passengerId, driverRating.getRating(), comment.getText().toString(), new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                parseRatingResponse(jsonObject);
            }
        });
    }

    void parseRatingResponse(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                String msg = response.getString(PaxApiCall.message);
                startMapActivity();
                finish();
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                startMapActivity();
                finish();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startMapActivity();
        finish();
    }

    void startMapActivity() {
        startActivity(new Intent(this, MapActivity.class));
    }
}

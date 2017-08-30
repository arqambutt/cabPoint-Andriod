package com.apps.cabpoint.cabigate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.RideHistory;
import com.apps.cabpoint.cabigate.utils.CircleTransform;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Abdul Ghani on 7/16/2017.
 */

public class HistoryDetailActivity extends BaseActivity {

    private Context context;
    private RatingBar driverRating;
    private MobiTextView currentLocation, destinationLocation, date, fare, paymentType, driverName, driverNickName, driverContact, carModel, carNumber, duration, numberOfPassenger, needHelp;
    private ImageView driverImage,carImage,back;
    private static final String TAG = HistoryDetailActivity.class.getName();
    private RideHistory rideHistory;
    private Button rideAgain;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_history);
        context = HistoryDetailActivity.this;
        initViews();
        getIntentData();
        setDataToViews();
        initClickLisners();
    }

    @Override
    protected void initViews() {
        currentLocation = (MobiTextView) findViewById(R.id.current_location_history);
        destinationLocation = (MobiTextView) findViewById(R.id.destination_location_history);
        date = (MobiTextView) findViewById(R.id.txt_date);
        fare = (MobiTextView) findViewById(R.id.txt_pound);
        paymentType = (MobiTextView) findViewById(R.id.payment_type_history);
        driverName = (MobiTextView) findViewById(R.id.txt_driver);
        driverNickName = (MobiTextView) findViewById(R.id.txt_nick_driver);
        driverRating = (RatingBar) findViewById(R.id.RatingBar);
        driverContact = (MobiTextView) findViewById(R.id.driver_contact_history);
        carModel = (MobiTextView) findViewById(R.id.txt_car_model);
        carNumber = (MobiTextView) findViewById(R.id.txt_registration);
        duration = (MobiTextView) findViewById(R.id.txt_duration_time);
        numberOfPassenger = (MobiTextView) findViewById(R.id.txt_total_no_passengers);
        needHelp = (MobiTextView) findViewById(R.id.need_help_history);
        carImage = (ImageView) findViewById(R.id.car_img);
        driverImage = (ImageView) findViewById(R.id.driver_img);
        back = (ImageView) findViewById(R.id.back_btn_history);
        rideAgain = (Button) findViewById(R.id.ride_again);
        rideAgain.setTypeface(Typeface.createFromAsset(getAssets(),"clanpro_news.ttf"));
        driverRating.setClickable(false);

    }

    void getIntentData(){
        Intent intent = getIntent();
        rideHistory = (RideHistory) intent.getSerializableExtra("history");
    }

    void setDataToViews(){
        currentLocation.setText(rideHistory.getPickUpLocation());
        destinationLocation.setText(rideHistory.getDropLocation());
        date.setText(rideHistory.getPickUpDate());
        fare.setText(rideHistory.getTotalFare());
        paymentType.setText(rideHistory.getPaymentMethod());
        driverName.setText(rideHistory.getDriverName());
        driverNickName.setText(rideHistory.getUserName());
        driverRating.setRating(Float.parseFloat(rideHistory.getDriverRating()));
        driverContact.setText(rideHistory.getDriverPhone());
        carModel.setText(rideHistory.getTaxiModel());
        carNumber.setText(rideHistory.getVehiclePlateNumber());
        duration.setText(rideHistory.getDuration());
        numberOfPassenger.setText(rideHistory.getPassengerCount());
        Picasso.with(context).load(rideHistory.getDriverImage()).transform(new CircleTransform()).into(driverImage);
        Picasso.with(context).load(rideHistory.getVehicleImage()).transform(new CircleTransform()).into(carImage);
    }

    @Override
    void initClickLisners() {
        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NeedHelpActivity.class);
                intent.putExtra("history",rideHistory);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rideAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("history",rideHistory);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}

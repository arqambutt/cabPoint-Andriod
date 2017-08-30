package com.apps.cabpoint.cabigate.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.models.Booking;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.google.android.gms.maps.model.LatLng;
import com.wang.avi.AVLoadingIndicatorView;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Abdul Ghani on 6/1/2017.
 */

public class BookJourneyActivity extends BaseActivity {

    EditText fullName, passengers;
    MobiTextView pickUp, destination, date, time, payment;
    RelativeLayout pickUpLocation, destinationLocation;
    Button bookBtn, cancelBtn;
    IntlPhoneInput phoneInputView;
    Context context = BookJourneyActivity.this;
    AVLoadingIndicatorView avLoadingIndicatorView;
    View loadingView;
    public final int PICK_UP = 1;
    public final int DESTINATION = 2;
    String driverId;
    String taxiModel, currentLat, currentLng, destLat, destLng, fare;
    Calendar myCalendar = Calendar.getInstance();
    Passenger passenger;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_journey);
        initViews();
        getUserData();
        getIntentData();
        initClickLisners();
    }

    @Override
    protected void initViews() {
        pickUp = (MobiTextView) findViewById(R.id.pickup_location);
        destination = (MobiTextView) findViewById(R.id.destination_location);
        fullName = (EditText) findViewById(R.id.user_name_book_journey);
        fullName.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        date = (MobiTextView) findViewById(R.id.date_book_journey);
        time = (MobiTextView) findViewById(R.id.time_book_journey);
        passengers = (EditText) findViewById(R.id.passenger_txt_book_journey);
        passengers.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        payment = (MobiTextView) findViewById(R.id.payment_book_journey);
        bookBtn = (Button) findViewById(R.id.book_btn);
        bookBtn.setTypeface(Typeface.createFromAsset(getAssets(), "clanpro_news.ttf"));
        phoneInputView = (IntlPhoneInput) findViewById(R.id.mobile_number_book_journey);
        loadingView = findViewById(R.id.loading_indicator);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        avLoadingIndicatorView = (AVLoadingIndicatorView) loadingView.findViewById(R.id.avi);
        pickUpLocation = (RelativeLayout) findViewById(R.id.pickup_location_layout);
        destinationLocation = (RelativeLayout) findViewById(R.id.destination_location_layout);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();
        datePickerDialog = new DatePickerDialog(context, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    @Override
    void initClickLisners() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataFilled()) {
                    bookBtn.setEnabled(false);
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    avLoadingIndicatorView.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ApiCalls.bookTaxi(getBooking(), new PaxApiCallListner() {
                                @Override
                                public void onApiRequestResult(JSONObject jsonObject) {
                                    parseBookTaxiResponseNew(jsonObject);
                                }
                            });
                        }
                    }, 1500);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (timeIsInRange(selectedHour, selectedMinute)) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    myCalendar.set(Calendar.MINUTE, selectedMinute);
                    updateTime(myCalendar);
                } else {
                    Toast.makeText(context, R.string.time_range_msg, Toast.LENGTH_LONG).show();
                    utils.vibrate(context, 400);
                    Calendar c = Calendar.getInstance();
                    myCalendar.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
                    myCalendar.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
                    updateTime(myCalendar);
                }
            }
        }, hour, minute, false);
        mTimePicker.show();
    }

    private boolean timeIsInRange(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (myCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && myCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && myCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            if (calendar.get(Calendar.HOUR_OF_DAY) < hour) {
                return true;
            } else if (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) <= minute) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
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
                passenger = new Passenger();
                passenger.setUserId(userData.getString(PaxApiCall.userId));
                passenger.setFirstName(userData.getString(PaxApiCall.firstName));
                passenger.setLastName(userData.getString(PaxApiCall.lastName));
                passenger.setEmail(userData.getString(PaxApiCall.email));
                passenger.setPhone(userData.getString(PaxApiCall.phone));
                passenger.setAddress(userData.getString(PaxApiCall.address));
                passenger.setNote(userData.getString(PaxApiCall.note));
            } else {
                avLoadingIndicatorView.hide();
            }
        } catch (Exception e) {
            avLoadingIndicatorView.hide();
        } finally {
            setPassengerData(passenger);
        }
    }

    void setPassengerData(Passenger passengerData) {
        fullName.setText(passengerData.getFirstName() + " " + passengerData.getLastName());
        phoneInputView.setNumber(passengerData.getPhone());
        setFormData();
        updateDate();
        updateTime(myCalendar);
        avLoadingIndicatorView.hide();
    }

    void setFormData() {
        if (MapActivity.currentLocationString != null && !MapActivity.currentLocationString.equals("")) {
            pickUp.setText(MapActivity.currentLocationString);
            currentLat = MapActivity.currentLocation.latitude + "";
            currentLng = MapActivity.currentLocation.longitude + "";
        }
        if (MapActivity.destinationLocationString != null && !MapActivity.destinationLocationString.equals("")) {
            destination.setText(MapActivity.destinationLocationString);
            destLat = MapActivity.destinationLocation.latitude + "";
            destLng = MapActivity.destinationLocation.longitude + "";
        }
        String paymentMethod = StoragePreference.getPaymentMethod(context);
        payment.setText(paymentMethod);
        getFare();
        passengers.setText("1");
    }

    void getIntentData() {
        Intent intent = getIntent();
        driverId = "1";
        taxiModel = intent.getStringExtra(PaxApiCall.taxiModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String location = data.getStringExtra("location");
            LatLng latLng = new LatLng(data.getDoubleExtra("lat", 0.0), data.getDoubleExtra("lng", 0.0));
            int add = data.getIntExtra("add", 0);
            if (add == 1) {
                if (location != null && !location.equals("")) {
                    pickUp.setText(location);
                    currentLat = latLng.latitude + "";
                    currentLng = latLng.longitude + "";
                }
            } else {
                if (location != null && !location.equals("")) {
                    destination.setText(location);
                    destLat = latLng.latitude + "";
                    destLng = latLng.longitude + "";
                }
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

    // Setting Date
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
            setTime();
        }
    };

    private void updateDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateTime(Calendar calendar) {
        String minute = calendar.get(Calendar.MINUTE) + "";
        String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        time.setText(hour + ":" + minute);
    }

    boolean isDataFilled() {
        if (utils.validInput(passengers)) {
            return true;
        } else {
            updateUI(bookBtn, "Enter Passengers");
            return false;
        }
    }

    Booking getBooking() {
        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Booking booking = new Booking();
        booking.setPassengerId(passenger.getUserId());
        booking.setFirstName(passenger.getFirstName());
        booking.setCurrentLocation(pickUp.getText().toString());
        booking.setDropLocation(destination.getText().toString());
        booking.setPassengerCount(Integer.parseInt(passengers.getText().toString()));
        booking.setPickUpDate(sdf.format(myCalendar.getTime()));
        booking.setTaxiModel(Integer.parseInt(taxiModel));
        booking.setPickUpLatitude(currentLat);
        booking.setPickUpLongitude(currentLng);
        booking.setDropLatitude(destLat);
        booking.setDropLongitude(destLng);
        booking.setPaymentMethod(MapActivity.paymentMethod);
        booking.setTotalFare(fare);
        Log.d("Booking", booking.toString());
        return booking;
    }

    void parseBookTaxiResponseNew(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                String message = response.getString(PaxApiCall.message);
                updateUI(bookBtn, message);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                updateUI(bookBtn, msg);
            }
        } catch (Exception e) {
            Log.d("Reponse", e.toString());
        } finally {
            bookBtn.setEnabled(true);
            avLoadingIndicatorView.hide();
        }
    }

    void getFare() {
        LatLng pickUplatLng = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLng));
        LatLng drop = new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLng));
        ApiCalls.calculateFare(StoragePreference.getUserId(context), pickUp.getText().toString(), destination.getText().toString(), Integer.parseInt(taxiModel), pickUplatLng, drop, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(PaxApiCall.status);
                    if (status == 1) {
                        JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                        Log.d("Fare response", jsonObject.toString());
                        fare = response.getString(PaxApiCall.totalFare);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUI(final Button button, String label) {
        final String previousLabel = button.getText().toString();
        button.setText(label);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setText(previousLabel);
            }
        }, 1500);
    }
}

package com.apps.cabpoint.cabigate.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.MapActivity;
import com.apps.cabpoint.cabigate.map.MyLocationListner;
import com.apps.cabpoint.cabigate.models.Booking;
import com.apps.cabpoint.cabigate.models.Fare;
import com.apps.cabpoint.cabigate.models.Vehicle;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.ui.BookJourneyActivity;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Abdul Ghani on 6/6/2017.
 */

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarListViewHolder> implements MyLocationListner {

    private ArrayList<Vehicle> cars = null;
    private MobiTextView type, eta, fareEstimate, maxPassenger, maxLuggage;
    private MobiTextView requestBtn;
    private ImageView rideLater;
    private int selectedItem = 0;
    private boolean isFirstTime;
    private MapActivity context;
    private Fare fare;

    @Override
    public void onLocationChanged() {
        setFare(context, Integer.parseInt(cars.get(selectedItem).getModel()));
    }

    static class CarListViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        MobiTextView carName;

        CarListViewHolder(View itemView) {
            super(itemView);
            carImage = (ImageView) itemView.findViewById(R.id.car_image_fab);
            carName = (MobiTextView) itemView.findViewById(R.id.car_name_text);
        }
    }

    public CarListAdapter(ArrayList<Vehicle> carList, MobiTextView type, MobiTextView eta, MobiTextView fareEstimate, MobiTextView requestBtn, ImageView rideLater, MobiTextView maxPassenger, MobiTextView maxLuggage) {
        cars = carList;
        this.type = type;
        this.eta = eta;
        this.fareEstimate = fareEstimate;
        this.requestBtn = requestBtn;
        this.rideLater = rideLater;
        this.maxPassenger = maxPassenger;
        this.maxLuggage = maxLuggage;
        isFirstTime = true;
    }

    @Override
    public CarListAdapter.CarListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_car_view, parent, false);
        context = (MapActivity) v.getContext();
        return new CarListAdapter.CarListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CarListAdapter.CarListViewHolder holder, int position) {
        Picasso.with(context).load(cars.get(position).getIcon()).into(holder.carImage);
        holder.carName.setText(cars.get(position).getType());
        holder.itemView.setSelected(selectedItem == position);
        if (isFirstTime) {
            clickFunction(context, position);
            isFirstTime = false;
        }

        holder.carImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFunction(context, holder.getAdapterPosition());
            }
        });
    }

    private void clickFunction(final Context context, final int position) {
        changeOnMapActivity(context, position);
        if (!isFirstTime) {
            notifyItemChanged(selectedItem);
            selectedItem = position;
            notifyItemChanged(selectedItem);
        }
        type.setText(cars.get(position).getType());
        setFare(context, Integer.parseInt(cars.get(position).getModel()));
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fare != null) {
                    if (fare.getAvailableStatus() == 1) {
                        checkPayment(position);
                    } else {
                        Toast.makeText(context,fare.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(context, "Not Available/Try Later", Toast.LENGTH_LONG).show();
                }

            }
        });
        rideLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preBooking = new Intent(context, BookJourneyActivity.class);
                preBooking.putExtra(PaxApiCall.taxiModel, cars.get(position).getModel());
                context.startActivity(preBooking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    private void changeOnMapActivity(Context context, int position) {
        ((MapActivity) context).bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ((MapActivity) context).getCars.setTaxiModel(cars.get(position).getModel());
        ((MapActivity) context).getCars.setCancel(true);
        ((MapActivity) context).getCars.getNearestDrivers();
    }

    private void setFare(Context context, int taxiModel) {
        ApiCalls.calculateFare(StoragePreference.getUserId(context), MapActivity.currentLocationString, MapActivity.destinationLocationString, taxiModel, MapActivity.currentLocation, MapActivity.destinationLocation, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                if (jsonObject != null) {
                    fare = PaxApiCall.parseFareResponse(jsonObject);
                    if (fare != null) {
                        fareEstimate.setText(fare.getFare());
                        maxLuggage.setText(fare.getMaxLuggage());
                        maxPassenger.setText(fare.getMaxPassenger());
                        eta.setText(fare.getDuration());
                    }
                }
            }
        });
    }

    private Booking getBookingParams(Context context, int position) {
        String myFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();

        Booking booking = new Booking();
        booking.setPassengerId(StoragePreference.getUserId(context));
        booking.setFirstName(StoragePreference.getUserName(context));
        booking.setCurrentLocation(MapActivity.currentLocationString);
        booking.setDropLocation(MapActivity.destinationLocationString);
        booking.setPassengerCount(4);
        booking.setPickUpDate(sdf.format(calendar.getTime()));
        booking.setTaxiModel(Integer.parseInt(cars.get(position).getModel()));
        booking.setPickUpLatitude(MapActivity.currentLocation.latitude + "");
        booking.setPickUpLongitude(MapActivity.currentLocation.longitude + "");
        booking.setDropLatitude(MapActivity.destinationLocation.latitude + "");
        booking.setDropLongitude(MapActivity.destinationLocation.longitude + "");
        booking.setPaymentMethod(MapActivity.paymentMethod);
        booking.setTotalFare(fareEstimate.getText().toString());
        booking.setPayPalTransId(((MapActivity) context).transId);
        return booking;
    }

    private void parseBookTaxiResponseNew(JSONObject jsonObject) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                context.isRequestRideClicked = true;
                context.getCars.setCancel(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.avLoadingIndicatorView.hide();
                        context.requestLayoutView.setVisibility(View.GONE);
                        context.requestRide.setVisibility(View.GONE);
                        requestBtn.setEnabled(false);
                        rideLater.setEnabled(false);
                        utils.enableDisableView(context.topPanelView, false);
                    }
                }, 4000);
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                context.avLoadingIndicatorView.hide();
                context.requestLayoutView.setVisibility(View.GONE);
                requestBtn.setText(context.getString(R.string.request_ride));
                rideLater.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            context.avLoadingIndicatorView.hide();
            context.requestLayoutView.setVisibility(View.GONE);
            requestBtn.setText(context.getString(R.string.request_ride));
            rideLater.setVisibility(View.VISIBLE);
            Log.d("Reponse", e.toString());
        }
    }

    public void initRequestLayout(final int position) {
        final MobiTextView reqCurrent, reqDestination;
        final Button cancelBtn;
        reqCurrent = (MobiTextView) context.requestLayoutView.findViewById(R.id.request_wating_from_text);
        reqDestination = (MobiTextView) context.requestLayoutView.findViewById(R.id.request_wating_to_txt);
        cancelBtn = (Button) context.requestLayoutView.findViewById(R.id.cancel_request_btn);
        reqCurrent.setText(MapActivity.currentLocationString);
        reqDestination.setText(MapActivity.destinationLocationString);
        cancelBtn.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                cancelBtn.setVisibility(View.GONE);
                ApiCalls.bookTaxi(getBookingParams(context, position), new PaxApiCallListner() {
                    @Override
                    public void onApiRequestResult(JSONObject jsonObject) {
                        parseBookTaxiResponseNew(jsonObject);
                    }
                });
            }
        };
        handler.postDelayed(r, 3500);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBtn.setVisibility(View.GONE);
                context.avLoadingIndicatorView.hide();
                context.requestLayoutView.setVisibility(View.GONE);
                context.isPaypalPaid = false;
                handler.removeCallbacks(r);
                requestBtn.setText(context.getString(R.string.request_ride));
                rideLater.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestRide(int position) {
        context.avLoadingIndicatorView.show();
        context.requestLayoutView.setVisibility(View.VISIBLE);
        requestBtn.setText("Connecting Driver...");
        rideLater.setVisibility(View.GONE);
        initRequestLayout(position);
    }

    private void checkPayment(final int position) {
        MapActivity.paymentMethod = StoragePreference.getPaymentMethod(context);
        requestRide(position);
    }
}

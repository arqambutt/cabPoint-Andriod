package com.apps.cabpoint.cabigate.map;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.adapter.CarListAdapter;
import com.apps.cabpoint.cabigate.adapter.LocationAdapter;
import com.apps.cabpoint.cabigate.dialog.CancelBookingDialog;
import com.apps.cabpoint.cabigate.dialog.ContactDriverDialog;
import com.apps.cabpoint.cabigate.dialog.ContactUsDialog;
import com.apps.cabpoint.cabigate.dialog.DialogListner;
import com.apps.cabpoint.cabigate.maprecyclerview.MapListActivity;
import com.apps.cabpoint.cabigate.models.Driver;
import com.apps.cabpoint.cabigate.models.LocationHistory;
import com.apps.cabpoint.cabigate.models.NearestDrivers;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.models.PassengerNotification;
import com.apps.cabpoint.cabigate.models.PaymentInfo;
import com.apps.cabpoint.cabigate.models.RideHistory;
import com.apps.cabpoint.cabigate.models.Vehicle;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.ui.DriverRatingActivity;
import com.apps.cabpoint.cabigate.ui.FreeRideActivity;
import com.apps.cabpoint.cabigate.ui.LatestPromotionsActivity;
import com.apps.cabpoint.cabigate.ui.LoginActivity;
import com.apps.cabpoint.cabigate.ui.PaymentActivity;
import com.apps.cabpoint.cabigate.ui.SettingActivity;
import com.apps.cabpoint.cabigate.utils.CircleTransform;
import com.apps.cabpoint.cabigate.utils.MenuAnimation;
import com.apps.cabpoint.cabigate.utils.SocketService;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.utils.utils;
import com.apps.cabpoint.cabigate.views.MobiButton;
import com.apps.cabpoint.cabigate.views.MobiTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.apps.cabpoint.cabigate.R.id.map;
import static com.apps.cabpoint.cabigate.R.id.topPanel;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, SocketDataListner {
    public GoogleMap gMap;
    LocationRequest mLocationRequest;
    public Marker currLocationMarker;
    Marker destinationMarker;
    GoogleApiClient mGoogleApiClient;
    public static LatLng currentLocation;
    public static LatLng destinationLocation;
    JSONObject jsonObjectResponse;
    PlaceAutocompleteFragment autocompleteFragment;
    Context context = MapActivity.this;
    View mapView;
    Circle circle;
    public Location myLocation;
    ImageView navigationBtn, currentLocationBtn, menuIconWhite;
    public MobiButton requestRide;
    public RelativeLayout placeLayout;
    RelativeLayout carBottomSheet, driverBottomSheet;
    public Boolean placeLayoutVisibility, isPickUPLocation, isRestart;
    public Boolean isRequestRideClicked, isTrackDriver;
    public static String currentLocationString, destinationLocationString, paymentMethod;
    public static String fare, paymentMethodString;
    public String duration, transId;
    ArrayList<DrawCarOnMap> carOnMapArrayList;
    private Boolean isDriverTracked, isRequestSent;
    DrawerLayout drawer;
    NavigationView navigationView;
    public BottomSheetBehavior bottomSheetBehavior;
    NestedScrollView bottomSheetScroll;
    public ArrayList<NearestDrivers> populateCars;
    public GetCars getCars;
    public GetTrackDirver getTrackDirver;
    public AVLoadingIndicatorView avLoadingIndicatorView;
    View loadingView, internetError, firstView;
    public View topPanelView;
    public View requestLayoutView;
    private MobiTextView topPanelCurrentLoc, topPanelDestinationLoc, requestBtn;
    private ImageView topPanelMenu, topPanelEarth, topPanelMarker, topPanelMenuBelow, topPanelEarthBelow, topPanelMarkerBelow, locationSelector, backBtnSearch;
    private LinearLayout topPanelHomeAndWork, homeLocation, workLocation;
    private PassengerNotification passengerNotification;
    private MenuAnimation menuAnimation, menuAnimationBelow;
    private boolean isMenuClicked, isMenuClickedBelow;
    public boolean isPaypalPaid;
    private List<RideHistory> rideHistories;
    private static final int PROFILE_CHOSER_REQUEST_CODE = 1;
    private static final int PROFILE_PERMISSION_REQUEST_CODE = 10;
    private static final int SETTING_REQUEST = 3;
    private static final int PAYPAL_RESULT = 420;
    private static final int RIDE_AGAIN = 11;
    boolean isRefreshRequestSent = false;
    //nav bar objects
    MobiTextView userName, locationNav;
    public ImageView backBtnNav, profilePic;
    RelativeLayout payment, history, signOut, freeRides, promotion, setting, contactUs;
    MobiTextView termsAndConditions;
    DrawPolyLine drawPolyLine;
    ArrayList<Vehicle> driversArrayList;
    private CarListAdapter carListAdapter;
    public static final String TAG = MapActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        jsonObjectResponse = null;
        initViews();
        initNavViews();
        setOnClickListners();
        checkPermissions();
        checkGpsAndInternet(true);
        registerReceivers();
    }

    private void checkGpsAndInternet(boolean request) {
        if (!utils.getActiveInternet(context)) {
            onWifiDisconnected();
            hideFirstLayout();
        } else if (!utils.isGPSEnable(context)) {
            onGpsDisconnected();
            hideFirstLayout();
        } else {
            if (request)
                sendTokenToServer();
        }
    }

    void initViews() {
        transId = "0";
        rideHistories = new ArrayList<>();
        placeLayoutVisibility = isPickUPLocation = isPaypalPaid = isRestart = isMenuClicked = isMenuClickedBelow = isTrackDriver = isRequestRideClicked = isRequestSent = isDriverTracked = false;
        populateCars = null;
        driversArrayList = null;
        carOnMapArrayList = null;
        drawPolyLine = null;
        myLocation = null;
        fare = duration = paymentMethodString = currentLocationString = destinationLocationString = null;
        navigationBtn = (ImageView) findViewById(R.id.navigation_button);
        currentLocationBtn = (ImageView) findViewById(R.id.current_location_btn);
        requestRide = (MobiButton) findViewById(R.id.ride_now_btn);
        placeLayout = (RelativeLayout) findViewById(R.id.place_api_layout);
//        topPanel = (RelativeLayout) findViewById(R.id.topPanel);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_map);
        navigationView = (NavigationView) findViewById(R.id.nav_view_map);
        loadingView = findViewById(R.id.loading_indicator);
        internetError = findViewById(R.id.internet_error_layout);
        firstView = findViewById(R.id.first_layout);
        menuIconWhite = (ImageView) internetError.findViewById(R.id.nav_menu);
        requestLayoutView = findViewById(R.id.request_layout);
        avLoadingIndicatorView = (AVLoadingIndicatorView) loadingView.findViewById(R.id.avi);
        placeLayout.setVisibility(View.INVISIBLE);
        topPanelView = findViewById(R.id.top_panel_view);
        topPanelCurrentLoc = (MobiTextView) topPanelView.findViewById(R.id.txt_location);
        topPanelDestinationLoc = (MobiTextView) topPanelView.findViewById(R.id.txt_destination);
        topPanelMenu = (ImageView) topPanelView.findViewById(R.id.img_menu_icon);
        topPanelEarth = (ImageView) topPanelView.findViewById(R.id.img_earth_icon);
        topPanelMarker = (ImageView) topPanelView.findViewById(R.id.img_marker_icon);
        topPanelMenuBelow = (ImageView) topPanelView.findViewById(R.id.img_menu_icon_below);
        topPanelEarthBelow = (ImageView) topPanelView.findViewById(R.id.img_earth_icon_below);
        topPanelMarkerBelow = (ImageView) topPanelView.findViewById(R.id.img_marker_icon_below);
        topPanelHomeAndWork = (LinearLayout) topPanelView.findViewById(R.id.home_and_work_layout);
        homeLocation = (LinearLayout) topPanelView.findViewById(R.id.below_home_layout);
        workLocation = (LinearLayout) topPanelView.findViewById(R.id.below_work_layout);
        locationSelector = (ImageView) findViewById(R.id.location_selector_marker);
        menuAnimation = new MenuAnimation(context, topPanelMenu, topPanelEarth, topPanelMarker, topPanelCurrentLoc);
        menuAnimationBelow = new MenuAnimation(context, topPanelMenuBelow, topPanelEarthBelow, topPanelMarkerBelow, topPanelDestinationLoc);
        paymentMethod = StoragePreference.getPaymentMethod(context);
        backBtnSearch = (ImageView) findViewById(R.id.back_btn_search);
        utils.enableDisableView(topPanelView, false);
        initBottomSheet();
    }

    void initNavViews() {
        userName = (MobiTextView) findViewById(R.id.user_name_nav);
        locationNav = (MobiTextView) findViewById(R.id.location_nav);
        locationNav.setSelected(true);
        locationNav.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        locationNav.setMarqueeRepeatLimit(-1);
        locationNav.setSingleLine(true);
        backBtnNav = (ImageView) findViewById(R.id.back_arrow_nav);
        profilePic = (ImageView) findViewById(R.id.profile_pic_nav);
        payment = (RelativeLayout) findViewById(R.id.payment_view);
        history = (RelativeLayout) findViewById(R.id.history_view);
        freeRides = (RelativeLayout) findViewById(R.id.free_ride_view);
        promotion = (RelativeLayout) findViewById(R.id.promotions_view);
        setting = (RelativeLayout) findViewById(R.id.setting_view);
        signOut = (RelativeLayout) findViewById(R.id.sign_out_view);
        termsAndConditions = (MobiTextView) findViewById(R.id.terms_conditions);
        contactUs = (RelativeLayout) findViewById(R.id.contact_us_view);
        setOnclickListnerNav();
    }

    void setOnclickListnerNav() {
        backBtnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PaymentActivity.class));
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(context, MapListActivity.class);
                startActivityForResult(historyIntent, RIDE_AGAIN);
            }
        });
        promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LatestPromotionsActivity.class));
            }
        });
        freeRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FreeRideActivity.class));
            }
        });
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ContactUsDialog(context).show();
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(context, SettingActivity.class);
                settingIntent.putExtra(PaxApiCall.firstName, userName.getText().toString());
                settingIntent.putExtra("location", currentLocationString);
                startActivityForResult(settingIntent, SETTING_REQUEST);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.getActiveInternet(context)) {
                    logOutProcedure(context);
                } else {
                    Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        // current destination views
        topPanelMenu.setOnClickListener(new View.OnClickListener() {
            //            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!isMenuClicked) {
                    isMenuClicked = true;
                    menuAnimation.animationIn();
                    if (isMenuClickedBelow) {
                        topPanelMenuBelow.performClick();
                    }
                } else {
                    isMenuClicked = false;
                    menuAnimation.animationOut();
                }
            }
        });
        topPanelMenuBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMenuClickedBelow) {
                    isMenuClickedBelow = true;
                    menuAnimationBelow.animationIn();
                    if (isMenuClicked) {
                        topPanelMenu.performClick();
                    }
                } else {
                    isMenuClickedBelow = false;
                    menuAnimationBelow.animationOut();
                }
            }
        });
        topPanelCurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickUPLocation = true;
                showPlaceFragment();
            }
        });
        topPanelDestinationLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickUPLocation = false;
                showPlaceFragment();
            }
        });
        topPanelMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(topPanelHomeAndWork);
            }
        });
        topPanelMarkerBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(topPanelHomeAndWork);
            }
        });
        topPanelEarth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestRide.getText().equals(getString(R.string.ride_now))) {
                    requestRide.setText(R.string.confirm_location);
                } else {
                    requestRide.setText(R.string.ride_now);
                }
                isPickUPLocation = true;
                toggleView(locationSelector);
            }
        });
        topPanelEarthBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestRide.getText().equals(getString(R.string.ride_now))) {
                    requestRide.setText(R.string.confirm_location);
                } else {
                    requestRide.setText(R.string.ride_now);
                }
                isPickUPLocation = false;
                toggleView(locationSelector);
            }
        });
        homeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuClicked) {
                    String location = StoragePreference.getHomeLocation(context);
                    LatLng latLng = StoragePreference.getHomeLatLng(context);
                    if (latLng != null) {
                        setCurrentMarker(latLng, location);
                    } else {
                        startActivity(new Intent(context, SettingActivity.class));
                    }
                    topPanelMenu.performClick();
                } else {
                    String location = StoragePreference.getHomeLocation(context);
                    LatLng latLng = StoragePreference.getHomeLatLng(context);
                    if (latLng != null) {
                        setDestinationMarker(latLng, location, true);
                    } else {
                        startActivity(new Intent(context, SettingActivity.class));
                    }
                    topPanelMenuBelow.performClick();
                }
                toggleView(topPanelHomeAndWork);

            }
        });
        workLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMenuClicked) {
                    String location = StoragePreference.getWorkLocation(context);
                    LatLng latLng = StoragePreference.getWorkLatLng(context);
                    if (latLng != null) {
                        setCurrentMarker(latLng, location);
                    } else {
                        startActivity(new Intent(context, SettingActivity.class));
                    }
                    topPanelMenu.performClick();
                } else {
                    String location = StoragePreference.getWorkLocation(context);
                    LatLng latLng = StoragePreference.getWorkLatLng(context);
                    if (latLng != null) {
                        setDestinationMarker(latLng, location, true);
                    } else {
                        startActivity(new Intent(context, SettingActivity.class));
                    }
                    topPanelMenuBelow.performClick();
                }
                toggleView(topPanelHomeAndWork);
            }
        });
        backBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeLayout.setVisibility(View.INVISIBLE);
                placeLayoutVisibility = false;
            }
        });
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                locationNav.setText(currentLocationString);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void initBottomSheet() {
        bottomSheetScroll = (NestedScrollView) findViewById(R.id.bottomsheetSV);
        carBottomSheet = (RelativeLayout) findViewById(R.id.car_bottom_sheet);
        driverBottomSheet = (RelativeLayout) findViewById(R.id.driver_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetScroll);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(getResources().getDimensionPixelOffset(R.dimen._40sdp));
        bottomSheetScroll.setVisibility(View.INVISIBLE);
    }

    private void updateUI(final Button view, final String label, String msg) {
        view.setText(msg.toUpperCase());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setText(label);
            }
        }, 5000);

    }

    void setOnClickListners() {
        requestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestRide.getText().equals(getString(R.string.ride_now))) {
                    if (currentLocationString != null && destinationLocationString != null) {
                        requestRide();
//                        if (GetNearestDrivers.driversArrayList != null) {
//                            requestRide();
//                        } else {
//                            Toast.makeText(context, R.string.no_rides_error, Toast.LENGTH_LONG).show();
//                        }
                    } else {
                        if (destinationLocation == null) {
                            isPickUPLocation = false;
                            showPlaceFragment();
                        } else if (currentLocation == null) {
                            isPickUPLocation = true;
                            showPlaceFragment();
                        }
                    }
                } else {
                    if (requestRide.getText().equals(getString(R.string.confirm_location))) {
                        setLocationSelector();
                    }
                }
            }
        });
        navigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
                if (userName.getText().toString().equals("Name")) {
                    setPreSavedUser();
                }
            }
        });
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setPeekHeight(getResources().getDimensionPixelOffset(R.dimen._40sdp));
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        currentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });
        menuIconWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationBtn.performClick();
            }
        });
    }

    void showPlaceFragment() {
        placeLayoutVisibility = true;
        placeLayout.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.suggestions_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        LocationAdapter locationAdapter = new LocationAdapter(getUniqueLocations(getLocations()));
        recyclerView.setAdapter(locationAdapter);
    }

    private void getMyLocation() {
        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            gMap.animateCamera(cameraUpdate);
        } else {
            if (currentLocation != null) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 18);
                gMap.animateCamera(cameraUpdate);
            }
        }
    }

    void initGooglePlaceApi() {
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        //restrictAutoCompleteSetCountryBounds(autocompleteFragment,"PK");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                String address = place.getAddress().toString();
                setLocation(address, latLng);
            }

            @Override
            public void onError(Status status) {
                Log.i(getLocalClassName(), "An error occurred: " + status);
            }
        });
    }

    void drawPolyLine() {
        if (currentLocation != null && destinationLocation != null) {
            drawPolyLine = new DrawPolyLine(context, gMap, currentLocation, destinationLocation);
            drawPolyLine.drawPolyLineToMap();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        buildGoogleApiClient();
    }


    void initMap() {
        currentLocation = null;
        destinationLocation = null;
        destinationMarker = null;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && permissions.length > 0 && grantResults.length > 0) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                utils.showInfoDialog(context, "Permissions Denied", false);
                finish();
            } else {
                initMap();
                initGooglePlaceApi();
            }
        }
        if (requestCode == PROFILE_PERMISSION_REQUEST_CODE && permissions.length > 0 && grantResults.length > 0) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                utils.showInfoDialog(context, "Permissions Denied", false);
            } else {
                utils.selectImage(MapActivity.this, PROFILE_CHOSER_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setTiltGesturesEnabled(false);
        gMap.getUiSettings().setRotateGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        if (!isRestart) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            myLocation = mLastLocation;
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (StoragePreference.getSavedInstance(context).equals(StoragePreference.notAvailable)) {
                setCurrentMarker(latLng, null);
            }
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        setCurrentMarker(latLng, null);
    }

    void setCurrentMarker(LatLng current, String address) {

        if (!utils.getActiveInternet(context)) {
            currentLocation = current;
            return;
        }
        if (isPickUPLocation) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        if (currLocationMarker != null) {
            currLocationMarker.remove();
            currentLocation = null;
            currentLocationString = null;
            destinationLocationString = null;

            if (destinationMarker != null) {
                destinationMarker.remove();
                destinationMarker = null;
                destinationLocation = null;
                if (drawPolyLine != null) {
                    drawPolyLine.removePolyLine();
                }
            }

            if (circle != null) {
                circle.remove();
                circle = null;
            }
        }
        currentLocation = current;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(current);
        markerOptions.title("You");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        currLocationMarker = gMap.addMarker(markerOptions);
        drawAccuracyCircle(current);
        setCamera();
        if (address != null) {
            currentLocationString = address;
        } else {
            currentLocationString = getAddress(current.latitude, current.longitude, 0);
        }
        topPanelCurrentLoc.setText(currentLocationString); //this line caused current loc error wtf is this u don't use static variables like this
        topPanelDestinationLoc.setText(getString(R.string.destination_location));
        if (!isRequestSent) {
            sendTokenToServer();
        }
        hideFirstLayout();
    }

    void setDestinationMarker(LatLng destination, String address, Boolean willDrawPolyLine) {
        if (isAddressSame(destination)) {
            Toast.makeText(context, "Choose different locations", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        if (destinationMarker != null) {
            destinationMarker.remove();
            destinationMarker = null;
            if (drawPolyLine != null) {
                drawPolyLine.removePolyLine();
            }
        }
        destinationLocation = destination;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(destination);
        markerOptions.title("Destination");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        destinationMarker = gMap.addMarker(markerOptions);
        setCamera();
        if (address != null) {
            destinationLocationString = address;
        } else {
            destinationLocationString = getAddress(destination.latitude, destination.longitude, 0);
        }
        if (carListAdapter != null) {
            carListAdapter.onLocationChanged();
        }
        topPanelDestinationLoc.setText(destinationLocationString);
        if (willDrawPolyLine) {
            drawPolyLine();
        }
    }

    void setCamera() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        ArrayList<Marker> markers = new ArrayList<>();
        float expandBound;
        if (currLocationMarker != null) {
            markers.add(currLocationMarker);
        }
        if (destinationMarker != null) {
            markers.add(destinationMarker);
            expandBound = 0.060f;
        } else {
            expandBound = 0.010f;
        }
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        if (markers.size() > 1) {
            LatLngBounds bounds = builder.build();
            LatLng center = bounds.getCenter();
            builder.include(new LatLng(center.latitude, center.longitude));
            builder.include(new LatLng(center.latitude, center.longitude));
            bounds = builder.build();
            int padding = getResources().getDimensionPixelOffset(R.dimen._100sdp); // offset from edges of the map in pixels
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            gMap.animateCamera(cameraUpdate);
        } else {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 16);
            gMap.animateCamera(cameraUpdate);
        }
    }

    private String getAddress(double latitude, double longitude, int count) {
        if (!utils.getActiveInternet(context)) {
            return "";
        }
        String data = "";
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            data = addresses.get(0).getAddressLine(0);
        } else {
            if (count < 2) {
                count++;
                getAddress(latitude, longitude, count);
            }
        }
        return data;
    }


    void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

            } else {
                utils.showInfoDialog(context, "Location Permissions Required", false);
            }
        } else {
            initMap();
            initGooglePlaceApi();
        }
    }

    void drawAccuracyCircle(LatLng latLng) {
        circle = gMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(getResources().getDimensionPixelOffset(R.dimen._10sdp))
                .strokeColor(0x996CE1E3)
                .strokeWidth(getResources().getDimensionPixelOffset(R.dimen._2sdp)));
    }

    void logOutProcedure(final Context context) {
        PaxApiCallListner paxApiCallListner = new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(PaxApiCall.status);
                    if (status == 1) {
                        StoragePreference.clearUserData(context);
                        Intent logoutIntent = new Intent(context, LoginActivity.class);
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logoutIntent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ApiCalls.logout(StoragePreference.getUserId(context), StoragePreference.getTOKEN(context), paxApiCallListner);
    }

    void setCarListInBottomSheet() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_list_recycler_view);
        MobiTextView type, eta, fareEstimate, capacity, laguage;
        final ImageView clock, topBtn;
        requestBtn = (MobiTextView) findViewById(R.id.txt_ride);
        type = (MobiTextView) findViewById(R.id.txt_family);
        eta = (MobiTextView) findViewById(R.id.txt_mins);
        fareEstimate = (MobiTextView) findViewById(R.id.txt_pound);
        capacity = (MobiTextView) findViewById(R.id.txt_paint);
        laguage = (MobiTextView) findViewById(R.id.txt_travel);
        clock = (ImageView) findViewById(R.id.clock);
        topBtn = (ImageView) findViewById(R.id.bottom_sheet_top_btn);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    topBtn.setRotation(180);
                } else {
                    topBtn.setRotation(0);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        carListAdapter = new CarListAdapter(driversArrayList, type, eta, fareEstimate, requestBtn, clock, capacity, laguage);
        recyclerView.setAdapter(carListAdapter);
        requestRide.setVisibility(View.GONE);
    }

    public void initDriverBottomSheetViews(final PassengerNotification notification) {
        if (notification.getStatus().equals(PaxApiCall.callOut) || notification.getStatus().equals(PaxApiCall.passengerOnBoard)
                || notification.getStatus().equals(PaxApiCall.wating)) {
            if(topPanelView.getVisibility()==View.VISIBLE){
                topPanelView.setVisibility(View.GONE);
            }
        }
        if (carBottomSheet.getVisibility() == View.VISIBLE) {
            carBottomSheet.setVisibility(View.GONE);
            driverBottomSheet.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.hide();
            requestLayoutView.setVisibility(View.GONE);
        }
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        ImageView driverImage, carImage;
        final MobiTextView driverName, carName, driverRating, carModel, status, cancelTrip, contactBtn;
        driverImage = (ImageView) driverBottomSheet.findViewById(R.id.img_passenger);
        carImage = (ImageView) driverBottomSheet.findViewById(R.id.img_ride_car);
        driverName = (MobiTextView) driverBottomSheet.findViewById(R.id.txt_user_name);
        carName = (MobiTextView) driverBottomSheet.findViewById(R.id.txt_car_name);
        driverRating = (MobiTextView) driverBottomSheet.findViewById(R.id.txt_rating);
        carModel = (MobiTextView) driverBottomSheet.findViewById(R.id.txt_car_model);
        status = (MobiTextView) driverBottomSheet.findViewById(R.id.txt_ride_status);
        cancelTrip = (MobiTextView) driverBottomSheet.findViewById(R.id.cancel_new);
        contactBtn = (MobiTextView) driverBottomSheet.findViewById(R.id.contact_btn_new);
        Picasso.with(context).load(notification.getDriverImage()).error(R.drawable.user_image).transform(new CircleTransform()).into(driverImage);
        Picasso.with(context).load(notification.getVehicleImage()).error(R.drawable.ic_history_black_36dp).transform(new CircleTransform()).into(carImage);
        driverName.setText(notification.getDriverName());
        carName.setText(notification.getVehicleName());
        driverRating.setText(notification.getDriverRating() + "");
        carModel.setText(notification.getVehicleModel());
        status.setText(notification.getStatus());
        cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelBookingDialog cancelBookingDialog = new CancelBookingDialog(context, notification.getJobId(), new DialogListner() {
                    @Override
                    public void getDialogValue(String value) {
                        if (value.equals("Cancel")) {
                            PassengerNotification passengerNotification = new PassengerNotification(context);
                            passengerNotification.setStatus(PaxApiCall.cancel);
                            onRideCompleted(passengerNotification);
                        }
                    }
                });
                cancelBookingDialog.show();
            }
        });
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Driver driver = new Driver();
                driver.setPhone(notification.getDriverContact());
                driver.setDriverId(notification.getDriverId());
                ContactDriverDialog contactDriverDialog = new ContactDriverDialog(context, driver);
                contactDriverDialog.show();
            }
        });
        if (notification.getStatus().equals("complete") || notification.getStatus().equals("cancel")) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    driverBottomSheet.setVisibility(View.GONE);
                    carBottomSheet.setVisibility(View.GONE);
                    onRideCompleted(notification);
                }
            }, 2000);
        }
    }

    void sendTokenToServer() {
        isRequestSent = true;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ApiCalls.sendDeviceToken(StoragePreference.getUserId(this), refreshedToken, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                Log.d(TAG, "Refreshed token Response: " + jsonObject);
                startSocketService();
                setUserProfileImage();
            }
        });
    }

    void getVehiclesList(final LatLng currentLocation) {
        if (currentLocation != null) {
            ApiCalls.vehiclesList(currentLocation.latitude + "", currentLocation.longitude + "", new PaxApiCallListner() {
                @Override
                public void onApiRequestResult(JSONObject jsonObject) {
                    driversArrayList = PaxApiCall.parseVehicleListResponse(jsonObject);
                    if (driversArrayList == null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getVehiclesList(currentLocation);
                            }
                        }, 500);
                    } else {
                        getNearCars();
                        hideFirstLayout();
                    }
                }
            });
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getVehiclesList(currentLocation);
                }
            }, 1000);
        }
    }

    void getNearCars() {
        if (getCars == null) {
            if (!isDriverTracked) {
                getCars = new GetCars(this, currentLocation, gMap);
                getCars.getNearestDrivers();
            }
        }
    }

    void startSocketService() {
        startService(new Intent(getBaseContext(), SocketService.class));
        SocketService.setSocketListener(this);
    }

    public void showOnlyAvailabeCars() {
        Set<String> availabeTaxiModels = new HashSet<>();
        for (NearestDrivers nearestDrivers : populateCars) {
            availabeTaxiModels.add(nearestDrivers.getTaxiModel());
        }
        ArrayList<String> uniqueModels = new ArrayList<>(availabeTaxiModels);
        ArrayList<Vehicle> uniqueVehicles = new ArrayList<>();
        for (String model : uniqueModels) {
            for (int i = 0; i < driversArrayList.size(); i++) {
                if (driversArrayList.get(i).getModel().equals(model)) {
                    uniqueVehicles.add(driversArrayList.get(i));
                    break;
                }
            }
        }
        driversArrayList = uniqueVehicles;
    }

    void toggleView(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
            return;
        } else if (placeLayout.getVisibility() == View.VISIBLE) {
            placeLayout.setVisibility(View.INVISIBLE);
//            placeLayout.removeView(findViewById(R.id.place_autocomplete_fragment));
            return;
        } else if (bottomSheetScroll.getVisibility() == View.VISIBLE) {
            toggleView(bottomSheetScroll);
            toggleView(requestRide);
            return;
        } else {
            super.onBackPressed();
        }
    }

    public void onRideCompleted(PassengerNotification notification) {
        topPanelView.setVisibility(View.VISIBLE);
        if (getTrackDirver != null) {
            getTrackDirver.setCancel(true);
            getCars = null;
        }
        if (notification.getStatus().equals(PaxApiCall.cancel)) {
            restartActivity();
        }

        if (notification.getStatus().equals(PaxApiCall.complete)) {
            Intent intent = new Intent(context, DriverRatingActivity.class);
            intent.putExtra("image", notification.getDriverImage());
            intent.putExtra("name", notification.getDriverName());
            intent.putExtra("jobid", notification.getJobId());
            intent.putExtra("pid", notification.getPassengerId());
            startActivity(intent);
            finish();
        }
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void socketPassengerNotificationUpdate(Object... args) {
        Gson gson = new Gson();
        String json = gson.toJson(args);
        passengerNotification = PassengerNotification.parseJsonFromString(context, json);
        if (passengerNotification.getStatus().equals("connecting")) {
            return;
        }
        if (!isDriverTracked) {
            isDriverTracked = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getCars != null) {
                        getCars.setCancel(true);
                    }
                    getTrackDirver = new GetTrackDirver(MapActivity.this, gMap, currentLocation, passengerNotification.getDriverId(), passengerNotification.getPassengerId(), passengerNotification.getVehicleImage());
                    getTrackDirver.getDriver();
                    isTrackDriver = true;
                    changeDataOnPassengerState(passengerNotification);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bottomSheetScroll.setVisibility(View.VISIBLE);
                    initDriverBottomSheetViews(passengerNotification);
                    drawDriverPolyLine(passengerNotification);
                }
            });
        }
    }

    void drawDriverPolyLine(final PassengerNotification notification) {
        if (notification.getStatus().equals(PaxApiCall.callOut)) {
            if (isTrackDriver && getTrackDirver != null) {
                final LatLng driverLocation = getTrackDirver.getDriverLocation();
                LatLng passengerLocation = new LatLng(Double.parseDouble(notification.getPickUpLat()), Double.parseDouble(notification.getPickUpLng()));
                if (driverLocation != null) {
                    drawPolyLine = new DrawPolyLine(context, gMap, driverLocation, passengerLocation);
                    drawPolyLine.drawPolyLineToMap();
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawDriverPolyLine(notification);
                        }
                    }, 1000);
                }
            }
        }
        if (notification.getStatus().equals(PaxApiCall.wating)) {
            if (drawPolyLine != null) {
                drawPolyLine.removePolyLine();
            }
        }
        if (notification.getStatus().equals(PaxApiCall.passengerOnBoard)) {
            if (isTrackDriver && getTrackDirver != null) {
                final LatLng driverLocation = getTrackDirver.getDriverLocation();
                LatLng passengerDestination = new LatLng(Double.parseDouble(notification.getDropLat()), Double.parseDouble(notification.getDropLng()));
                if (driverLocation != null) {
                    drawPolyLine = new DrawPolyLine(context, gMap, driverLocation, passengerDestination);
                    drawPolyLine.drawPolyLineToMap();
                    setDestinationMarker(passengerDestination, notification.getDestinationLocation(), false);
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawDriverPolyLine(notification);
                        }
                    }, 1000);
                }
            }
        }
        if (notification.getStatus().equals(PaxApiCall.complete)) {
            if (drawPolyLine != null) {
                drawPolyLine.removePolyLine();
            }
        }
    }

    public void socketLocationUpdate(Object... args) {
        if (!isTrackDriver) {
            if (getCars != null)
                getCars.updateCarOnMapWithSocketData(args);
        } else {
            if (getTrackDirver != null)
                getTrackDirver.updateDriverWithSocketData(args);
        }
    }

    void setLocationSelector() {
        requestRide.setText(R.string.ride_now);
        toggleView(locationSelector);
        LatLng latLng = gMap.getCameraPosition().target;
        if (isPickUPLocation) {
            setCurrentMarker(latLng, null);
            topPanelMenu.performClick();
        } else {
            setDestinationMarker(latLng, null, true);
            topPanelMenuBelow.performClick();
        }
    }

    @Override
    public void onSocketConnect() {
    }

    @Override
    public void onSocketLocationUpdate(Object... args) {
        socketLocationUpdate(args);
        if (!isRefreshRequestSent) {
            refreshState();
            isRefreshRequestSent = true;
        }
    }

    @Override
    public void onSocketPassengerUpdate(Object... args) {
        socketPassengerNotificationUpdate(args);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destory");
//        if (!isDriverTracked) {
//            Log.d("Service", "On Stop");
//            stopService(new Intent(this, SocketService.class));
//        } else {
//            SocketService.removeSocketListner();
//        }
        stopService(new Intent(this, SocketService.class));
        SocketService.removeSocketListner();
        StoragePreference.deleteLocation(context);
        unRegisterReceiver();
    }

    public void setLocation(String address, LatLng latLng) {
        if (isPickUPLocation) {
            setCurrentMarker(latLng, address);
        } else {
            setDestinationMarker(latLng, address, true);
        }
        placeLayout.setVisibility(View.INVISIBLE);
        placeLayoutVisibility = false;
    }

    void getRideHistory() {
        ApiCalls.rideHistory(StoragePreference.getUserId(context), StoragePreference.getTOKEN(context), "all", new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                parseRideHistoryResponse(jsonObject);
                getVehiclesList(currentLocation);
//                hideFirstLayout();
            }
        });
    }

    void parseRideHistoryResponse(JSONObject jsonObject) {
        try {
            rideHistories = new ArrayList<>();
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                JSONArray ridesHistoryArray = response.getJSONArray(PaxApiCall.list);
                for (int i = 0; i < ridesHistoryArray.length(); i++) {
                    JSONObject ride = (JSONObject) ridesHistoryArray.get(i);
                    RideHistory rideHistory = new RideHistory();
                    rideHistory.setPickUpLocation(ride.getString(PaxApiCall.pickUpLocation));
                    rideHistory.setDropLocation(ride.getString(PaxApiCall.dropLocation));
                    rideHistory.setPick_lat(ride.getString(PaxApiCall.pickUpLatitude));
                    rideHistory.setPick_lng(ride.getString(PaxApiCall.pickUpLongitude));
                    rideHistory.setDrop_lat(ride.getString(PaxApiCall.dropLatitude));
                    rideHistory.setDrop_lng(ride.getString(PaxApiCall.dropLongitude));
                    rideHistories.add(rideHistory);
                }
                Log.d(TAG, rideHistories.toString());
                hideFirstLayout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void rideAgain(RideHistory rideHistory) {
        LatLng currentLatLng = new LatLng(Double.parseDouble(rideHistory.getPick_lat()), Double.parseDouble(rideHistory.getPick_lng()));
        LatLng destiantionLatLng = new LatLng(Double.parseDouble(rideHistory.getDrop_lat()), Double.parseDouble(rideHistory.getDrop_lng()));
        setCurrentMarker(currentLatLng, rideHistory.getPickUpLocation());
        setDestinationMarker(destiantionLatLng, rideHistory.getDropLocation(), true);
    }

    void setUserProfileImage() {
        ApiCalls.getUserProfileData(StoragePreference.getUserId(context), StoragePreference.getTOKEN(context), new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(PaxApiCall.status);
                    if (status == 1) {
                        Passenger passenger = new Passenger();
                        JSONObject userData = jsonObject.getJSONObject(PaxApiCall.response);
                        passenger.setUserId(userData.getString(PaxApiCall.userId));
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
                        passenger.setToken(StoragePreference.getTOKEN(context));
                        setUserData(passenger);
                        StoragePreference.setPASSENGER(context, passenger);
                    } else {
                        setPreSavedUser();
                    }
                } catch (Exception e) {
                    setPreSavedUser();
                }
                getRideHistory();
            }
        });
    }

    void setUserData(Passenger passenger) {
        userName.setText(passenger.getFirstName() + " " + passenger.getLastName());
        Picasso.with(context).load(passenger.getProfile()).error(R.drawable.user_image).transform(new CircleTransform()).into(profilePic);
        if (!passenger.getHomeAddress().equals("")) {
            LatLng latLng = new LatLng(Double.parseDouble(passenger.getHomeLatitude()), Double.parseDouble(passenger.getHomeLongitude()));
            StoragePreference.setHomeLocation(context, passenger.getHomeAddress(), latLng);
        }
        if (!passenger.getWorkAddress().equals("")) {
            LatLng latLng = new LatLng(Double.parseDouble(passenger.getWorkLatitude()), Double.parseDouble(passenger.getWorkLongitude()));
            StoragePreference.setWorkLocation(context, passenger.getWorkAddress(), latLng);
        }
    }

    void setPreSavedUser() {
        Passenger passenger = StoragePreference.getPASSENGER(context);
        if (passenger != null) {
            setUserData(passenger);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        isRestart = true;
    }

    void requestRide() {
        paymentMethod = StoragePreference.getPaymentMethod(context);
        toggleView(bottomSheetScroll);
        setCarListInBottomSheet();
    }

    public void paymentSuccess() {
        isPaypalPaid = true;
        requestBtn.performClick();
    }

    public List<LocationHistory> getUniqueLocations(List<LocationHistory> locationHistories) {
        for (int i = 2; i < locationHistories.size(); i++) {

            LocationHistory locationHistory = locationHistories.get(i);

            for (int j = i + 1; j < locationHistories.size(); j++) {
                if (locationHistory.getLocationDescription().equals(locationHistories.get(j).getLocationDescription())) {
                    locationHistories.remove(j);
                    j--;
                }
            }
        }
        return locationHistories;
    }

    void registerReceivers() {
        registerReceiver(WifiStateChangedReceiver,
                new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        registerReceiver(gpsReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        setMobileDataListner();
    }

    void unRegisterReceiver() {
        unregisterReceiver(WifiStateChangedReceiver);
        unregisterReceiver(gpsReceiver);
    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                if (!utils.isGPSEnable(context)) {
                    onGpsDisconnected();
                } else {
                    onGpsConnected();
                }
            }
        }
    };
    private BroadcastReceiver WifiStateChangedReceiver
            = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (extraWifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    onWifiDisconnected();
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    onWifiConnected();
                    break;
            }

        }
    };

    void setMobileDataListner() {
        TelephonyManager myTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onDataConnectionStateChanged(int state) {
                switch (state) {
                    case TelephonyManager.DATA_DISCONNECTED:
                        if (!utils.getActiveInternet(context)) {
                            onWifiDisconnected();
                        }
                        break;
                    case TelephonyManager.DATA_CONNECTED:
                        onWifiConnected();
                        break;
                }
            }
        };
        myTelephonyManager.listen(callStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    }

    void onWifiDisconnected() {
        internetError.setVisibility(View.VISIBLE);
        ImageView iconLocation = (ImageView) internetError.findViewById(R.id.img_wifioff);
        TextView topText = (TextView) internetError.findViewById(R.id.txt_connectivity);
        TextView textTwo = (TextView) internetError.findViewById(R.id.txt_connectivity_two);
        topText.setText(R.string.no_connectivity);
        textTwo.setText(R.string.please_check_your_internet_connection);
        iconLocation.setImageResource(R.drawable.ic_no_wifi);
        requestRide.setEnabled(false);
        utils.enableDisableView(topPanelView, false);
        setPreSavedUser();
    }

    void onGpsDisconnected() {
        internetError.setVisibility(View.VISIBLE);
        ImageView iconLocation = (ImageView) internetError.findViewById(R.id.img_wifioff);
        TextView topText = (TextView) internetError.findViewById(R.id.txt_connectivity);
        TextView textTwo = (TextView) internetError.findViewById(R.id.txt_connectivity_two);
        topText.setText("GPS Disable");
        textTwo.setText("Please Enable your GPS");
        iconLocation.setImageResource(R.drawable.ic_location_off);
        requestRide.setEnabled(false);
    }

    void onWifiConnected() {
        internetError.setVisibility(View.GONE);
        requestRide.setEnabled(true);
        utils.enableDisableView(topPanelView, true);
        setCurrentLocationOnActiveInternet();
        if (!utils.isGPSEnable(context)) {
            onGpsDisconnected();
        }
    }

    void onGpsConnected() {
        internetError.setVisibility(View.GONE);
        requestRide.setEnabled(true);
        if (!utils.getActiveInternet(context)) {
            onWifiDisconnected();
        }
    }

    boolean isAddressSame(LatLng destinationLocation) {
        if (currentLocation != null) {
            return currentLocation.equals(destinationLocation);
        }
        return false;
    }

    void hideFirstLayout() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigationView.setVisibility(View.VISIBLE);
                firstView.setVisibility(View.GONE);
            }
        }, 1000);
    }

    void setCurrentLocationOnActiveInternet() {
        if (currentLocation != null && currLocationMarker == null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (utils.getActiveInternet(context)) {
                        setCurrentMarker(currentLocation, null);
                        sendTokenToServer();
                    } else {
                        setCurrentLocationOnActiveInternet();
                    }
                }
            }, 1000);
        }
    }

    void refreshState() {
        Log.d("Refresh", "Sent");
        ApiCalls.initilizeState(StoragePreference.getUserId(context));
    }

    List<LocationHistory> getLocations() {
        List<LocationHistory> locations = new ArrayList<>();
        String home, work;
        home = StoragePreference.getHomeLocation(context);
        work = StoragePreference.getWorkLocation(context);
        if (!home.equals(getString(R.string.add_home))) {
            LatLng latLng = StoragePreference.getHomeLatLng(context);
            locations.add(new LocationHistory("Home", home, R.drawable.ic_home_black_36dp, latLng));
        } else {
            locations.add(new LocationHistory("Home", null, R.drawable.ic_add_circle_outline_black_36dp, null));
        }

        if (!work.equals(getString(R.string.add_work))) {
            LatLng latLng = StoragePreference.getWorkLatLng(context);
            locations.add(new LocationHistory("Work", work, R.drawable.ic_work_black_36dp, latLng));
        } else {
            locations.add(new LocationHistory("Work", null, R.drawable.ic_add_circle_outline_black_36dp, null));
        }
        if (rideHistories.size() != 0) {
            for (int i = 0; i < rideHistories.size() && i < 4; i++) {
                String address = rideHistories.get(i).getPickUpLocation();
                String lat = rideHistories.get(i).getPick_lat();
                String lng = rideHistories.get(i).getPick_lng();
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                locations.add(new LocationHistory(address.split(" ")[0], address, R.drawable.ic_history_black_36dp, latLng));
                String address1 = rideHistories.get(i).getDropLocation();
                String lat1 = rideHistories.get(i).getDrop_lat();
                String lng1 = rideHistories.get(i).getDrop_lng();
                LatLng latLng1 = new LatLng(Double.parseDouble(lat1), Double.parseDouble(lng1));
                locations.add(new LocationHistory(address1.split(" ")[0], address1, R.drawable.ic_history_black_36dp, latLng1));
            }
        }
        return locations;
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_CHOSER_REQUEST_CODE)
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                ApiCalls.updateProfilePic(StoragePreference.getUserId(context), utils.getImagePathFromURI(context, uri), new PaxApiCallListner() {
                    @Override
                    public void onApiRequestResult(JSONObject jsonObject) {
                        try {
                            int status = jsonObject.getInt(PaxApiCall.status);
                            if (status == 1) {
                                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                                String msg = response.getString(PaxApiCall.message);
                                setUserProfileImage();
                            } else {
                                String msg = jsonObject.getString(PaxApiCall.errorMessage);
                            }
                        } catch (Exception e) {
                            Log.d(TAG, e.toString());
                        }
                    }
                });
            }
        if (requestCode == SETTING_REQUEST) {
            setUserProfileImage();
        }
        if (requestCode == PAYPAL_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                transId = data.getStringExtra(PaxApiCall.payPalTransId);
                paymentSuccess();
            }
        }
        if (requestCode == RIDE_AGAIN) {
            if (resultCode == Activity.RESULT_OK) {
                RideHistory rideHistory = (RideHistory) data.getSerializableExtra("history");
                if (rideHistory != null) {
                    rideAgain(rideHistory);
                }
                drawer.closeDrawer(Gravity.LEFT);
            }
        }
    }

    void changeDataOnPassengerState(final PassengerNotification notification) {
        if (!notification.getStatus().equals(PaxApiCall.cancel) && !notification.getStatus().equals(PaxApiCall.complete) && !notification.getStatus().equals(PaxApiCall.connecting)) {
            LatLng current = new LatLng(Double.parseDouble(notification.getPickUpLat()), Double.parseDouble(notification.getPickUpLng()));
            LatLng destination = new LatLng(Double.parseDouble(notification.getDropLat()), Double.parseDouble(notification.getDropLng()));
            setCurrentMarker(current, notification.getPickUPLocation());
            setDestinationMarker(destination, notification.getDestinationLocation(), false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    utils.enableDisableView(topPanelView, false);
                }
            });
            requestRide.setVisibility(View.GONE);
            bottomSheetScroll.setVisibility(View.VISIBLE);
            initDriverBottomSheetViews(notification);
            drawDriverPolyLine(notification);
        } else {
            onRideCompleted(notification);
        }
    }
}

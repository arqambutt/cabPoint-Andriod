package com.apps.cabpoint.cabigate.restapi;

import android.util.Log;

import com.apps.cabpoint.cabigate.models.Booking;
import com.apps.cabpoint.cabigate.models.CreditCard;
import com.apps.cabpoint.cabigate.models.Fare;
import com.apps.cabpoint.cabigate.models.FreeRide;
import com.apps.cabpoint.cabigate.models.NeedHelp;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.models.PaymentInfo;
import com.apps.cabpoint.cabigate.models.Promotion;
import com.apps.cabpoint.cabigate.models.Vehicle;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Abdul Ghani on 5/13/2017.
 */

public class PaxApiCall {

    public static PaxApiCallListner paxApiCallListner;
    public static PaxApiDrawCallListner paxApiDrawCallListner;

    // Api database identifiers
    public static String ID = "ID";
    public static String heading = "heading";
    public static String feedbackEnable = "feedback_enable";
    public static String firstName = "fname";
    public static String FirstNAME = "firstname";
    public static String lastName = "lname";
    public static String phone = "phone";
    public static String password = "password";
    public static String email = "email";
    public static String companySerial = "company_serial";
    public static String companyId = "companyid";
    public static String companyIdValue = "2100";
    public static String userId = "userid";
    public static String keyMatch = "keymatch";
    public static String oPT = "OPT";
    public static String token = "token";
    public static String reason = "reason";
    public static String jobHistoryType = "type";
    public static String current = "current";
    public static String all = "all";
    public static String cancel = "cancel";
    public static String jobId = "jobid";
    public static String job_Id = "job_id";
    public static String expireMonth = "exp_month";
    public static String expireYear = "exp_year";
    public static String defaultCard = "default";
    public static String cardNumber = "number";
    public static String cardVerificationCode = "cvc";
    public static String cardID = "cardid";
    public static String authorization = "auth";
    public static String authNormal = "normal";
    public static String status = "status";
    public static String response = "response";
    public static String currentLocation = "current_location";
    public static String dropLocation = "drop_location";
    public static String taxiModel = "taxi_model";
    public static String latitude = "lat";
    public static String longtude = "lng";
    public static String longitudeDriver = "lon";
    public static String distance = "distance";
    public static String unit = "unit";
    public static String totalFare = "total_fare";
    public static String totalDuration = "total_duration";
    public static String duration = "duration";
    public static String count = "count";
    public static String list = "list";
    public static String driverId = "driver_id";
    public static String driverName = "driver_name";
    public static String callSign = "Callsign";
    public static String taxiColor = "taxi_color";
    public static String taxiType = "taxi_type";
    public static String estimateTime = "eta_time";
    public static String message = "msg";
    public static String errorMessage = "error_msg";
    public static String cardBrand = "brand";
    public static String lastFourDigitsOfCard = "last4";
    public static String vehicalId = "vehicle_id";
    public static String userName = "Username";
    public static String driverImage = "driver_image";
    public static String vehicleImage = "vehicle_image";
    public static String address = "Address";
    public static String note = "note";
    public static String passengerCount = "pax_count";
    public static String pickUpDate = "pickup_date";
    public static String pickUpLatitude = "pickup_lat";
    public static String pickUpLongitude = "pickup_lng";
    public static String dropLatitude = "drop_lat";
    public static String dropLongitude = "drop_lng";
    public static String paymentMethod = "payment_method";
    public static String passengerId = "passenger_id";
    public static String cash = "Cash";
    public static String credit = "creditcards";
    public static String paypal = "paypal";
    public static String pickUpLocation = "pickup_location";
    public static String pickUpDateTime = "pickup_datetime";
    public static String passengers = "passengers";
    public static String driverLatitude = "driver_lat";
    public static String driverLongitude = "driver_lng";
    public static String rating = "rating";
    public static String driverRating = "driver_rating";
    public static String comment = "comment";
    public static String carIcon = "icon";
    public static String namePairValues = "nameValuePairs";
    public static String bearing = "bearing";
    public static String speed = "speed";
    public static String username = "username";
    public static String socketDriverId = "driverid";
    public static String passengerIdNotfication = "passengerid";
    public static String pickUpLocNotfi = "pickup";
    public static String destinationLocNotifi = "dropoff";
    public static String dropLocLatNotifi = "dropoff_lat";
    public static String dropLocLngNotifi = "dropoff_lng";
    public static String driverContact = "driver_contact";
    public static String vehicleName = "vehicle_name";
    public static String vehicleModel = "vehicle_model";
    public static String vehiclePlate = "vehicle_plate";
    public static String detailsNoti = "details";
    public static String profilePic = "pax_photo";
    public static String driverPhone = "driver_phone";
    public static String maxPassenger = "max_passenger";
    public static String maxLaguage = "max_luggage";
    public static String file = "file";
    public static String plateNumber = "plate_number";
    public static String callOut = "callout";
    public static String wating = "waiting";
    public static String passengerOnBoard = "pob";
    public static String complete = "complete";
    public static String connecting = "connecting";
    public static String needHelpReference = "needhelp_refrence";
    public static String yes = "yes";
    public static String no = "no";
    public static String invitationLink = "invitaion_link";
    public static String invitationMessage = "invitaion_message";
    public static String freeRideValue = "freeridevalue";
    public static String currency = "currency";
    public static String expiry = "expiry";
    public static String code = "code";
    public static String description = "description";
    public static String typeId = "typeid";
    public static String name = "name";
    public static String verified = "verified";
    public static String homeAddress = "home_address";
    public static String workAddress = "work_address";
    public static String homeLatitide = "home_lat";
    public static String homeLongitude = "home_lng";
    public static String workLatitiude = "work_lat";
    public static String workLongitude = "work_lng";
    public static String postCode = "postcode";
    public static String termsAndConditonLink = "http://www.cabpointcars.com/companyid2100/terms";
    public static String payPalTransId = "paypal_txn_id";
    public static String promoCode = "promocode";
    public static String deviceToken = "device_token";
    public static String clientMetaId = "client_metadata_id";
    public static String payPalId = "paypal_id";
    public static String screenMessage = "screen_message";
    public static String historyOld = "old";
    public static String historyFuture = "future";


    // Api calls
    public static String SIGN_IN = "/signin";
    public static String SIGN_UP = "/signup";
    public static String VERIFY_PHONE = "/verifyphone";
    public static String VERIFY_PHONE_UPDATE = "/verifyphoneupdate";
    public static String PASSENGER_DETAIL = "/paxdetails";
    public static String UPDATE_PASSENGER_DETAIL = "/updatepaxdetails";
    public static String JOB_HISTORY = "/jobhistory";
    public static String CANCEL_JOB = "/canceljob";
    public static String ADD_CARDS = "/addcards";
    public static String CREDIT_CARD_LIST = "/storedcardslist";
    public static String DELETE_CARD = "/deletecard";
    public static String FORGOT_PASSWORD = "/forgotpassword";
    public static String MAKE_DEFAULT_CARD = "/makedefaultcard";
    public static String LOG_OUT = "/logout";
    public static String CALCULTE_FARE = "/calculatefare";
    public static String NEAREST_DRIVERS = "/nearestdrivers";
    public static String VEHICLES_LIST = "/vehicleslist";
    public static String DRIVER_DETAILS = "/driverdetails";
    public static String ADD_BOOKING = "/addbooking";
    public static String TRACK_DRIVER = "/trackdriver";
    public static String RATE_AND_PAY = "/rateadriver";
    public static String FREE_RIDES = "/freerides";
    public static String UPDATE_PROFILE_PIC = "/updateprofilephoto";
    public static String UPDATE_PHONE_NUMBER = "/phoneupdate";
    public static String NEED_HELP = "/needhelp";
    public static String REPORT_PROBLEM = "/reportproblem";
    public static String PROMOTIONS = "/activepromocodes";
    public static String PAYMENT_INFO = "/paymentinfo";
    public static String VALIDATE_PROMO_CODE = "/promocodevalidate";
    public static String DEVICE_TOKEN = "/devicetoken";
    public static String PAYPAL_AUTH_CODE = "/paypalauthorizationcode";
    public static String PAYPAY_ACCOUNT_LIST = "/paypalaccountlist";
    public static String MAKE_DEFAULT_PAYPAL = "/makedefaultpaypal";
    public static String DELETE_PAYPAL_CARD = "/deletepaypalaccount";
    public static String INITILIZE_STATE = "/initializestate";
    public static String LATEST_PROMOTIONS = "/promotion";
    public static String COMPANY_SETTING = "/companysettings";


    public static void requestAPI(String requestString, RequestParams requestParams, final PaxApiCallListner paxApiCallListner1) {
        UberRestClient.post(requestString, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                paxApiCallListner1.onApiRequestResult(response);
                if (response != null) {
                    Log.d("Result", response.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(PaxApiCall.status, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                paxApiCallListner1.onApiRequestResult(jsonObject);
            }
        });
    }

    public static void requestAPIOnce(String requestString, RequestParams requestParams, final PaxApiCallListner paxApiCallListner1) {
        UberRestClient.postOnceOnly(requestString, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                paxApiCallListner1.onApiRequestResult(response);
                if (response != null) {
                    Log.d("Result", response.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(PaxApiCall.status, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                paxApiCallListner1.onApiRequestResult(jsonObject);
            }
        });
    }

    public static void requestDrawCarApi(String requestString, final RequestParams requestParams, PaxApiDrawCallListner paxApiDrawCallListner1) {
        paxApiDrawCallListner = paxApiDrawCallListner1;
        UberRestClient.post(requestString, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                paxApiDrawCallListner.onDrawCarRequestResult(response);
                if (response != null) {
                    Log.d("Result", response.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(PaxApiCall.status, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                paxApiDrawCallListner.onDrawCarRequestResult(jsonObject);
            }
        });
    }

    public static void requestRefreshState(String requestString, final RequestParams requestParams) {
        UberRestClient.post(requestString, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public static RequestParams getSignupParameters(String firstName, String lastName, String phone, String password, String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.firstName, firstName);
            jsonObject.put(PaxApiCall.lastName, lastName);
            jsonObject.put(PaxApiCall.phone, phone);
            jsonObject.put(PaxApiCall.password, password);
            jsonObject.put(PaxApiCall.email, email);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("Data", params.toString());
        return params;
    }

    public static RequestParams getSignInParameters(String email, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.email, email);
            jsonObject.put(PaxApiCall.password, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getVerifyPhoneParameters(String userId, String keyMatch, String opt, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.keyMatch, keyMatch);
            jsonObject.put(PaxApiCall.oPT, opt);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getPassengerParameters(String userId, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getUpdatePassengerParameters(String firstName, String lastName, String phone, String token, String userId, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.firstName, firstName);
            jsonObject.put(PaxApiCall.lastName, lastName);
            jsonObject.put(PaxApiCall.phone, phone);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
            if (password != null) {
                jsonObject.put(PaxApiCall.password, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getUpdatePassengerParameters(Passenger passenger) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.firstName, passenger.getFirstName());
            jsonObject.put(PaxApiCall.lastName, passenger.getLastName());
            jsonObject.put(PaxApiCall.phone, passenger.getPhone());
            jsonObject.put(PaxApiCall.token, passenger.getToken());
            jsonObject.put(PaxApiCall.userId, passenger.getUserId());
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
            if (passenger.getHomeAddress() != null) {
                jsonObject.put(PaxApiCall.homeAddress, passenger.getHomeAddress());
                jsonObject.put(PaxApiCall.homeLatitide, passenger.getHomeLatitude());
                jsonObject.put(PaxApiCall.homeLongitude, passenger.getHomeLongitude());
            }
            if (passenger.getWorkAddress() != null) {
                jsonObject.put(PaxApiCall.workAddress, passenger.getWorkAddress());
                jsonObject.put(PaxApiCall.workLatitiude, passenger.getWorkLatitude());
                jsonObject.put(PaxApiCall.workLongitude, passenger.getWorkLongitude());
            }
            if (passenger.getPassword() != null) {
                jsonObject.put(PaxApiCall.password, passenger.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getJobHistoryParameters(String userId, String token, String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.jobHistoryType, type);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getCancelJobParameters(String userId, String jobId, String reason, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.jobId, jobId);
            jsonObject.put(PaxApiCall.reason, reason);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("Cancel", jsonObject.toString());
        Log.d("Cancel", params.toString());
        return params;
    }

    public static RequestParams getPassengerCardDetails(String userId, int expireMonth, int expireYear, String cardNumber, String cardVerificationCode, String token, String postCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.expireMonth, expireMonth);
            if (expireYear != -1)
                jsonObject.put(PaxApiCall.expireYear, expireYear);
            jsonObject.put(PaxApiCall.cardNumber, cardNumber);
            jsonObject.put(PaxApiCall.cardVerificationCode, cardVerificationCode);
            jsonObject.put(PaxApiCall.postCode, postCode);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getCreditCardList(String userId, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getDeleteCreditCardDetails(String userId, String cardID, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.cardID, cardID);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getForgotPasswordDetails(String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.email, email);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getDefaultCardDetails(String userId, String cardID, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.cardID, cardID);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getLogoutDetails(String userId, String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.token, token);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getFareEstimationParams(String userId, String currentLocation, String dropLocation, int taxiModel, LatLng current, LatLng drop) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
            jsonObject.put(PaxApiCall.currentLocation, currentLocation);
            jsonObject.put(PaxApiCall.dropLocation, dropLocation);
            jsonObject.put(PaxApiCall.taxiModel, taxiModel);
            jsonObject.put(PaxApiCall.pickUpLatitude, current.latitude + "");
            jsonObject.put(PaxApiCall.pickUpLongitude, current.longitude + "");
            jsonObject.put(PaxApiCall.dropLatitude, drop.latitude + "");
            jsonObject.put(PaxApiCall.dropLongitude, drop.longitude + "");
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getNearestDriversParams(String latitude, String longtude, String taxiModel) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.latitude, latitude);
            jsonObject.put(PaxApiCall.longtude, longtude);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
            if (taxiModel != null) {
                jsonObject.put(PaxApiCall.taxiModel, taxiModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getVehicalsListParams(String latitude, String longtude) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.latitude, latitude);
            jsonObject.put(PaxApiCall.longtude, longtude);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getDriverDetailsParams(String driverId, String companySerial) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.driverId, driverId);
//            jsonObject.put(PaxApiCall.companySerial, companySerial);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getBookingParameters(Booking book) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, book.getPassengerId());
            jsonObject.put(PaxApiCall.FirstNAME, book.getFirstName());
            jsonObject.put(PaxApiCall.currentLocation, book.getCurrentLocation());
            jsonObject.put(PaxApiCall.dropLocation, book.getDropLocation());
            jsonObject.put(PaxApiCall.passengerCount, book.getPassengerCount());
            jsonObject.put(PaxApiCall.pickUpDate, book.getPickUpDate());
            jsonObject.put(PaxApiCall.taxiModel, book.getTaxiModel());
            jsonObject.put(PaxApiCall.pickUpLatitude, book.getPickUpLatitude());
            jsonObject.put(PaxApiCall.pickUpLongitude, book.getPickUpLongitude());
            jsonObject.put(PaxApiCall.dropLatitude, book.getDropLatitude());
            jsonObject.put(PaxApiCall.dropLongitude, book.getDropLongitude());
            jsonObject.put(PaxApiCall.paymentMethod, book.getPaymentMethod());
            jsonObject.put(PaxApiCall.totalFare, book.getTotalFare());
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
            if (!book.getPaymentMethod().equals(PaxApiCall.paypal)) {
                book.setPayPalTransId("0");
            }
            jsonObject.put(PaxApiCall.payPalTransId, book.getPayPalTransId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("Booking", params.toString());
        return params;
    }

    public static RequestParams getTrackDriverParams(String driverId, String passengerId, String latitude, String longtude) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.driverId, driverId);
            jsonObject.put(PaxApiCall.passengerId, passengerId);
            jsonObject.put(PaxApiCall.latitude, latitude);
            jsonObject.put(PaxApiCall.longtude, longtude);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getRateAndPayParams(String jobId, String passengerId, double rating, String comment) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.job_Id, jobId);
            jsonObject.put(PaxApiCall.userId, Integer.parseInt(passengerId));
            jsonObject.put(PaxApiCall.rating, rating);
            jsonObject.put(PaxApiCall.comment, comment);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("Data", params.toString());
        return params;
    }

    public static RequestParams getFreeRideParams(String passengerId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, passengerId);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getUpdateProfileParams(String passengerId, String profileImage) {
        RequestParams params = new RequestParams();
        try {
            params.put(PaxApiCall.passengerId, Integer.parseInt(passengerId));
            params.put(PaxApiCall.file, new File(profileImage));
            params.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            params.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static RequestParams getUpdatePhoneParams(String userId, String phoneNumber) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.phone, phoneNumber);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("verify phone", params.toString());
        return params;
    }

    public static Fare parseFareResponse(JSONObject jsonObject) {
        Fare fare = null;
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                Gson gson = new Gson();
                fare = gson.fromJson(response.toString(),Fare.class);
                Log.d("Fare", fare.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fare;
    }

    public static RequestParams getVerifyPhoneParams(String userId, String phoneNumber, String keyMatch, String oPT) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.userId, Integer.parseInt(userId));
            jsonObject.put(PaxApiCall.phone, phoneNumber);
            jsonObject.put(PaxApiCall.keyMatch, keyMatch);
            jsonObject.put(PaxApiCall.oPT, oPT);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("verify phone", params.toString());
        return params;
    }

    public static RequestParams getNeedHelpParams(String userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, userId);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }
    public static RequestParams getRefreshParam(String userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("Refresh", params.toString());
        return params;
    }

    public static ArrayList<NeedHelp> parseNeedHelpResponse(JSONObject jsonObject) {
        ArrayList<NeedHelp> needHelps = null;
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                needHelps = new ArrayList<>();
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                JSONArray list = response.getJSONArray(PaxApiCall.list);
                for (int i = 0; i < list.length(); i++) {
                    NeedHelp needHelp = new NeedHelp();
                    JSONObject need = list.getJSONObject(i);
                    needHelp.setId(need.getString(PaxApiCall.ID));
                    needHelp.setHeading(need.getString(PaxApiCall.heading));
                    needHelp.setFeedBack(need.getString(PaxApiCall.feedbackEnable));
                    needHelp.setDetails(need.getString(PaxApiCall.detailsNoti));
                    needHelps.add(needHelp);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return needHelps;
    }

    public static RequestParams getReportProblemsParams(String jobId, String userId, String comment, String needHelpReference) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.job_Id, jobId);
            jsonObject.put(PaxApiCall.userId, userId);
            jsonObject.put(PaxApiCall.comment, comment);
            jsonObject.put(PaxApiCall.needHelpReference, needHelpReference);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
//            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static FreeRide parseFreeRideResponse(JSONObject jsonObject) {
        FreeRide freeRide = null;
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                freeRide = new FreeRide();
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                freeRide.setCurrency(response.getString(PaxApiCall.currency));
                freeRide.setExpiry(response.getString(PaxApiCall.expiry));
                freeRide.setFreeRideValue(response.getString(PaxApiCall.freeRideValue));
                freeRide.setInvitationLink(response.getString(PaxApiCall.invitationLink));
                freeRide.setInvitationMessage(response.getString(PaxApiCall.invitationMessage));
                freeRide.setScreenMessage(response.getString(PaxApiCall.screenMessage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return freeRide;
    }

    public static ArrayList<Promotion> parsePromotionResponse(JSONObject jsonObject) {
        ArrayList<Promotion> promotions = null;
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                JSONArray list = response.getJSONArray(PaxApiCall.list);
                promotions = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject object = (JSONObject) list.get(i);
                    Promotion promotion = new Promotion();
                    promotion.setId(object.getString(PaxApiCall.ID));
                    promotion.setExpiry(object.getString(PaxApiCall.expiry));
                    promotion.setCode(object.getString(PaxApiCall.code));
                    promotion.setDescription(object.getString(PaxApiCall.description));
                    Log.d("Promotions", promotion.toString());
                    promotions.add(promotion);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return promotions;
    }

    public static RequestParams getForgetPasswordParams(String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.email, email);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
            jsonObject.put(PaxApiCall.companySerial, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getPaymentParams(String companyId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.companyId, companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }


    public static ArrayList<Vehicle> parseVehicleListResponse(JSONObject jsonObject) {
        ArrayList<Vehicle> cars = null;
        if (jsonObject != null) {
            try {
                int status = jsonObject.getInt(PaxApiCall.status);
                if (status == 1) {
                    cars = new ArrayList<>();
                    JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                    JSONArray list = response.getJSONArray(PaxApiCall.list);
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = (JSONObject) list.get(i);
                        Vehicle nearestDrivers = new Vehicle();
                        nearestDrivers.setModel(object.getString(PaxApiCall.typeId));
                        nearestDrivers.setType(object.getString(PaxApiCall.name));
                        nearestDrivers.setIcon(object.getString(PaxApiCall.carIcon));
                        cars.add(nearestDrivers);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cars;
    }

    public static boolean parseCardResponse(JSONObject jsonObject) {
        boolean isCardAvailabe = false;
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                JSONArray cardArray = response.getJSONArray(PaxApiCall.list);
                for (int i = 0; i < cardArray.length(); i++) {
                    JSONObject card = (JSONObject) cardArray.get(i);
                    Gson gson = new Gson();
                    CreditCard creditCard = gson.fromJson(card.toString(), CreditCard.class);
                    if (creditCard.getDefaultCard().equals("1")) {
                        isCardAvailabe = true;
                        break;
                    }
                }
            } else {
                isCardAvailabe = false;
            }
        } catch (JSONException e) {
        }
        return isCardAvailabe;
    }

    public static PaymentInfo parsePaymentResponse(JSONObject jsonObject) {
        PaymentInfo paymentInfo = null;
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                Gson gson = new Gson();
                paymentInfo = gson.fromJson(response.toString(), PaymentInfo.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return paymentInfo;
    }

    public static RequestParams getValidatePromoParams(String passengerId, String promoCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, passengerId);
            jsonObject.put(PaxApiCall.promoCode, promoCode);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getDeviceTokenParams(String passengerId, String deviceToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, passengerId);
            jsonObject.put(PaxApiCall.deviceToken, deviceToken);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static RequestParams getPaypalAuthParams(String passengerId, String payPalAuthCode, String clientMetaId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, passengerId);
            jsonObject.put(PaxApiCall.code, payPalAuthCode);
            jsonObject.put(PaxApiCall.clientMetaId, clientMetaId);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        Log.d("PayPal",params.toString());
        return params;
    }

    public static RequestParams getDefaultPayPalParams(String passengerId, String payPalId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PaxApiCall.passengerId, passengerId);
            jsonObject.put(PaxApiCall.payPalId, payPalId);
            jsonObject.put(PaxApiCall.companyId, PaxApiCall.companyIdValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.put("data", jsonObject.toString());
        return params;
    }

    public static String parseCompanyIdResponse(JSONObject jsonObject){
        String id = null;
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                JSONObject response = jsonObject.getJSONObject(PaxApiCall.response);
                id = response.getString(PaxApiCall.companyId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }
}

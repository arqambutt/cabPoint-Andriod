package com.apps.cabpoint.cabigate.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.MarkerInfo;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

/**
 * Created by Abdul Ghani on 5/16/2017.
 */

public class StoragePreference {

    public static String MY_PREFERENCE_NAME = "cabigate_preference";
    public static String USER_ID = PaxApiCall.userId;
    public static String TOKEN = PaxApiCall.token;
    public static String NAME = PaxApiCall.firstName;
    public static String notAvailable = "N/A";
    private static String HOME_LOCATION = "home";
    private static String WORK_LOCATION = "work";
    private static String HOME_LAT = "home_lat";
    private static String HOME_LNG = "home_lng";
    private static String WORK_LAT = "work_lat";
    private static String WORK_LNG = "work_lng";
    private static String IS_SAVED_INSTANCE = "instance";
    private static String PAYMENT_METHOD = "payment";
    private static String CURRENT = "current";
    private static String DESTINATION = "destination";
    private static String FIRST_TIME = "first";
    private static String PASSENGER = "passenger";
    private static String COMPANY_ID = "companyid";
    private static String PASSWORD = PaxApiCall.password;

    public static void writeUserData(Context context, String userId, String token, String name, String password,String companyId) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(USER_ID, userId);
        editor.putString(TOKEN, token);
        editor.putString(NAME, name);
        editor.putString(PASSWORD, password);
        editor.putString(COMPANY_ID,companyId);
        PaxApiCall.companyIdValue = companyId;
        editor.apply();
    }

    public static String readUserData(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_ID, notAvailable);
        String token = sharedPreferences.getString(TOKEN, notAvailable);
        if (userId.equals(notAvailable)) {
            return notAvailable;
        }
        return userId + "/" + token;
    }

    public static String getCompanyId(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(COMPANY_ID, notAvailable);
    }

    public static void setFirstTime(Context context) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(FIRST_TIME, FIRST_TIME);
        editor.apply();
    }

    public static boolean isFirstTime(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(FIRST_TIME, notAvailable);
        if (userId.equals(notAvailable)) {
            return true;
        }
        return false;
    }

    public static Boolean isSessionExist(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_ID, notAvailable);
        String token = sharedPreferences.getString(TOKEN, notAvailable);
        if (userId.equals(notAvailable) && token.equals(notAvailable)) {
            return false;
        } else {
            return true;
        }
    }

    public static String getUserId(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID, notAvailable);
    }

    public static String getTOKEN(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN, notAvailable);
    }

    public static String getUserName(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NAME, notAvailable);
    }

    public static void clearUserData(Context context) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.apply();
    }

    public static void setHomeLocation(Context context, String home, LatLng homeLatlng) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(HOME_LOCATION, home);
        editor.putString(HOME_LAT, homeLatlng.latitude + "");
        editor.putString(HOME_LNG, homeLatlng.longitude + "");
        editor.apply();
    }

    public static void setWorkLocation(Context context, String work, LatLng workLatlng) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(WORK_LOCATION, work);
        editor.putString(WORK_LAT, workLatlng.latitude + "");
        editor.putString(WORK_LNG, workLatlng.longitude + "");
        editor.apply();
    }

    public static void setPASSWORD(Context context, String password) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public static String getPASSWORD(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSWORD, notAvailable);
    }

    public static String getHomeLocation(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String location = sharedPreferences.getString(HOME_LOCATION, notAvailable);
        if (location.equals(notAvailable)) {
            return context.getString(R.string.add_home);
        } else {
            return location;
        }
    }

    public static String getWorkLocation(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String location = sharedPreferences.getString(WORK_LOCATION, notAvailable);
        if (location.equals(notAvailable)) {
            return context.getString(R.string.add_work);
        } else {
            return location;
        }
    }

    @Nullable
    public static LatLng getHomeLatLng(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String lat = sharedPreferences.getString(HOME_LAT, notAvailable);
        String lng = sharedPreferences.getString(HOME_LNG, notAvailable);
        LatLng latLng = null;
        if (!lat.equals(notAvailable) && !lng.equals(notAvailable)) {
            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            return latLng;
        } else {
            return null;
        }
    }

    @Nullable
    public static LatLng getWorkLatLng(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String lat = sharedPreferences.getString(WORK_LAT, notAvailable);
        String lng = sharedPreferences.getString(WORK_LNG, notAvailable);
        LatLng latLng = null;
        if (!lat.equals(notAvailable) && !lng.equals(notAvailable)) {
            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            return latLng;
        } else {
            return null;
        }
    }

    public static void setSavedInstance(Context context, String jobId) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(IS_SAVED_INSTANCE, jobId);
        editor.apply();
    }

    public static String getSavedInstance(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(IS_SAVED_INSTANCE, notAvailable);
    }

    public static void setPaymentMethod(Context context, String payment) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(PAYMENT_METHOD, payment);
        editor.apply();
    }

    public static String getPaymentMethod(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String paymentMethod = sharedPreferences.getString(PAYMENT_METHOD, notAvailable);
        if (!paymentMethod.equals(notAvailable)) {
            return paymentMethod;
        } else {
            setPaymentMethod(context, PaxApiCall.cash);
            return PaxApiCall.cash;
        }
    }

    public static void setPASSENGER(Context context, Passenger passenger) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(passenger);
        editor.putString(PASSENGER, json);
        editor.apply();
    }

    public static Passenger getPASSENGER(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(PASSENGER, notAvailable);
        if (json.equals(notAvailable)) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(json, Passenger.class);
        }
    }

    public static void setDestinationtLocation(Context context, MarkerInfo markerInfo) {
        SharedPreferences sharedPreference = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(markerInfo);
        editor.putString(DESTINATION, json);
        editor.apply();
    }

    public static MarkerInfo getDestinationLocation(Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(DESTINATION, notAvailable);
        if (json.equals(notAvailable)) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(json, MarkerInfo.class);
        }
    }

    public static void deleteLocation(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CURRENT);
        editor.apply();
        editor.remove(DESTINATION);
        editor.apply();
        editor.commit();
    }
}

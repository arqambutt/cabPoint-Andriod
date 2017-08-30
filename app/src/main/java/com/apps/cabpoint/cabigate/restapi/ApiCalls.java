package com.apps.cabpoint.cabigate.restapi;

import com.apps.cabpoint.cabigate.models.Booking;
import com.apps.cabpoint.cabigate.models.Passenger;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.nearby.messages.PublishCallback;

/**
 * Created by Abdul Ghani on 5/27/2017.
 */

public class ApiCalls {
    public static void logout(String userId,String token,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.LOG_OUT,PaxApiCall.getLogoutDetails(userId,token),paxApiCallListner);
    }
    public static void getUserProfileData(String userId, String token,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.PASSENGER_DETAIL,PaxApiCall.getPassengerParameters(userId,token),paxApiCallListner);
    }
    public static void updateUserData(Passenger passenger,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.UPDATE_PASSENGER_DETAIL,PaxApiCall.getUpdatePassengerParameters(passenger),paxApiCallListner);
    }
    public static void addCardData(String userId,int expireMonth, int expireYear, String cardNumber, String cardVerificationCode,String postCode, String token,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.ADD_CARDS,PaxApiCall.getPassengerCardDetails(userId,expireMonth,expireYear,cardNumber,cardVerificationCode,token,postCode),paxApiCallListner);
    }
    public static void storeCardList(String userId, String token, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.CREDIT_CARD_LIST,PaxApiCall.getCreditCardList(userId,token),paxApiCallListner);
    }
    public static void getDriverDetails(String driverId,String companyId, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.DRIVER_DETAILS,PaxApiCall.getDriverDetailsParams(driverId,companyId),paxApiCallListner);
    }
    public static void deleteCard(String userId, String cardId, String token, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.DELETE_CARD,PaxApiCall.getDeleteCreditCardDetails(userId,cardId,token),paxApiCallListner);
    }
    public static void setDefult(String userId,String cardId, String token, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.MAKE_DEFAULT_CARD,PaxApiCall.getDefaultCardDetails(userId,cardId,token),paxApiCallListner);
    }
    public static void bookTaxi(Booking booking,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPIOnce(PaxApiCall.ADD_BOOKING,PaxApiCall.getBookingParameters(booking),paxApiCallListner);
    }
    public static void calculateFare(String userId, String currentLocation, String dropLocation, int taxiModel, LatLng current,LatLng drop, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.CALCULTE_FARE,PaxApiCall.getFareEstimationParams(userId,currentLocation,dropLocation,taxiModel,current,drop), paxApiCallListner);
    }
    public static void trackDriverLocation(String driverId, String passengerId, String longitude, String latitude, PaxApiDrawCallListner paxApiTrackDriverListner){
        PaxApiCall.requestDrawCarApi(PaxApiCall.TRACK_DRIVER,PaxApiCall.getTrackDriverParams(driverId,passengerId,longitude,latitude),paxApiTrackDriverListner);
    }
    public static void rideHistory(String userId, String token,String type,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.JOB_HISTORY,PaxApiCall.getJobHistoryParameters(userId,token,type),paxApiCallListner);
    }
    public static void rateAndPay(String jobId,String passengerId,double rating,String comment,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.RATE_AND_PAY,PaxApiCall.getRateAndPayParams(jobId,passengerId,rating,comment),paxApiCallListner);
    }
    public static void vehiclesList(String latitude,String longtude,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.VEHICLES_LIST,PaxApiCall.getVehicalsListParams(latitude,longtude),paxApiCallListner);
    }
    public static void getNearestDrivers(String latitude,String longtude,String taxiModel,PaxApiDrawCallListner paxApiDrawCallListner){
        PaxApiCall.requestDrawCarApi(PaxApiCall.NEAREST_DRIVERS,PaxApiCall.getNearestDriversParams(latitude,longtude,taxiModel),paxApiDrawCallListner);
    }
    public static void cancelBooking(String userid,String jobId, String reason,String token,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.CANCEL_JOB,PaxApiCall.getCancelJobParameters(userid,jobId,reason,token),paxApiCallListner);
    }
    public static void getFreeRide(String passengerId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.FREE_RIDES,PaxApiCall.getFreeRideParams(passengerId),paxApiCallListner);
    }
    public static void updateProfilePic(String passengerId, String profilePic, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.UPDATE_PROFILE_PIC,PaxApiCall.getUpdateProfileParams(passengerId,profilePic),paxApiCallListner);
    }
    public static void updatePhone(String userId,String phoneNumber,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.UPDATE_PHONE_NUMBER,PaxApiCall.getUpdatePhoneParams(userId,phoneNumber),paxApiCallListner);
    }
    public static void verifyUpdatePhone(String userId, String phoneNumber,String keyMatch,String oPT,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.VERIFY_PHONE_UPDATE,PaxApiCall.getVerifyPhoneParams(userId,phoneNumber,keyMatch,oPT),paxApiCallListner);
    }
    public static void needHelp(String userId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.NEED_HELP,PaxApiCall.getNeedHelpParams(userId),paxApiCallListner);
    }
    public static void getPromotion(String userId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.PROMOTIONS,PaxApiCall.getNeedHelpParams(userId),paxApiCallListner);
    }
    public static void reportProblem(String jobId,String userId,String comment,String needHelpReference,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.REPORT_PROBLEM,PaxApiCall.getReportProblemsParams(jobId,userId,comment,needHelpReference),paxApiCallListner);
    }
    public static void forgetPassword(String email,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.FORGOT_PASSWORD,PaxApiCall.getForgetPasswordParams(email),paxApiCallListner);
    }
    public static void paymentInfo(String companyId, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.PAYMENT_INFO,PaxApiCall.getPaymentParams(companyId),paxApiCallListner);
    }
    public static void validatePromoCode(String passengerId, String promoCode,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.VALIDATE_PROMO_CODE,PaxApiCall.getValidatePromoParams(passengerId,promoCode),paxApiCallListner);
    }
    public static void sendDeviceToken(String passengerId, String token,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.DEVICE_TOKEN,PaxApiCall.getDeviceTokenParams(passengerId,token),paxApiCallListner);
    }
    public static void payPalAuthCode(String passengerId,String authCode,String clientMetaId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.PAYPAL_AUTH_CODE,PaxApiCall.getPaypalAuthParams(passengerId,authCode,clientMetaId),paxApiCallListner);
    }
    public static void listPaypalAccouts(String passengerId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.PAYPAY_ACCOUNT_LIST,PaxApiCall.getNeedHelpParams(passengerId),paxApiCallListner);
    }
    public static void setDefaultPaypal(String passengerId, String payPalId, PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.MAKE_DEFAULT_PAYPAL,PaxApiCall.getDefaultPayPalParams(passengerId,payPalId),paxApiCallListner);
    }
    public static void deletePaypal(String passengerId,String paypalId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.DELETE_PAYPAL_CARD,PaxApiCall.getDefaultPayPalParams(passengerId,paypalId),paxApiCallListner);
    }
    public static void initilizeState(String pasengerId){
        PaxApiCall.requestRefreshState(PaxApiCall.INITILIZE_STATE,PaxApiCall.getRefreshParam(pasengerId));
    }
    public static void getLatestPromotions(String passengerId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.LATEST_PROMOTIONS,PaxApiCall.getNeedHelpParams(passengerId),paxApiCallListner);
    }
    public static void getCompanyId(String passengerId,PaxApiCallListner paxApiCallListner){
        PaxApiCall.requestAPI(PaxApiCall.COMPANY_SETTING,PaxApiCall.getNeedHelpParams(passengerId),paxApiCallListner);
    }
}

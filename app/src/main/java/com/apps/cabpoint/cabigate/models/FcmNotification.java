package com.apps.cabpoint.cabigate.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Ghani on 8/10/2017.
 */

public class FcmNotification {


    @SerializedName("subtitle")
    private String subTitle;
    private String smallIcon;
    private int sound;
    private String title;
    private int vibrate;
    private String largeIcon;
    private String message;
    private String tickerText;

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTickerText() {
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public static FcmNotification jsonToObject(String json) {
        Gson gson = new Gson();
        FcmNotification fcmNotification;
        fcmNotification = gson.fromJson(json, FcmNotification.class);
        return fcmNotification;
    }

    @Override
    public String toString() {
        return "FcmNotification{" +
                "subTitle='" + subTitle + '\'' +
                ", smallIcon='" + smallIcon + '\'' +
                ", sound=" + sound +
                ", title='" + title + '\'' +
                ", vibrate=" + vibrate +
                ", largeIcon='" + largeIcon + '\'' +
                ", message='" + message + '\'' +
                ", tickerText='" + tickerText + '\'' +
                '}';
    }
}

package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 7/17/2017.
 */

public class FreeRide {
    private String invitationLink;
    private String invitationMessage;
    private String freeRideValue;
    private String currency;
    private String expiry;
    private String screenMessage;

    public FreeRide() {
    }

    public String getInvitationLink() {
        return invitationLink;
    }

    public void setInvitationLink(String invitationLink) {
        this.invitationLink = invitationLink;
    }

    public String getInvitationMessage() {
        return invitationMessage;
    }

    public void setInvitationMessage(String invitationMessage) {
        this.invitationMessage = invitationMessage;
    }

    public String getFreeRideValue() {
        return freeRideValue;
    }

    public void setFreeRideValue(String freeRideValue) {
        this.freeRideValue = freeRideValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getScreenMessage() {
        return screenMessage;
    }

    public void setScreenMessage(String screenMessage) {
        this.screenMessage = screenMessage;
    }

    @Override
    public String toString() {
        return "FreeRide{" +
                "invitationLink='" + invitationLink + '\'' +
                ", invitationMessage='" + invitationMessage + '\'' +
                ", freeRideValue='" + freeRideValue + '\'' +
                ", currency='" + currency + '\'' +
                ", expiry='" + expiry + '\'' +
                ", screenMessage='" + screenMessage + '\'' +
                '}';
    }

    public String getPromoCode() {
        String link = getInvitationLink();
        String[] array = link.split("/");
        return array[array.length - 1];
    }
}

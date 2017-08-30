package com.apps.cabpoint.cabigate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Ghani on 5/29/2017.
 */

public class CreditCard {

    @SerializedName("cardid")
    private String cardId;
    @SerializedName("brand")
    private String brand;
    @SerializedName("last4")
    private String lastFourDigits;
    @SerializedName("exp_month")
    private String expireMonth;
    @SerializedName("exp_year")
    private String expireYear;
    @SerializedName("default")
    private String defaultCard;

    public CreditCard() {
    }

    public String getDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(String defaultCard) {
        this.defaultCard = defaultCard;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardId='" + cardId + '\'' +
                ", brand='" + brand + '\'' +
                ", lastFourDigits='" + lastFourDigits + '\'' +
                ", expireMonth='" + expireMonth + '\'' +
                ", expireYear='" + expireYear + '\'' +
                '}';
    }
}

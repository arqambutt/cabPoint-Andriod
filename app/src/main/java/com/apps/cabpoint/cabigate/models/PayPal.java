package com.apps.cabpoint.cabigate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Ghani on 8/10/2017.
 */

public class PayPal {

    @SerializedName("paypal_id")
    private String payPalId;
    private String email;
    private String date;
    @SerializedName("default")
    private String isDefault;

    public String getPayPalId() {
        return payPalId;
    }

    public void setPayPalId(String payPalId) {
        this.payPalId = payPalId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "PayPal{" +
                "payPalId='" + payPalId + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", isDefault='" + isDefault + '\'' +
                '}';
    }
}

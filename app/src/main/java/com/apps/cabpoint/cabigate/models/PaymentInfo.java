package com.apps.cabpoint.cabigate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Ghani on 7/24/2017.
 */

public class PaymentInfo {
    @SerializedName("paypal_email")
    private String payPalEmail;
    @SerializedName("contact_phone")
    private String contactPhone;
    @SerializedName("contact_email")
    private String contactEmail;

    @SerializedName("terms_link")
    private String termsAndCondition;
    @SerializedName("client_id")
    private String clientId;
    private String environment;

    public PaymentInfo() {
    }

    public String getPayPalEmail() {
        return payPalEmail;
    }

    public void setPayPalEmail(String payPalEmail) {
        this.payPalEmail = payPalEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(String termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "payPalEmail='" + payPalEmail + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", termsAndCondition='" + termsAndCondition + '\'' +
                ", clientId='" + clientId + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}


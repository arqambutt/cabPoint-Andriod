package com.apps.cabpoint.cabigate.models;

/**
 * Created by Abdul Ghani on 8/2/2017.
 */

public class PaymentMethod {
    private int paymentImage;
    private String paymentName;
    private boolean isDefault;
    private String id;
    public static int cash = 0;
    public static int credit = 1;
    public static int paypal = 2;

    public PaymentMethod() {
    }

    public PaymentMethod(int paymentImage, String paymentName, boolean isDefault) {
        this.paymentImage = paymentImage;
        this.paymentName = paymentName;
        this.isDefault = isDefault;
    }

    public PaymentMethod(int paymentImage, String paymentName, boolean isDefault, String id) {
        this.paymentImage = paymentImage;
        this.paymentName = paymentName;
        this.isDefault = isDefault;
        this.id = id;
    }

    public int getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(int paymentImage) {
        this.paymentImage = paymentImage;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

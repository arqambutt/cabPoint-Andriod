package com.apps.cabpoint.cabigate.utils;

/**
 * Created by Abdul Ghani on 8/3/2017.
 */

public interface ConnectionListner {
    void onWifiConnected();
    void onWifiDisconnected();
    void onMobileDataConnected();
    void onMobileDataDisconnected();
}

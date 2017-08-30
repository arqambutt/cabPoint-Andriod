package com.apps.cabpoint.cabigate.map;

/**
 * Created by Abdul Ghani on 6/19/2017.
 */

public interface SocketListner {
    void onConnect(Object... args);
    void onDisconnect(Object... args);
    void onLocationUpdate(Object... args);
    void onPassengerNotification(Object... args);
}

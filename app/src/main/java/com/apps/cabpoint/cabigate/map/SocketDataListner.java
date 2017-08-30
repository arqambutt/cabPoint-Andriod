package com.apps.cabpoint.cabigate.map;

/**
 * Created by Abdul Ghani on 7/8/2017.
 */

public interface SocketDataListner {
    void onSocketConnect();
    void onSocketLocationUpdate(Object... args);
    void onSocketPassengerUpdate(Object... args);
}

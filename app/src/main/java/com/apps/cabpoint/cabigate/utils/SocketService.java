package com.apps.cabpoint.cabigate.utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.map.ServerSocket;
import com.apps.cabpoint.cabigate.map.SocketDataListner;
import com.apps.cabpoint.cabigate.map.SocketListner;

/**
 * Created by Abdul Ghani on 7/8/2017.
 */

public class SocketService extends Service implements SocketListner {

    ServerSocket serverSocket;
    public static final String TAG = "SocketService";
    private static SocketDataListner listenerS;
    private Context context = SocketService.this;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public SocketService() {
    }

    public static void setSocketListener(SocketDataListner listener) {
        if (listenerS == null)
            listenerS = listener;
    }

    public static void removeSocketListner() {
        listenerS = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        serverSocket = new ServerSocket(this, this);
        serverSocket.connect();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serverSocket.disconnect();
        Log.d(TAG, "Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onConnect(Object... args) {
        if (listenerS != null)
            listenerS.onSocketConnect();
        Log.d(TAG, "Connect");
    }

    @Override
    public void onDisconnect(Object... args) {
        Log.d(TAG, "DisConnect");
    }

    @Override
    public void onLocationUpdate(Object... args) {
        if (listenerS != null) {
            listenerS.onSocketLocationUpdate(args);
        }
    }

    @Override
    public void onPassengerNotification(Object... args) {
        Log.d(TAG, "Passenger");
        if (listenerS != null) {
            listenerS.onSocketPassengerUpdate(args);
        }
//        Gson gson = new Gson();
//        String json = gson.toJson(args);
//        PassengerNotification passengerNotification = PassengerNotification.parseJsonFromString(context, json, true);
//        Log.d("Passenger", passengerNotification.toString());
//        notifiyPassenger(passengerNotification.getStatus());
    }

    void notifiyPassenger(String status) {
        if (!status.equals("connecting")) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.car)
                            .setContentTitle(getString(R.string.app_name))
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setVibrate(new long[]{1000, 100, 1000, 100})
                            .setLights(Color.BLUE, 2000, 2000)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("Status " + status))
                            .setPriority(NotificationCompat.PRIORITY_HIGH);
            int mNotificationId = 1;
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            if (status.equals("complete")) {
                stopService(new Intent(this, SocketService.class));
            }
        }
    }
}
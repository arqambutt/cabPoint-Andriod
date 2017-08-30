package com.apps.cabpoint.cabigate.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.apps.cabpoint.cabigate.utils.ConnectionListner;

/**
 * Created by Abdul Ghani on 5/15/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private ConnectionListner connectionListner;

    void initViews() {

    }

    void initClickLisners() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
        setMobileDataListner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private void register() {
        registerReceiver(WifiStateChangedReceiver,
                new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    private void unRegister() {
        unregisterReceiver(WifiStateChangedReceiver);
    }

    protected BroadcastReceiver WifiStateChangedReceiver
            = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);

            switch (extraWifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    if (connectionListner != null) {
                        connectionListner.onWifiDisconnected();
                    }
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    if (connectionListner != null) {
                        connectionListner.onWifiConnected();
                    }
                    break;
            }

        }
    };

    private void setMobileDataListner() {
        TelephonyManager myTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onDataConnectionStateChanged(int state) {
                switch (state) {
                    case TelephonyManager.DATA_DISCONNECTED:
                        if (connectionListner != null) {
                            connectionListner.onMobileDataDisconnected();
                        }
                        break;
                    case TelephonyManager.DATA_CONNECTED:
                        if (connectionListner != null) {
                            connectionListner.onMobileDataConnected();
                        }
                        break;
                }
            }
        };
        myTelephonyManager.listen(callStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    }

    public void setConnectionListner(ConnectionListner connectionListner) {
        this.connectionListner = connectionListner;
    }

    public void removeConnectionListner() {
        connectionListner = null;
    }
}

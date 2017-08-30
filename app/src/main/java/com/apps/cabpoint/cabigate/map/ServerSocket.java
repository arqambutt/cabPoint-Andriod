package com.apps.cabpoint.cabigate.map;

import android.content.Context;
import android.util.Log;

import com.apps.cabpoint.cabigate.utils.StoragePreference;

import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Abdul Ghani on 6/19/2017.
 */

public class ServerSocket {

    private  static Socket socket = null;
    private  static JSONObject sendDataObject;
    private  static String serverURI = "http://cabigate.com:3000";
    private  static String sendData = "senddata";
    private  static String TAG = "ServerSocketException";
    private static SocketListner socketListner;

    public ServerSocket(Context context,SocketListner socketListner) {
        if(socket == null){
            try {

                socket = IO.socket(serverURI);
                sendDataObject = new JSONObject();
                sendDataObject.put("user_id", StoragePreference.getUserId(context));
                sendDataObject.put("type","passenger");
                sendDataObject.put("username",StoragePreference.getUserName(context));
                this.socketListner = socketListner;
                setSocketListner();

            } catch (Exception e) {
                Log.d(TAG,e.toString());
            }
        }
    }

    public void connect(){
        socket.connect();
    }
    public void disconnect(){ socket.disconnect();}
    boolean isConnected(){
        return socket.connected();
    }
    Socket getSocket(){
        return socket;
    }

    private void setSocketListner(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socketListner.onConnect(args);
                socket.emit(sendData,sendDataObject);
            }

        }).on("locationupdater", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socketListner.onLocationUpdate(args);
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.d(TAG, "DISCONNECT");
                socketListner.onDisconnect(args);
            }
        }).on("passenger_notifier", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socketListner.onPassengerNotification(args);
            }
        }).on("driver_updater", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        });

    }
}

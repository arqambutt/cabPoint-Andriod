package com.apps.cabpoint.cabigate.restapi;

import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by Abdul Ghani on 5/12/2017.
 */

public class UberRestClient{
    public static final String BASE_URL = "http://paxapi.cabigate.com/index.php";
    public static AsyncHttpClient client = new AsyncHttpClient();
    public static AsyncHttpClient syncHttpClient= new SyncHttpClient();

    public static void post(String url,RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
       getClient().post(getAbsoluteUrl(url),requestParams,responseHandler);
    }

    public static void postOnceOnly(String url,RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(0,3000);
        client.post(getAbsoluteUrl(url),requestParams,responseHandler);
    }
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
    private static AsyncHttpClient getClient()
    {
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return client;
    }
}

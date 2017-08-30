package com.apps.cabpoint.cabigate.restapi;

import org.json.JSONObject;

/**
 * Created by Abdul Ghani on 5/16/2017.
 */

public interface PaxApiCallListner {
    void onApiRequestResult(JSONObject jsonObject);
}

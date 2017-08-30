package com.apps.cabpoint.cabigate.map;

import com.apps.cabpoint.cabigate.models.NearestDrivers;

import java.util.ArrayList;

/**
 * Created by Abdul Ghani on 5/23/2017.
 */

public interface NearestDriversListner {
    void onNearestDriversSet(ArrayList<NearestDrivers> driversArrayList);
}

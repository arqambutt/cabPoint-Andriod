package com.apps.cabpoint.cabigate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.apps.cabpoint.cabigate.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by Abdul Ghani on 5/30/2017.
 */

public class PlaceAPIActivity extends BaseActivity {

    PlaceAutocompleteFragment autocompleteFragment;
    Context context = PlaceAPIActivity.this;
    public static String HOME = "home";
    public static String WORK = "work";
    Intent returnIntent;
    Boolean isHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_api);
        initViews();
        initClickLisners();
        isHome = getAddPlace();
    }

    @Override
    protected void initViews() {
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete);
    }

    @Override
    void initClickLisners() {
        returnIntent = new Intent();
        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input).performClick();
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                returnIntent.putExtra("location", place.getAddress());
                returnIntent.putExtra("lat", place.getLatLng().latitude);
                returnIntent.putExtra("lng", place.getLatLng().longitude);
                if (isHome) {
                    returnIntent.putExtra("add", 1);
                    setResult(Activity.RESULT_OK, returnIntent);
                } else {
                    returnIntent.putExtra("add", 2);
                    setResult(Activity.RESULT_OK, returnIntent);
                }
                finish();
            }

            @Override
            public void onError(Status status) {
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    boolean getAddPlace() {
        Intent get = getIntent();
        int add = get.getIntExtra("add", 1);
        if (add == 1) {
            return true;
        } else {
            return false;
        }
    }
}

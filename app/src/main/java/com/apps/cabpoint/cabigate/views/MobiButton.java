package com.apps.cabpoint.cabigate.views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.apps.cabpoint.cabigate.R;

/**
 * Created by MohammedMobinMunir on 7/27/2017.
 */

public class MobiButton extends android.support.v7.widget.AppCompatButton {

    public MobiButton(Context context) {
        super(context);

    }

    public MobiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackground(ContextCompat.getDrawable(context, R.drawable.btn_selector));
        setTypeface(Typeface.createFromAsset(context.getAssets(), "clanpro_news.ttf"));

    }
}

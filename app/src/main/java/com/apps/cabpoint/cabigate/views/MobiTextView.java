package com.apps.cabpoint.cabigate.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by MohammedMobinMunir on 7/27/2017.
 */

public class MobiTextView extends android.support.v7.widget.AppCompatTextView {
    public MobiTextView(Context context) {
        super(context);
    }

    public MobiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), "clanpro_news.ttf"));
//        if (getMaxLines() == 1) {
//            setSelected(true);
//            setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            setMarqueeRepeatLimit(-1);
//            setSingleLine(true);
//
//
//        }
    }
}

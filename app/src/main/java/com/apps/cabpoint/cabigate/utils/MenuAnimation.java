package com.apps.cabpoint.cabigate.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.cabpoint.cabigate.R;

/**
 * Created by Abdul Ghani on 7/6/2017.
 */

public class MenuAnimation {
    private int animationDuration = 300;
    private Context context;
    private ImageView topPanelMenu,topPanelEarth,topPanelMarker;
    private TextView topPanelCurrentLoc;

    public MenuAnimation(Context context, ImageView topPanelMenu, ImageView topPanelEarth, ImageView topPanelMarker, TextView topPanelCurrentLoc) {
        this.topPanelMenu = topPanelMenu;
        this.topPanelEarth = topPanelEarth;
        this.topPanelMarker = topPanelMarker;
        this.topPanelCurrentLoc = topPanelCurrentLoc;
        this.context = context;
    }

    public void animationIn(){

        ObjectAnimator animation = ObjectAnimator.ofFloat(topPanelMenu, "translationX", -context.getResources().getDimensionPixelOffset(R.dimen._55sdp));
        animation.setDuration(animationDuration);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                topPanelMenu.setImageResource(R.drawable.click_menu);
                earthMenuAnimation();
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topPanelCurrentLoc.getLayoutParams();
                params.setMargins(context.getResources().getDimensionPixelOffset(R.dimen._15sdp), context.getResources().getDimensionPixelOffset(R.dimen._5sdp), context.getResources().getDimensionPixelOffset(R.dimen._80sdp),context.getResources().getDimensionPixelOffset(R.dimen._5sdp));
                topPanelCurrentLoc.setLayoutParams(params);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animation.start();
    }
    private void menuAnimationOut(){
        ObjectAnimator animation = ObjectAnimator.ofFloat(topPanelMenu, "translationX", context.getResources().getDimensionPixelOffset(R.dimen._1sdp));
        animation.setDuration(animationDuration);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                topPanelMenu.setImageResource(R.drawable.menu_icon);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topPanelCurrentLoc.getLayoutParams();
                params.setMargins(context.getResources().getDimensionPixelOffset(R.dimen._15sdp), context.getResources().getDimensionPixelOffset(R.dimen._5sdp), context.getResources().getDimensionPixelOffset(R.dimen._30sdp),context.getResources().getDimensionPixelOffset(R.dimen._5sdp));
                topPanelCurrentLoc.setLayoutParams(params);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animation.start();
    }

    private void earthMenuAnimation(){
        topPanelEarth.setVisibility(View.VISIBLE);
        Animation animFadeIn= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in_animation);
        topPanelEarth.startAnimation(animFadeIn);

        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ObjectAnimator earthAnimation = ObjectAnimator.ofFloat(topPanelEarth, "translationX", -context.getResources().getDimensionPixelOffset(R.dimen._27sdp));
                earthAnimation.setDuration(animationDuration);
                earthAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        markerMenuAnimation();
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                earthAnimation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void markerMenuAnimation(){
        topPanelMarker.setVisibility(View.VISIBLE);
        Animation animFadeIn= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in_animation);
        topPanelMarker.startAnimation(animFadeIn);
    }
    public void animationOut(){
        Animation animFadeOut= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_out_animation);
        topPanelMarker.startAnimation(animFadeOut);
        topPanelMarker.setVisibility(View.INVISIBLE);
        earthMenuAnimationOut();
    }
    private void earthMenuAnimationOut(){
        ObjectAnimator earthAnimation = ObjectAnimator.ofFloat(topPanelEarth, "translationX", context.getResources().getDimensionPixelOffset(R.dimen._1sdp));
        earthAnimation.setDuration(animationDuration);
        earthAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                Animation animFadeOut= android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_out_animation);
                topPanelEarth.startAnimation(animFadeOut);
                topPanelEarth.setVisibility(View.INVISIBLE);
                menuAnimationOut();
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        earthAnimation.start();
    }
}

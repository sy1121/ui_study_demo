package com.example.custom_ui_demo.util;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * yishe
 * 2019/7/7
 */
public class AnimationUtils {

    public static RotateAnimation initRotateAnimation(long duration,
                                                      int fromAngle, int toAngle,
                                                      boolean isFillAfter, int repeatCount) {
        RotateAnimation mLoadingRotateAnimation = new RotateAnimation(fromAngle, toAngle,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        LinearInterpolator lirInterpolator = new LinearInterpolator();
        mLoadingRotateAnimation.setInterpolator(lirInterpolator);
        mLoadingRotateAnimation.setDuration(duration);
        mLoadingRotateAnimation.setFillAfter(isFillAfter);
        mLoadingRotateAnimation.setRepeatCount(repeatCount);
        mLoadingRotateAnimation.setRepeatMode(Animation.RESTART);
        return mLoadingRotateAnimation;
    }

    public static ScaleAnimation initScaleAnimation(long duration,
                                                    float fromX, float toX,
                                                    float fromY, float toY,
                                                    boolean isFillAfter,
                                                    int repeatCount){
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX,toX,fromY,toY,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(duration);
        scaleAnimation.setFillAfter(isFillAfter);
        scaleAnimation.setRepeatCount(repeatCount);
        return scaleAnimation;
    }


    public static class AnimationListerAdapter implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}

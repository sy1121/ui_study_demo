package com.example.custom_ui_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * yishe
 * 2019/7/4
 */
public class NoClickableButton extends android.support.v7.widget.AppCompatButton {

    private static final String TAG = "NoClickableButton";

    public NoClickableButton(Context context){
        this(context,null);
    }

    public NoClickableButton(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG,"dispatchTouchEvent event = " + event.toString());
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouchEvent event = " + event.toString());
        return false;
    }
}

package com.example.custom_ui_demo.fragment;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**
 * yishe
 * 2019-07-08
 */
public class RemoteControlView extends View{
    Path up_p,down_p,left_p,right_p,center_p;
    Region up,down,left,right,center;

    Matrix mMapMatrix = null;

    public RemoteControlView(Context context) {
        super(context);
    }

    public RemoteControlView(Context context,AttributeSet attrs) {
        super(context, attrs);
    }
}

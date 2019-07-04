package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * @author yishe
 * @date 2019/7/4.
 * email：yishe@tencent.com
 * description：
 */
public class BlinkTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mViewWidth,mViewHeight;
    private int mTranslate;

    public BlinkTextView(Context context){
        super(context);
    }

    public BlinkTextView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mViewWidth == 0){
            mViewWidth = getMeasuredWidth();
            if(mViewWidth > 0){
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(
                        0,
                        0,
                        mViewWidth,
                        0,
                        new int[]{
                                Color.BLUE,
                                0xffffffff,
                                Color.BLUE
                        },
                        null,
                        Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mGradientMatrix != null){
            mTranslate += mViewWidth / 5;
            if(mTranslate > 2 * mViewWidth){
                mTranslate = 0;
            }
            mGradientMatrix.setTranslate(mTranslate,0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }
}

package com.example.custom_ui_demo.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yishe on 2017/9/15.
 */

public class NormalProgressBar extends View {
    private static final String TAG ="NormalProgressBar";

    private static final int COLOR_SHADOW_BLUE = 0xFFAADDFF;
    private static final int COLOR_DARK_BLUE = 0xAA0066FF;
    private static final int PROGRESS_MAX = 100;


    private int mCurProgress = 0;
    private Paint mBackPaint,mProgressPaint;
    private int mWidth,mHeight;

    public NormalProgressBar(Context context){
        this(context,null);
    }

    public NormalProgressBar(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    private void  init(){
        mBackPaint =new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setColor(Color.WHITE);
        Shader shader=new LinearGradient(0,mHeight/2,mWidth,mHeight/2, COLOR_SHADOW_BLUE,COLOR_DARK_BLUE, Shader.TileMode.CLAMP);
        mBackPaint.setShader(shader);
        mBackPaint.setStrokeWidth(15);
        mBackPaint.setStyle(Paint.Style.FILL);

        mProgressPaint =new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.BLUE);
        mProgressPaint.setStrokeWidth(10);
        mProgressPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth =w;
        mHeight=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawBackground,
         canvas.drawLine(0.0f,(float)mHeight/2,(float)mWidth,(float)mHeight/2,mBackPaint);
        //draw progress
        float endx = mCurProgress * mWidth / PROGRESS_MAX;
        canvas.drawLine(0.0f,(float)mHeight/2,(float)endx,(float)mHeight/2,mProgressPaint);
        invalidate();
    }

    public int getProgress() {
        return mCurProgress;
    }

    public void setProgress(int progress) {
        this.mCurProgress = progress;
    }
}

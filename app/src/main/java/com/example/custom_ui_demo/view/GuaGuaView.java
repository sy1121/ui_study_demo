package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.custom_ui_demo.R;

/**
 * yishe
 * 2019/7/6
 */
public class GuaGuaView extends View {

    private Bitmap mBgBitmap,mFgBitmap;
    private Paint mPaint;
    private Canvas mCanvas;
    private Path mPath;
    public GuaGuaView(Context context) {
        this(context,null);
    }

    public GuaGuaView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAlpha(0); // 这里是关键
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(50);

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath = new Path();
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.poly_test);
        mFgBitmap = Bitmap.createBitmap(mBgBitmap.getWidth(),mBgBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mFgBitmap);
        mCanvas.drawColor(Color.GRAY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(),event.getY());
                break;
        }
        mCanvas.drawPath(mPath,mPaint);
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBgBitmap,0,0,null);
        canvas.drawBitmap(mFgBitmap,0,0,null);
    }
}

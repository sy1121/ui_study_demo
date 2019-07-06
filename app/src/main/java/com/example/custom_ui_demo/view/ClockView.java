package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.custom_ui_demo.util.DensityUtils;

import java.util.Date;

/**
 * yishe
 * 2019/7/6
 */
public class ClockView extends View{

    private static final String TAG = "ClockView";
    private Context mContext;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int BIG_LINE_SIZE = 5;
    private static final int SMALL_LINES_SIZE = 3;

    private int mWidth,mHeight;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private int mRadius;
    private int mMintueAngle,mHourAngle;
    private int mMintueSize,mHourSize;

    private Handler mLogicHandler;


    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView(){
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(SMALL_LINES_SIZE);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setAntiAlias(true);

        mTextPaint = new Paint(mLinePaint);
        mTextPaint.setTextSize(40);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureSize(widthMeasureSpec, DensityUtils.dp2px(mContext,DEFAULT_WIDTH));
        int height = measureSize(heightMeasureSpec,DensityUtils.dp2px(mContext,DEFAULT_HEIGHT));
        setMeasuredDimension(width,height);
    }


    private int measureSize(int widthMeasureSpec, int defaultSize){
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if(mode == MeasureSpec.EXACTLY){
            return size;
        }else{
            return defaultSize;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mRadius = (int)(Math.min(mWidth,mHeight)*0.8f)/2;
        mMintueSize = (int)(mRadius * 0.5f);
        mHourSize = (int)(mRadius * 0.25f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth/2,mHeight/2);
        //画表盘
        canvas.drawCircle(0,0,mRadius,mLinePaint);
        //画刻度
        for(int i = 0;i < 12;i++){
            canvas.save();
            canvas.rotate(30*i);
            String text = (i == 0 ? "12": i) + "";
            Rect rect = new Rect();
            mTextPaint.getTextBounds(text,0,text.length(),rect);
            if(i % 3 == 0){
                mLinePaint.setStrokeWidth(BIG_LINE_SIZE);
                canvas.drawLine(0,-mRadius,0,-mRadius*0.8f,mLinePaint);
                canvas.drawText(i+"",-rect.width()/2,-mRadius * 0.65f,mTextPaint);
            }else{
                mLinePaint.setStrokeWidth(SMALL_LINES_SIZE);
                canvas.drawLine(0,-mRadius,0,-mRadius * 0.9f,mLinePaint);
                canvas.drawText(i+"",-rect.width()/2,-mRadius * 0.75f,mTextPaint);
            }
            canvas.restore();
        }

        // 画指针
        //分针
        mLinePaint.setStrokeWidth(SMALL_LINES_SIZE * 2);
        canvas.drawLine(0,0,(int)(mMintueSize * Math.cos(mMintueAngle)),(int)(mMintueSize * Math.sin(mMintueAngle)),mLinePaint);
        //时针
        mLinePaint.setStrokeWidth(BIG_LINE_SIZE * 2);
        canvas.drawLine(0,0,(int)(mHourSize * Math.sin(mMintueAngle)),(int)(mHourSize * Math.sin(mHourAngle)),mLinePaint);
        canvas.restore();
    }

    public void work(){
        getLogicHandler().sendEmptyMessage(1);
    }

    private Handler getLogicHandler(){
        if(mLogicHandler != null){
            return mLogicHandler;
        }
        mLogicHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                long second = System.currentTimeMillis()/1000%(60*60*12);
                mHourAngle = (int)(second / (float)(60 * 60 * 12) * 360);
                second = second % (60*60);
                mMintueAngle = (int)(second / (float)(60 * 60) * 360);
                mMintueAngle = (mMintueAngle - 90 + 360) % 360;
                mHourAngle = (mHourAngle - 90 + 360) % 360;
                invalidate();
                mLogicHandler.sendEmptyMessageDelayed(1,60*1000);
            }
        };
        return mLogicHandler;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mLogicHandler != null){
            mLogicHandler.removeCallbacksAndMessages(null);
        }
    }
}

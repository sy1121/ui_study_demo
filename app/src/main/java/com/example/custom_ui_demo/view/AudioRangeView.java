package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.custom_ui_demo.util.DensityUtils;

import java.util.Random;
import java.util.logging.FileHandler;

/**
 * yishe
 * 2019/7/5
 * 模拟音频条动画
 */
public class AudioRangeView extends View{
    private static final String TAG = "AudioRangeView";

    private static final int DEFAULT_WIDTH = 300; //300dp 默认宽
    private static final int DEFAULT_HEIGHT = 150; //150dp
    private static final int DEFAULT_OFFSIZE = 10;//矩形的偏移量
    private static final int DEFAULT_RECT_WIDTH = 20;//矩形的宽度
    private static final int DEFAULT_RECT_COLOR = Color.BLUE;

    private int mDefaultWidth = DEFAULT_WIDTH;
    private int mDefaultHeight = DEFAULT_HEIGHT;
    private int mRectOffSize = DEFAULT_OFFSIZE;
    private int mRectColor = DEFAULT_RECT_COLOR;
    private int mRectWidth = DEFAULT_RECT_WIDTH;

    private Context mContext;
    private int mWidth,mHeight; //真实宽高
    private int mRectCount; //矩形数量

    private Paint mRectPaint;
    private Paint mOutlinePaint;
    private LinearGradient mLinearGradient;

    public AudioRangeView(Context context) {
        this(context,null);
    }

    public AudioRangeView(Context context,AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(mRectColor);
        mRectPaint.setAntiAlias(true);

        mOutlinePaint = new Paint();
        mOutlinePaint.setStyle(Paint.Style.FILL);
        //mOutlinePaint.setStrokeWidth(5);
        mOutlinePaint.setColor(Color.GRAY);
        mOutlinePaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureSize(widthMeasureSpec, DensityUtils.dp2px(mContext,mDefaultWidth));
        int height = measureSize(heightMeasureSpec,DensityUtils.dp2px(mContext,mDefaultHeight));
        setMeasuredDimension(width,height);
    }

    private int measureSize(int meaureSpec,int defaultSize){
        int mode = MeasureSpec.getMode(meaureSpec);
        int size = MeasureSpec.getSize(meaureSpec);

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
        mRectCount = (int)(mWidth * 0.9f) / (mRectWidth + mRectOffSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(1,-1); //y轴向上
        canvas.translate(0,-mHeight);//坐标原点移到左下角
        canvas.drawRoundRect(new RectF(0,0,mWidth,mHeight),50,50,mOutlinePaint);
        int xOffSize = (mWidth - mRectCount * (mRectWidth + mRectOffSize) + mRectOffSize)/2;
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0;i < mRectCount;i++){
            int height = Math.abs(random.nextInt((int)(mHeight*0.9f)));
            mLinearGradient = new LinearGradient(0,height,0,0,Color.RED ,Color.YELLOW,Shader.TileMode.CLAMP);
            mRectPaint.setShader(mLinearGradient);
            RectF rectF = new RectF(xOffSize,height,xOffSize+mRectWidth,0);
            canvas.drawRect(rectF,mRectPaint);
            xOffSize += mRectWidth+mRectOffSize;
        }
        canvas.restore();
        postInvalidateDelayed(200);
    }
}

package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.custom_ui_demo.entity.PieData;

import java.util.ArrayList;

public class PieView extends View {
    //颜色表
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    //饼状图初始绘制角度
    private float mStartAngle = 0;
    //数据
    private ArrayList<PieData> mData;
    //宽高
    private int mWidth,mHeight;
    //画笔
    private Paint mPaint = new Paint();
    //文字画笔
    private Paint mTextPaint = new Paint();

    public PieView(Context context){
        this(context,null);
    }

    public PieView(Context context, AttributeSet attrs){
        super(context,attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(30);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setAntiAlias(true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null == mData)
            return ;
        float currentStartAngle = mStartAngle;
        canvas.translate(mWidth/2,mHeight/2);
        float r = (float)(Math.min(mWidth,mHeight)/2*0.8);
        RectF rect = new RectF(-r,-r,r,r);
        for(int i = 0; i< mData.size();i++){
            PieData pie = mData.get(i);
            mPaint.setColor(pie.getColor());
            canvas.drawArc(rect,currentStartAngle,pie.getAngle(),true,mPaint);
            //绘制文字
            float midAngle = (float)Math.toRadians(currentStartAngle+pie.getAngle()/2);
            canvas.drawText(pie.getName(),(float) Math.cos(midAngle)*r/2,(float) Math.sin(midAngle)*r/2,mTextPaint);
            currentStartAngle += pie.getAngle();
        }
    }

    //设置起始角度
    public void setStartAngle(int mStartAngle){
        this.mStartAngle = mStartAngle;
        invalidate();
    }

    //设置数据
    public void setData(ArrayList<PieData> mData){
        this.mData = mData;
        initData(mData);
        invalidate();
    }

    //初始化数据
    private void initData(ArrayList<PieData> mData){
        if(null == mData|| mData.size() == 0) //数据有问题 直接返回
            return ;
        float sumValue = 0;
        for(int i = 0; i < mData.size(); i++){
            PieData pie = mData.get(i);
            sumValue += pie.getValue(); //计算数值和
            int j = i % mColors.length;//设置颜色
            pie.setColor(mColors[j]);
        }

        float sumAngle = 0;
        for(int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            float percentage = pie.getValue() / sumValue; //百分比
            float angle = percentage * 360;

            pie.setPercentage(percentage);
            pie.setAngle(angle);
            sumAngle += angle;

            Log.i("angle",""+pie.getAngle());
        }
    }

}

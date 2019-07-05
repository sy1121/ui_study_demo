package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.example.custom_ui_demo.util.DensityUtils;

/**
 * @author yishe
 * @date 2019/7/5.
 * email：yishe@tencent.com
 * description：
 */
public class PercentView extends View{

    private static final String TAG = "PercentView";

    private Context mContext;
    private Paint mCirclePaint;
    private Paint mArcPaint;
    private Paint mTextPaint;


    private static final int DEFAULT_CIRCLE_COLOR = Color.YELLOW;
    private static final int DEFAULT_ARC_COLOR = Color.GREEN;
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 200;
    private static final int DEFAULT_START_ANGLE = -90;
    private static final float DEFAULT_PERCENT = 0.3f;
    private static final int DEFAULT_ANIM_DURATION = 1000;
    private static final int DEFAULT_FRAME_DURATION = 50;

    private int mCircleColor = DEFAULT_CIRCLE_COLOR;
    private int mArcColor = DEFAULT_ARC_COLOR;

    private int mWidth,mHeight;
    private int mCircleRadius,mArcRadius;
    private int mStartAngle; //圆弧起始角度


    //动画相关
    private float mTargetPercent;
    private int mAnimDuration = DEFAULT_ANIM_DURATION;
    private int mFrameDuration = DEFAULT_FRAME_DURATION;
    private float mIncPercentPerFrame;

    //数据
    private float mPercent; //百分比
    private String mText; //圆上显示的文字

    private Handler mLogicHandler;

    public PercentView(Context context) {
        this(context,null);
    }

    public PercentView(Context context, AttributeSet attrs){
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);

        mArcPaint = new Paint();
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStyle(Paint.Style.FILL);
        mArcPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(50);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        //设置默认参数
        mStartAngle = DEFAULT_START_ANGLE;
        mPercent = DEFAULT_PERCENT;
        mText = "demo";
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureSize(widthMeasureSpec, DensityUtils.dp2px(mContext,DEFAULT_WIDTH));
        int height = measureSize(heightMeasureSpec,DensityUtils.dp2px(mContext,DEFAULT_HEIGHT));
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mArcRadius = (int)(Math.min(mWidth,mHeight)/2*0.9f);
        mCircleRadius = Math.min(mWidth,mHeight)/4;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mWidth/2,mHeight/2);
        //绘制外围圆弧 通过遮罩层实现
        /*int count = canvas.saveLayer(new RectF(-mArcRadius,-mArcRadius,mArcRadius,mArcRadius),mArcPaint);
        canvas.drawArc(new RectF(-mArcRadius,-mArcRadius,mArcRadius,mArcRadius),mStartAngle,(int)(mPercent * 360),true,mArcPaint);
        mArcPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        int mSmallArcRadius = (int)(mArcRadius * 0.8);
        canvas.drawArc(new RectF(-mSmallArcRadius,-mSmallArcRadius,mSmallArcRadius,mSmallArcRadius),mStartAngle,(int)(mPercent * 360),true,mArcPaint);
        mArcPaint.setXfermode(null);
        canvas.restoreToCount(count);*/
        //通过构造path实现
        canvas.drawPath(buildPath(),mArcPaint);
        //绘制内层圆形
        canvas.drawCircle(0,0,mCircleRadius,mCirclePaint);
        //绘制文字
        Rect rect = new Rect();
        String perText = String.valueOf((int)(mPercent*100)+"%");
        mTextPaint.getTextBounds(perText,0,perText.length(),rect);
        canvas.drawText(perText,-rect.width()/2,rect.height()/2,mTextPaint);
        canvas.restore();
    }

    private Path buildPath(){
        Path res = new Path();
        int sweepAngle = (int)(mPercent * 360);
        //外围圆弧
        Path outPath = new Path();
        RectF outRectF = new RectF(-mArcRadius,-mArcRadius,mArcRadius,mArcRadius);
        outPath.addArc(outRectF,mStartAngle,(int)(mPercent * 360));
        PathMeasure pathMeasure = new PathMeasure(outPath,false);
        float[] startPos1 = new float[2];
        float[] endPos1 = new float[2];
        pathMeasure.getPosTan(0,startPos1,null);
        pathMeasure.getPosTan(pathMeasure.getLength(),endPos1,null);


        //里面圆弧
        int mSmallArcRadius = (int)(mArcRadius * 0.8);
        Path inPath = new Path();
        RectF inRectF = new RectF(-mSmallArcRadius,-mSmallArcRadius,mSmallArcRadius,mSmallArcRadius);
        inPath.addArc(inRectF,mStartAngle,sweepAngle);
        pathMeasure.setPath(inPath,false);
        float[] startPos2 = new float[2];
        float[] endPos2 = new float[2];
        pathMeasure.getPosTan(0,startPos2,null);
        pathMeasure.getPosTan(pathMeasure.getLength(),endPos2,null);

        //构造path
        res.moveTo(startPos1[0],startPos1[1]);
        res.arcTo(outRectF,mStartAngle,sweepAngle,false);
        res.moveTo(endPos1[0],endPos1[1]);
        res.arcTo(inRectF,mStartAngle+sweepAngle,-sweepAngle,false);
        res.lineTo(startPos1[0],startPos1[1]);

        return res;
    }

    //对外接口

    /**
     * 静态设置百分比
     * @param percent
     */
    public void setPercent(float percent){
        if(percent > 1) percent = 1.0f;
        if(percent < 0) percent = 0.0f;
        mPercent = percent;
        postInvalidate();
    }

    /**
     * 设置初始角度
     * @param startAngle
     */
    public void setStartAngle(int startAngle){
        mStartAngle = startAngle;
    }


    /**
     * 设置中心显示的文字
     * @param text
     */
    public void setText(String text){
        mText = text;
    }


    /**
     * 带动画效果
     * @param percent
     */
    public void setPercentAnim(float percent){
        if(percent > 1) percent = 1.0f;
        if(percent < 0) percent = 0.0f;
        mTargetPercent  = percent;
        mPercent = 0;
        mIncPercentPerFrame = (mFrameDuration / (float) mAnimDuration) * percent;
        getLogicHandler().sendEmptyMessageDelayed(1,mFrameDuration);
    }

    private Handler getLogicHandler(){
        if(mLogicHandler != null){
            return mLogicHandler;
        }
        mLogicHandler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        mPercent += mIncPercentPerFrame;
                        invalidate();
                        if(mPercent >= mTargetPercent){
                            sendEmptyMessage(2);
                        }else{
                            sendEmptyMessageDelayed(1,mFrameDuration);
                        }
                        break;
                    case 2:
                        mTargetPercent = 0;
                        invalidate();
                        break;
                }
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

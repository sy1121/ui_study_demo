package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * yishe
 * 2019-07-09
 */
public class HexagonLoadingView extends View{

    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HIGHT = 50;
    private static final int COLOR_1 = 0x001F59FE;
    private static final int COLOR_2 = 0xFF1F59FE;
    private static final int COLOR_3 = 0xFF00CCFF;

    public static final int TYPE_NOMAL = 1;
    public static final int TYPE_SPECIAL = 2;

    private Context mContext;
    private Paint mHexagonPaint;
    private Path mPath;
    private SweepGradient mSweepGradient;

    private int mType = TYPE_SPECIAL;
    private int mCenterX, mCenterY;

    private static final float SQRT3 = (float)Math.sqrt(3);

    private int mShaderDegrees = 0;
    private Matrix mMatrix = new Matrix();

    private volatile  boolean mRunning = false;
    private int mStrokeWidth;
    private int mDiffDegree = 7;


    public HexagonLoadingView(Context context) {
        this(context,null);
    }

    public HexagonLoadingView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mStrokeWidth = dp2px(context,3);
        mPath = new Path();
        mHexagonPaint = new Paint();
        mHexagonPaint.setStrokeWidth(mStrokeWidth);
        mHexagonPaint.setStyle(Paint.Style.STROKE);
        mHexagonPaint.setAntiAlias(true);
        mHexagonPaint.setShader(mSweepGradient);
        setType(mType);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(dp2px(mContext,DEFAULT_WIDTH),dp2px(mContext,DEFAULT_HIGHT));
        }else if(widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(dp2px(mContext,DEFAULT_WIDTH),heightSize);
        }else if(heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize,dp2px(mContext,DEFAULT_HIGHT));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        int radius =(int)(Math.min(mCenterX, mCenterY) * 0.9f);
        mPath = getHexagon(radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mRunning){
            canvas.translate(mCenterX, mCenterY);
            mMatrix.setRotate(mShaderDegrees);
            mSweepGradient.setLocalMatrix(mMatrix);
            canvas.drawPath(mPath, mHexagonPaint);

            mShaderDegrees = mShaderDegrees + mDiffDegree;
            if(mShaderDegrees > 360){
                mShaderDegrees = mShaderDegrees % 360;
            }

            invalidate();
        }
    }

    private  Path getHexagon(int radius){
        Path path = new Path();
        float dx = SQRT3 * radius / 2;
        path.moveTo(0, radius );
        path.lineTo(0 - dx, radius / 2);
        path.lineTo(0 - dx, 0 - radius / 2);
        path.lineTo(0, 0 - radius);
        path.lineTo(dx, 0 - radius / 2);
        path.lineTo(dx, radius / 2);
        path.close();

        return path;
    }

    /**
     * 开始动画
     *
     */
    public void startAnimation(){
        if(mRunning){
            return ;
        }

        mRunning = true;
        postInvalidate();
    }

    /**
     * 停止动画
     */
    public void stopAnimation(){
        mRunning = false;
        postInvalidate();
    }

    public boolean isRunning(){return mRunning;}


    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    /**
     * 设置菊花旋转样式
     */
    public void setLoadingViewByType(int type){
        this.mType = type;
        setType(type);
    }

    /**
     * 设置画笔大小
     * @param strokeWidth  单位 dp
     */
    public void setStrokeWidth(int strokeWidth){
        this.mStrokeWidth = dp2px(mContext,strokeWidth);
        mHexagonPaint.setStrokeWidth(mStrokeWidth);
    }

    /**
     * 设置旋转的速度（数字越大旋转越快）
     * @param speed
     */
    public void setSpeed(int speed){
        this.mDiffDegree = speed;
    }

    private void setType(int type){
        if(type == TYPE_NOMAL){
            mSweepGradient = new SweepGradient(0,0,0x00000000,0x33000000);
        }else if(type == TYPE_SPECIAL){
            float[] degrees = new float[]{0,60,90,120,300,301,360};
            for(int i = 0; i < degrees.length; i++){
                degrees[i] = degrees[i] / 30;
            }
            mSweepGradient = new SweepGradient(0,0,
                    new int[]{COLOR_1, COLOR_2, COLOR_2, COLOR_3, COLOR_3, COLOR_1,COLOR_1},degrees);
        }
        mHexagonPaint.setShader(mSweepGradient);
    }


    private int dp2px(Context context,float dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal,context.getResources().getDisplayMetrics());
    }


}

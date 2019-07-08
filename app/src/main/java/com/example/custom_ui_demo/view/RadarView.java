package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * yishe
 * 2019-07-07
 */
public class RadarView extends View{

    private static final String TAG = "RadarView";
    private static final int HEXAGON_STROKE_MAX = 6; //六边形画笔的做大宽度
    private static final int MSG_LOGIC_CYCLE = 1;
    private static final int MSG_LOGIC_END = 2;
    private static final int COLOR_0 = 0x00ffffff; //渐变过渡色
    private static final int COLOR_100 = Color.WHITE;
    private static final int HEXAGON_COUNT = 2; //六边形数量的最大值
    private static final int ROTATE_TIME = 1000; //渐变效果一圈的时间
    private static final int TRANSLATE_TIME = 2000;
    private static final int FRAME_TIME = 40;//每帧动画停留时间
    private static final int FRAME_COUNT = TRANSLATE_TIME / FRAME_TIME;
    private static final int ROTATE_COUNT = ROTATE_TIME / FRAME_TIME;
    private static final int STATE_STOP = 1;
    private static final int STATE_RUNNING = 2;
    private static final int STATE_FADE_OUT = 3;

    private Paint mHexagonPaint;//六边形画笔
    private SweepGradient mSweepGradient;
    private List<Hexagon> mHexagons;
    private int mHexagonStrokeMax;
    private DecelerateInterpolator mInterpolator;
    private float mRadio;
    private boolean mPauseDecelerate;

    private Handler mLogicHandler;
    private RectF mCanvasRect;
    private RectF mDiamondRect;

    private int mDistanceMax;
    private int mHexagonMargin;
    private int mCenterX,mCenterY,mRadius,mStartRadius,mAlphaRadius;
    private static final float SQRT3 = (float)Math.sqrt(3);
    //用于旋转扫描渐变的矩阵
    private Matrix mMatrix = new Matrix();
    private int mShaderDegree = 0; //渐变旋转的角度
    private Bitmap mDiamondBitmap;

    private int mState = STATE_STOP;
    private boolean isFix = false;
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHexagons.clear();
            List<Hexagon> list = (List<Hexagon>) msg.obj;
            if(list != null){
                for(int i = 0;i < list.size(); i++){
                    mHexagons.add(list.get(i));
                }
            }
            invalidate();
        }
    };



    public RadarView(Context context) {
        this(context,null);
    }

    public RadarView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        mHexagonPaint = new Paint();
        mHexagonPaint.setStrokeWidth(10);
        mHexagonPaint.setColor(Color.WHITE);
        mHexagonPaint.setStyle(Paint.Style.STROKE);
        mHexagonPaint.setAntiAlias(true);
        mSweepGradient = new SweepGradient(0,0,
                new int[]{COLOR_0,COLOR_0,COLOR_100,COLOR_0,COLOR_0,COLOR_100,COLOR_0,COLOR_0,COLOR_100},
                new float[]{0f,0.083f,0.333f,0.334f,0.417f,0.666f,0.667f,0.075f,1f});
        mHexagonPaint.setShader(mSweepGradient);

        mHexagons = new ArrayList<>();
        mHexagonStrokeMax = DensityUtils.dp2px(context,HEXAGON_STROKE_MAX);
        mInterpolator = new DecelerateInterpolator(2f);

        mDiamondBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_diamond,null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mRadius = Math.min(mCenterX,mCenterY);
        mStartRadius = mRadius * 2/3;
        mAlphaRadius = mStartRadius + (mRadius - mStartRadius)/10;
        mHexagonMargin = (mRadius - mStartRadius)/HEXAGON_COUNT;
        mCanvasRect = new RectF(0,0,getWidth(),getHeight());
        mDistanceMax = mRadius - mStartRadius;
        mDiamondRect = new RectF(-mStartRadius,-mStartRadius,mStartRadius,mStartRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
        int layId = canvas.saveLayer(mCanvasRect,null,Canvas.ALL_SAVE_FLAG);
        //画六边形
        canvas.translate(mCenterX,mCenterY);
        for(int i = 0;i < mHexagons.size();i++){
            mMatrix.setRotate(mShaderDegree + i * 30);
            mSweepGradient.setLocalMatrix(mMatrix);
            Hexagon h = mHexagons.get(i);
            mHexagonPaint.setStrokeWidth(h.width);
            mHexagonPaint.setAlpha(getAlpha(h.radius));
            canvas.drawPath(h.path,mHexagonPaint);
        }
        canvas.restoreToCount(layId);
        mShaderDegree = mShaderDegree + (int)(360 / ROTATE_COUNT * mRadio);
        if(mShaderDegree >= 360){
            mShaderDegree = mShaderDegree - 360;
        }

        //绘制里面钻石图
        canvas.save();
        canvas.translate(mCenterX,mCenterY);
        canvas.drawBitmap(mDiamondBitmap,new Rect(0,0,mDiamondBitmap.getWidth(),mDiamondBitmap.getHeight()),mDiamondRect,null);
        canvas.restore();
    }

    private int getAlpha(int radius){
        if(radius < mStartRadius){
            return 0;
        }else if(radius < mAlphaRadius){
            return (radius - mStartRadius) * 255/(mAlphaRadius - mStartRadius);
        }else if(radius < mRadius){
            return (mRadius - radius) * 255 /(mRadius - mAlphaRadius);
        }else {
            return 0;
        }
    }

    private Handler getmLogicHandler(){
        Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_LOGIC_CYCLE:
                        if(mState == STATE_STOP){
                            return ;
                        }
                        int i = msg.arg1;
                        if(i > FRAME_COUNT){
                            i = 1;
                            mPauseDecelerate = true;
                        }
                        float f = (float)i/FRAME_COUNT;
                        if(isFix){
                            f = 15/(float) FRAME_COUNT;
                        }
                        if(!mPauseDecelerate){
                            mRadio = mInterpolator.getInterpolation(f);
                        }
                        int curRadius = (int)(mDistanceMax * f) + mStartRadius;
                        mHandler.sendMessage(mHandler.obtainMessage(0,getHexagons(curRadius)));

                        if(mState == STATE_FADE_OUT){
                            mLogicHandler.sendMessageDelayed(mLogicHandler.obtainMessage(MSG_LOGIC_END,1,0),FRAME_TIME);
                        }else if(mState == STATE_RUNNING){
                            mLogicHandler.sendMessageDelayed(mLogicHandler.obtainMessage(MSG_LOGIC_CYCLE,i + 1,0),FRAME_TIME);
                        }
                        break;
                    case MSG_LOGIC_END:
                        if(mState != STATE_FADE_OUT){
                            return  ;
                        }
                        int j = msg.arg1;
                        float f2 = (float)j / FRAME_COUNT;
                        int outCurRadius = (int)(mDistanceMax * f2) + mStartRadius;

                        mRadio = mInterpolator.getInterpolation(f2);
                        mHandler.sendMessage(mHandler.obtainMessage(0,getFrameOutHexagons(outCurRadius)));
                        if(j <= FRAME_COUNT){
                            mLogicHandler.sendMessageDelayed(mLogicHandler.obtainMessage(MSG_LOGIC_END,j + 1,0),FRAME_TIME);
                        }else{
                            mState = STATE_STOP;
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(mAnimListener != null){
                                        mAnimListener.onAnimEnd();
                                    }
                                }
                            });
                        }

                }
            }
        };
        return handler;
    }

    private Hexagon getHexagon(int radius){
        Hexagon hexagon = new Hexagon();
        float f =(float)(radius - mStartRadius)/(float)(mRadius - mStartRadius);
        if(isFix){
            hexagon.width = HEXAGON_STROKE_MAX * 2/3;
        }else{
            hexagon.width = HEXAGON_STROKE_MAX - (int)(HEXAGON_STROKE_MAX * f);
        }
        //draw a hexagon path
        Path path = new Path();
        float dx = SQRT3 * radius / 2;
        path.moveTo(0,radius);
        path.lineTo(-dx,radius/2);
        path.lineTo(-dx,0-radius/2);
        path.lineTo(0,0-radius);
        path.lineTo(dx,0-radius/2);
        path.lineTo(dx,radius/2);
        path.close();
        hexagon.path = path;
        hexagon.radius = radius;
        return hexagon;
    }

    private List<Hexagon> getHexagons(int radius){
        List<Hexagon> list =new ArrayList<Hexagon>();
        list.add(getHexagon(radius));
        if(radius + mHexagonMargin < mRadius){
            list.add(getHexagon(radius + mHexagonMargin));
        }else {
            list.add(getHexagon(radius - mHexagonMargin));
        }
        return list;
    }

    private List<Hexagon> getFrameOutHexagons(int radius){
        List<Hexagon> list=new ArrayList<Hexagon>();
        while(radius < mRadius){
            list.add(getHexagon(radius));
            radius = radius + mHexagonMargin;
        }
        return list;
    }

    //六边形
    class Hexagon{
        Path path;
        int width;
        int radius;
    }


    //动画结束监听
    public interface  AnimListener{
        void onAnimEnd();
    }

    public void setmAnimListener(AnimListener mAnimListener) {
        this.mAnimListener = mAnimListener;
    }

    public  AnimListener mAnimListener;

    /**
     * 开始动画
     */
    public void startAnimation() {
        mPauseDecelerate = false;
        if(mLogicHandler == null){
            mLogicHandler = getmLogicHandler();
        }
        if(mState == STATE_STOP){
            mState =STATE_RUNNING;
            mLogicHandler.sendMessage(mLogicHandler.obtainMessage(MSG_LOGIC_CYCLE));
        }
    }


    public boolean  isRunning(){ return mState != STATE_STOP;}

    /**
     * 淡出动画
     */
    public void fadeOutAnimation() {
        mState = STATE_FADE_OUT;
    }

    /**
     * 停止动画
     */
    public void stopAnimation() {
        mState = STATE_STOP;
    }


}

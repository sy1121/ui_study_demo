package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.custom_ui_demo.R;

import java.util.Random;

/**
 * @author yishe
 * @date 2017/9/28.
 * email：yishe@tencent.com
 * description：
 */
public class SpeedUpBall extends View{
    private static final String TAG = "SpeedUpBall";

    private static final int MSG_LOGIC_CYCLE = 1000;
    private static final int MSG_LOGIC_END = 1001;

    private static final int STATE_STOP = 1;
    private static final int STATE_RUN = 2;

    private Bitmap backgroundBitmap,rocketBitmap;
    private Paint textPaint,bitmapPaint;
    private Matrix maskMatrix;
    private int mWidth,mHeight;
    private int rocketWidth,rocketHeight;
    private int backgroundWidth,backgroundHeight;
    private int mState;

    private int maxRate; //每次点击时设置的rate最大值
    private int curRate; //动画过程中rate的值
    private String curText; //显示的当前rate字符串

    /**
     * 动画周期时间
     */
    private static final int TEXT_CHANGE_TIME=2300;
    /**
     * 每帧动画停留的时间
     */
    private static final int FRAME_TIME = 100;
    /**
     * 动画帧数
     */
    private static final int TEXT_FRAME_COUNT= TEXT_CHANGE_TIME/FRAME_TIME;

    /**
     * 每条曲线的x方向的跨度
     */
    private static final int CURL_X_SPAN=120;

    private static final int CURL_Y_AMPLITUDE=30;


    private Context context;

    private Handler mLogicHandler;

    private Handler mHandler=new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    invalidate();
                    break;
                case 2:
                    mState=STATE_STOP;
                    invalidate();
                    break;
                default:break;
            }
        }
    };

    public SpeedUpBall(Context context){
        this(context,null);
    }

    public SpeedUpBall(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public SpeedUpBall(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init(context);
    }

    private void init(Context context){
        this.context = context;

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(50);

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setDither(true);
        bitmapPaint.setFilterBitmap(true);

        backgroundBitmap=((BitmapDrawable)getResources().getDrawable(R.mipmap.rectangle)).getBitmap();
        rocketBitmap =((BitmapDrawable)getResources().getDrawable(R.mipmap.rocket)).getBitmap();
        backgroundWidth = backgroundBitmap.getWidth();
        backgroundHeight =backgroundBitmap.getHeight();
        rocketWidth = rocketBitmap.getWidth();
        rocketHeight = rocketBitmap.getHeight();

        maskMatrix = new Matrix();

        mState = STATE_STOP;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec,backgroundWidth);
        int height= measureHeight(heightMeasureSpec,backgroundHeight);
        setMeasuredDimension(width,height);
    }

    private int measureWidth(int widthMeasureSpec,int backgroundWidth){
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width=0;
        if(mode==MeasureSpec.EXACTLY){
            width = size;
        }else{
            width=backgroundWidth;
        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec,int backgroundHeight){
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int height=0;
        if(mode==MeasureSpec.EXACTLY){
            height = size;
        }else{
            height=backgroundHeight;
        }
        return height;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight =h;
        Log.i(TAG,"mWidth = "+mWidth+" mHeight= "+ mHeight);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mState==STATE_STOP){
            canvas.drawBitmap(backgroundBitmap,0,0,bitmapPaint);
            int left= (mWidth-rocketWidth)/2;
            int top = (mHeight -rocketHeight)/2;
            canvas.drawBitmap(rocketBitmap,left,top,bitmapPaint);
        }else{
            //绘制背景
            canvas.drawBitmap(backgroundBitmap,0,0,bitmapPaint);
            //绘制波纹
            int layer =canvas.saveLayer(0,0,mWidth,mHeight,null,Canvas.ALL_SAVE_FLAG);
            maskMatrix.setScale(0.95f,0.95f,mWidth/2.0f,mHeight/2.0f); //按中心缩放，作为遮罩层
            canvas.drawBitmap(backgroundBitmap,maskMatrix,bitmapPaint);
            bitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //当前波纹的高度
            float t=  backgroundHeight*(maxRate-curRate+0.00001f)/maxRate;
            canvas.drawBitmap(drawWave(),0,t,bitmapPaint);
            canvas.restoreToCount(layer);
            bitmapPaint.setXfermode(null);

            //绘制文字
            Rect textRect=new Rect();
            textPaint.getTextBounds(curText,0,curText.length(),textRect);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int left= (mWidth-textRect.width())/2;
            int top = (int)(mHeight/2+(fontMetrics.descent-fontMetrics.ascent)/2-fontMetrics.descent);
            canvas.drawText(curText,left,top,textPaint);
        }
    }

    /**
     * 绘制上边是波纹的矩形 ， 波纹效果由多个三阶贝塞尔曲线绘制而成
     * @return
     */
    private Bitmap drawWave(){
        Bitmap  waveBitmap = Bitmap.createBitmap(backgroundWidth,backgroundHeight, Bitmap.Config.ARGB_8888);
        Canvas waveCanvas = new Canvas(waveBitmap);
        //构造波纹图像路径
        Random random =new Random(System.currentTimeMillis());
        Path curlPath = new Path();
        curlPath.moveTo(0,random.nextInt(CURL_Y_AMPLITUDE));
        int endPointx,controlx1,controlx2,controly1,controly2; //三阶贝塞尔曲线的控制点坐标
        int lineCount = backgroundWidth/CURL_X_SPAN+1;
        int lineWidth = CURL_X_SPAN*lineCount;
        for(int i=1;i<lineCount+1;i++){
            endPointx=i*CURL_X_SPAN;
            controlx1= endPointx-Math.abs(random.nextInt(CURL_X_SPAN/2))-CURL_X_SPAN/2;
            controlx2 =endPointx-Math.abs(random.nextInt(CURL_X_SPAN/2));
            controly1 =random.nextInt(CURL_Y_AMPLITUDE);
            controly2=random.nextInt(CURL_Y_AMPLITUDE);
            curlPath.cubicTo(controlx1,controly1,controlx2,controly2,endPointx,random.nextInt(CURL_Y_AMPLITUDE));
        }
        curlPath.lineTo(lineWidth,backgroundHeight);
        curlPath.lineTo(0,backgroundHeight);
        curlPath.close();
        Paint paint= new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.shallowBlue));
        waveCanvas.drawPath(curlPath,paint);
        return waveBitmap;
    }


    public void startAnim(){
        if(mLogicHandler==null){
            mLogicHandler = getLogicHandler();
        }
        if(mState== STATE_STOP){
            mState = STATE_RUN;
            mLogicHandler.sendMessage(mLogicHandler.obtainMessage(MSG_LOGIC_CYCLE,1,0));
        }
    }
    private Handler getLogicHandler(){
        Handler handler =new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_LOGIC_CYCLE: {
                        if (mState == STATE_STOP) return;
                        int i = msg.arg1;
                        if(i > TEXT_FRAME_COUNT){
                            mState = STATE_STOP;
                        }

                        if(curRate % 5 != 0){
                            curRate = curRate - curRate % 5;
                        }else{
                            curRate -= 5;
                        }
                        if(curRate<0){
                            curText = "完成";
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(2),FRAME_TIME);
                        }else{
                            curText = curRate+"%";
                        }
                        mHandler.sendEmptyMessage(1);
                        if(mState == STATE_RUN){
                            mLogicHandler.sendMessageDelayed(mLogicHandler.obtainMessage(MSG_LOGIC_CYCLE,i+1,0),FRAME_TIME);
                        }else{
                            mLogicHandler.sendMessageDelayed(mLogicHandler.obtainMessage(MSG_LOGIC_END,1,0),FRAME_TIME);
                        }
                        break;
                    }
                    case MSG_LOGIC_END:{
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }
        };
        return handler;
    }

    public void setCurRate(int rate) {
        if(mState != STATE_STOP) return ;
        maxRate=rate;
        curRate = maxRate;
        startAnim();
    }
}


package com.example.custom_ui_demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.custom_ui_demo.R;


public class CheckView extends View {

    private static final int ANIM_NULL = 0; //动画状态 -- 没有
    private static final int ANIM_CHECK = 1; //动画状态 -- 开启
    private static final int ANIM_UNCHECK = 2;//动画状态 -- 结束

    private Context mContext;
    private int mWidth, mHeight;
    private Handler mHandler;

    private Paint mPaint;
    private Bitmap okBitmap;

    private int animCurrentPage = -1;  //当前页码
    private int animMaxPage = 13; //总页数
    private int animDuration = 500; // 动画时长
    private int animState = ANIM_NULL; // 动画状态

    private boolean isCheck = false; //是否是选中状态


    public CheckView(Context context){
        this(context,null);
    }
    public CheckView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    /**
     *  初始化
     * @param context
     */
    private void init(Context context){
        mContext = context;

        mPaint = new Paint();
        mPaint.setColor(0xffFF5317);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        okBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.checkmark);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(animCurrentPage < animMaxPage && animCurrentPage >= 0){
                    invalidate();
                    if(animState == ANIM_NULL)
                        return ;
                    if(animState == ANIM_CHECK){
                        animCurrentPage++;
                    }else{
                        animCurrentPage--;
                    }
                    this.sendEmptyMessageDelayed(0,animDuration / animMaxPage);
                    Log.e("AAA","count=" + animCurrentPage);
                }else {
                    if(isCheck){
                        animCurrentPage = animMaxPage - 1;
                    }else{
                        animCurrentPage = -1;
                    }
                    invalidate();
                    animState = ANIM_NULL;
                }
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //移动坐标到画布中央
        canvas.translate(mWidth/2,mHeight/2);

        //绘制背景圆形
        canvas.drawCircle(0,0,240,mPaint);

        //得出图形边长
        int sizeLength = okBitmap.getHeight();

        //得到图形选区 和 实际绘制区域
        Rect src = new Rect(sizeLength*animCurrentPage,0,sizeLength*(animCurrentPage+1),sizeLength);
        Rect dst = new Rect(-200,-200,200,200);

        //绘制
        canvas.drawBitmap(okBitmap,src,dst,null);
    }

    /**
     * 选择
     */
    public void check(){
        if(animState != ANIM_NULL || isCheck){
            return ;
        }
        animState = ANIM_CHECK;
        animCurrentPage = 0;
        mHandler.sendEmptyMessageDelayed(0,animDuration / animMaxPage);
        isCheck = true;
    }

    /**
     * 取消选择
     */
    public void unCheck(){
        if(animState != ANIM_NULL || (!isCheck)){
            return ;
        }
        animState = ANIM_UNCHECK;
        animCurrentPage = animMaxPage -1;
        mHandler.sendEmptyMessageDelayed(0,animDuration/animMaxPage);
        isCheck = false;
    }

    /**
     * 设置动画时长
     * @param animDuration
     */
    public void setAnimDuration(int animDuration){
        if(animDuration <= 0){
            return ;
        }
        this.animDuration = animDuration;
    }

    /**
     * 设置背景圆形颜色
     * @param color
     */
    public void setBackgroundColor(int color){
        mPaint.setColor(color);
    }
}

package com.example.custom_ui_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;


/**
 * yishe
 * 2019/7/6
 */
public class DrawView extends View{
    private static final String TAG = "DrawView";
    private Context mContext;

    private int mStartX;
    private int mStartY;
    private int mLastXOffSet;
    private int mLastYOffSet;
    private int mTouchSlop;

    private Scroller mScroller;

    public DrawView(Context context) {
        this(context,null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        mScroller = new Scroller(mContext);
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"ACTION_DOWN");
                mLastXOffSet = x;
                mLastYOffSet = y;
                mStartX = x;
                mStartY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG,"ACTION_MOVE");
                int dx = x - mLastXOffSet;
                int dy = y - mLastYOffSet;
                /*offsetLeftAndRight(dx);
                offsetTopAndBottom(dy);*/
                if(Math.abs(dx) < mTouchSlop && Math.abs(dy) < mTouchSlop ){
                    dx = 0;
                    dy = 0;
                }
                layout(getLeft() +dx ,getTop() + dy,getRight() + dx,getBottom() + dy);
                mLastXOffSet = x;
                mLastYOffSet = y;
                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll(getLeft(),getTop(),-100,-100,3000);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }
}

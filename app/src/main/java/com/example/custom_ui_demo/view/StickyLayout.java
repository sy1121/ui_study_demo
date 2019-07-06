package com.example.custom_ui_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.example.custom_ui_demo.util.DensityUtils;

/**
 * yishe
 * 2019/7/6
 */
public class StickyLayout extends ViewGroup{

    private static final String TAG = "StickyLayout";

    private Context mContext;
    private int mScreenHeight;
    private int mLastY;
    private int mStart,mEnd;//开始和结束滑动的Y坐标
    private Scroller mScroller;

    public StickyLayout(Context context) {
        this(context,null);
    }

    public StickyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        mScreenHeight = DensityUtils.getWindowHeight(mContext.getApplicationContext());
        mScroller = new Scroller(mContext);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0;i < count;i++){
            View childView = getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        //设置ViewGroup的高度
        MarginLayoutParams mlp = (MarginLayoutParams)getLayoutParams();
        mlp.height = mScreenHeight * childCount;
        setLayoutParams(mlp);
        for(int i = 0;i < childCount;i++){
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE){
                child.layout(l,i * mScreenHeight,r,(i + 1)*mScreenHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int)event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy = mLastY - y;
                if(getScrollY() < 0){
                    dy = 0;
                }
                if(getScrollY() > mScreenHeight * (getChildCount() - 1)){
                    dy = 0;
                }
                scrollBy(0,dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mEnd = getScrollY();
                int dScrollY = mEnd - mStart;
                if(dScrollY > 0){
                    if(dScrollY < mScreenHeight / 3){
                        mScroller.startScroll(0,getScrollY(),
                                0,-dScrollY);
                    }else{
                        mScroller.startScroll(0,getScrollY(),0,mScreenHeight-dScrollY);
                    }
                }else{
                    if(-dScrollY < mScreenHeight / 3){
                        mScroller.startScroll(
                                0,getScrollY(),
                                0,-dScrollY);
                    }else{
                        mScroller.startScroll(
                                0,getScrollY(),
                                0,-mScreenHeight - dScrollY);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(0,mScroller.getCurrY());
            postInvalidate();
        }
    }
}

package com.example.custom_ui_demo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * yishe
 * 2019/7/6
 */
public class DrawViewGroup extends FrameLayout {
    private static final String TAG = "DrawViewGroup";

    private ViewDragHelper mViewDragHelper;
    private View mMenuView,mMainView;
    private int mWidth;
    public DrawViewGroup(Context context) {
        super(context);
        initView();
    }

    public DrawViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DrawViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMenuView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件传递给ViewDragHelper,此操作必不可少
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    private void initView(){
        mViewDragHelper = ViewDragHelper.create(this,callback);
    }

    private ViewDragHelper.Callback callback =
            new ViewDragHelper.Callback() {
                //何时开始检测触摸事件
                @Override
                public boolean tryCaptureView(@NonNull View view, int i) {
                    return mMainView == view;  //滑动的MainView，左边的MenuView跟着滑动
                }

                //处理水平滑动
                @Override
                public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                    Log.i(TAG,"left = " + left);
                    if(left < 0) left = 0; //从右向左滑不处理
                    return Math.min(left,mWidth);
                }

                //处理垂直滑动
                @Override
                public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                    return 0;
                }

                //拖动结束后调用
                @Override
                public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                    super.onViewReleased(releasedChild, xvel, yvel);
                    //手指抬起后缓慢移动到指定位置
                    if(mMainView.getLeft() < 300){
                        //关闭菜单, 相当于strtScroll方法
                        mViewDragHelper.smoothSlideViewTo(mMainView,0,0);
                        ViewCompat.postInvalidateOnAnimation(DrawViewGroup.this);
                    }else{
                        //打开菜单
                        mViewDragHelper.smoothSlideViewTo(mMainView,mWidth,0);
                        ViewCompat.postInvalidateOnAnimation(DrawViewGroup.this);
                    }
                }

                //在用户触摸到View后回调
                @Override
                public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                    super.onViewCaptured(capturedChild, activePointerId);
                }

                //这个事件在拖拽状态改变时回调，比如idle ,dragging等状态
                @Override
                public void onViewDragStateChanged(int state) {
                    super.onViewDragStateChanged(state);
                }

                //这个事件在位置改变时回调，常用于滑动时更改scale进行缩放等效果
                @Override
                public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                    super.onViewPositionChanged(changedView, left, top, dx, dy);
                }
            };

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}

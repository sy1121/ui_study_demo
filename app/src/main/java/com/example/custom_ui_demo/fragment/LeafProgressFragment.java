package com.example.custom_ui_demo.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.util.AnimationUtils;
import com.example.custom_ui_demo.view.LeafLoadingView;

import java.util.Random;

import butterknife.BindView;

/**
 * yishe
 * 2019/7/7
 */
public class LeafProgressFragment extends BaseFragment {

    @BindView(R.id.leaf_loading)
    LeafLoadingView mLeafLoading;
    @BindView(R.id.fengshan)
    ImageView mFengShan;
    @BindView(R.id.finish_tips)
    TextView mFinishTips;

    private static final int REFRESH_PROGRESS = 0x10;
    private static final int REFRESH_STOP = 0x11;

    private int mProgress = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_PROGRESS:
                    if (mProgress < 40) {
                        mProgress += 2;
                        // 随机800ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(400));
                        mLeafLoading.setProgress(mProgress);
                    } else if(mProgress >= 100){
                        mHandler.sendEmptyMessage(REFRESH_STOP);
                    }else {
                        mProgress += 4;
                        // 随机1200ms以内刷新一次
                        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS,
                                new Random().nextInt(600));
                        mLeafLoading.setProgress(mProgress);

                    }

                    break;
                case REFRESH_STOP:
                    mFengShan.clearAnimation();
                    doFinishAnim();
                    break;
            }
        }
    };
    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        mHandler.sendEmptyMessageDelayed(REFRESH_PROGRESS, 3000);
        RotateAnimation progessRotateAnimation = AnimationUtils.initRotateAnimation(1500,0,360,true,Animation.INFINITE);
        mFengShan.startAnimation(progessRotateAnimation);
    }

    private void doFinishAnim(){
        ScaleAnimation dismissScale = AnimationUtils.initScaleAnimation(1000,1,0,1,0,true,0);
        mFengShan.startAnimation(dismissScale);
        dismissScale.setAnimationListener(new AnimationUtils.AnimationListerAdapter(){
            @Override
            public void onAnimationEnd(Animation animation) {
                mFinishTips.setVisibility(View.VISIBLE);
                doTipsAnim();
            }
        });
    }

    private void doTipsAnim(){
        ScaleAnimation tipsShowScale = AnimationUtils.initScaleAnimation(1000,0,1,0,1,true,0);
        mFinishTips.startAnimation(tipsShowScale);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_leaf_progress;
    }
}

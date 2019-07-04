package com.example.custom_ui_demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.view.SpeedUpBall;

import butterknife.BindView;

/**
 * @author yishe
 * @date 2019/7/4.
 * email：yishe@tencent.com
 * description：
 */
public class SpeedUpBallFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.speed_up_ball)
    SpeedUpBall mSpeedUpBall;
    @BindView(R.id.start_rate)
    SeekBar mRateBar;

    private static final int DEFAULT_START_RATE = 80;
    private int mStartRate = DEFAULT_START_RATE;

    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        initView();
    }


    private void initView(){
        mRateBar.setOnSeekBarChangeListener(this);
        mSpeedUpBall.setOnClickListener(this);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_speed_up_ball;
    }

    @Override
    public void onClick(View view) {
        if(mStartRate > 0){
            mSpeedUpBall.setCurRate(mStartRate);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mStartRate = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

package com.example.custom_ui_demo.fragment;

import android.os.Bundle;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.view.ClockView;

import butterknife.BindView;

/**
 * yishe
 * 2019/7/6
 */
public class ClockViewFragment extends BaseFragment {

    @BindView(R.id.clock_view)
    ClockView mClockView;

    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        mClockView.work();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_clock_view;
    }
}

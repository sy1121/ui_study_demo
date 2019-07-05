package com.example.custom_ui_demo.fragment;

import android.os.Bundle;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.view.PercentView;

import butterknife.BindView;

/**
 * @author yishe
 * @date 2019/7/5.
 * email：yishe@tencent.com
 * description：
 */
public class PercentViewFragment extends BaseFragment {

    @BindView(R.id.percent_view)
    PercentView mPercentView;

    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        //mPercentView.setStartAngle(0);
        mPercentView.setText("demo");
        mPercentView.setPercentAnim(0.7f);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_percent_view;
    }
}

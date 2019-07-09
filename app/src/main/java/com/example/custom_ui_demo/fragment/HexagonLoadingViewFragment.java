package com.example.custom_ui_demo.fragment;

import android.os.Bundle;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.view.HexagonLoadingView;

import butterknife.BindView;

/**
 * yishe
 * 2019-07-09
 */
public class HexagonLoadingViewFragment extends BaseFragment {

    @BindView(R.id.hexagon_loading_view)
    HexagonLoadingView mLoadingView;


    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        mLoadingView.setLoadingViewByType(HexagonLoadingView.TYPE_SPECIAL);
        mLoadingView.startAnimation();

    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_hexagon_loading_view;
    }
}

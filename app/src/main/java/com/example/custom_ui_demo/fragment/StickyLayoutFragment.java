package com.example.custom_ui_demo.fragment;

import android.os.Bundle;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;

/**
 * yishe
 * 2019/7/6
 */
public class StickyLayoutFragment extends BaseFragment {
    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_sticky_layout;
    }
}

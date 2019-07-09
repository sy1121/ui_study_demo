package com.example.custom_ui_demo.fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;

import butterknife.BindView;

/**
 * yishe
 * 2019-07-08
 */
public class RemoteControlFragment extends BaseFragment {

    @BindView(R.id.remote_control)
    RemoteControlView remoteControlView;
    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        remoteControlView.setmListener(new RemoteControlView.MenuListener() {
            @Override
            public void onCenterClicked() {
                Toast.makeText(mContext,"确定",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpClicked() {
                Toast.makeText(mContext,"上",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightClicked() {
                Toast.makeText(mContext,"右",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownClicked() {
                Toast.makeText(mContext,"下",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLeftClicked() {
                Toast.makeText(mContext,"左",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_remote_control;
    }
}

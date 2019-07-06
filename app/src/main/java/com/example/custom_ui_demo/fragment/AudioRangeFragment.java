package com.example.custom_ui_demo.fragment;

import android.os.Bundle;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.view.AudioRangeView;

import butterknife.BindView;

/**
 * yishe
 * 2019/7/6
 */
public class AudioRangeFragment extends BaseFragment {

    @BindView(R.id.audio_range)
    AudioRangeView mAudioRangeView;
    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_audio_range;
    }
}

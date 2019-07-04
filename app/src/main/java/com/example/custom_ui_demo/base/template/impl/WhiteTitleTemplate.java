package com.example.custom_ui_demo.base.template.impl;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.custom_ui_demo.base.template.BaseCommonTemplate;

/**
 * yishe
 * 2019/7/3
 */
public class WhiteTitleTemplate extends BaseCommonTemplate {
    public WhiteTitleTemplate(Context context) {
        super(context);
    }

    @Override
    public void addContentView(View contentView) {
        RelativeLayout.LayoutParams contentViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        mContainerLayout.addView(contentView, contentViewLayoutParams);
    }
}

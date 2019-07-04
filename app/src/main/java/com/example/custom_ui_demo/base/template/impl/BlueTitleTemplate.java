package com.example.custom_ui_demo.base.template.impl;

import android.content.Context;

import com.example.custom_ui_demo.R;

/**
 * yishe
 * 2019/7/3
 */
public class BlueTitleTemplate extends WhiteTitleTemplate {

    public BlueTitleTemplate(Context context) {
        super(context);
        mTitleLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
    }
}

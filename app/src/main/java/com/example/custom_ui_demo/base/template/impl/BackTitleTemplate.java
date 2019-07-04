package com.example.custom_ui_demo.base.template.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.example.custom_ui_demo.R;

/**
 * yishe
 * 2019/7/3
 */
public class BackTitleTemplate extends BlueTitleTemplate {
    public BackTitleTemplate(Context context,String title){
        super(context);
        mBackImg.setImageResource(R.mipmap.go_back);
        mBackImg.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title)) {
            mTitleText.setText(title);
            mTitleText.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String newTitle){
        if(!TextUtils.isEmpty(newTitle)) {
            mTitleText.setText(newTitle);
            mTitleText.setVisibility(View.VISIBLE);
        }
    }

}

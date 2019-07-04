package com.example.custom_ui_demo.base.template.impl;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.custom_ui_demo.base.template.BaseTemplate;

/**
 * yishe
 * 2019/7/2
 */
public class EmptyTemplate extends BaseTemplate {
    //整个页面的视图View
    protected LinearLayout mPageView;

    public EmptyTemplate(Context context) {
        super(context);
        mContext = context;
        mPageView = new LinearLayout(mContext);
    }

    @Override
    public void addContentView(View contentView) {
        RelativeLayout.LayoutParams contentViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        mPageView.addView(contentView, contentViewLayoutParams);
    }

    @Override
    public View getPageView() {
        return mPageView;
    }
}

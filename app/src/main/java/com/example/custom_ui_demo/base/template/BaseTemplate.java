package com.example.custom_ui_demo.base.template;

import android.content.Context;
import android.view.View;

public abstract class BaseTemplate {
    protected Context mContext;


    public BaseTemplate(Context context) {
        this.mContext = context;
    }


    /**
     * 添加页面实际body内容区域视图进去
     *
     * @param contentView 页面实际body内容区域视图
     */
    public abstract void addContentView(View contentView);

    /**
     * 获取模版产生的页面View
     *
     * @return 页面View
     */
    public abstract View getPageView();
}

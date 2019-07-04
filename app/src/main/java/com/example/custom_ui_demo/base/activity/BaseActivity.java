package com.example.custom_ui_demo.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.example.custom_ui_demo.base.template.BaseTemplate;
import com.example.custom_ui_demo.base.tools.StatusBarHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    protected Activity mContext;

    /**
     * 页面的真正的内容试图（除开了标题栏试图的）
     */
    protected View mContentView;

    //当前页面模板
    protected BaseTemplate mTemplate;
    private Unbinder mUnbinder;
    protected boolean isSetStatusBar = false;
    protected int mStatusBarColorId = android.R.color.white;
    protected  boolean isDarkTitleBar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mContext = this;
        mTemplate = createTemplate();
        if(mTemplate != null){
            if(getContentLayout() != 0){
                mContentView = LayoutInflater.from(mContext).inflate(getContentLayout(),null,false);
                mTemplate.addContentView(mContentView);
            }
            setContentView(mTemplate.getPageView());

            mUnbinder = ButterKnife.bind(this);
        }
        preSetContentView(savedInstanceState);
        doOnCreate(savedInstanceState);

        if(isSetStatusBar){
            StatusBarHelper.initStatusBar(this, mStatusBarColorId, isDarkTitleBar);
        }
    }

    @Override
    protected void onDestroy() {
        //解绑试图
        mUnbinder.unbind();
        super.onDestroy();
    }

    /**
     * 在SetContentView前执行
     *
     * @param savedInstanceState
     */
    protected void preSetContentView(Bundle savedInstanceState) {

    }

    /**
     * 创建模板，不能返回空，可以返回EmptyTemplate
     *
     * @return
     */
    protected abstract BaseTemplate createTemplate();

    /**
     * 返回内容view layout
     *
     * @return
     */
    protected abstract int getContentLayout();

    /**
     * 初始化视图
     */
    protected abstract void doOnCreate(Bundle savedInstanceState);

}

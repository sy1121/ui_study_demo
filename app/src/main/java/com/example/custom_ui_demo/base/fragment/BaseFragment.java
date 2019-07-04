package com.example.custom_ui_demo.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.custom_ui_demo.base.template.BaseTemplate;
import com.example.custom_ui_demo.base.template.impl.EmptyTemplate;
import com.example.custom_ui_demo.util.LayoutUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * yishe
 * 2019/7/3
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mContext;
    /**
     * 页面的真正的内容视图(除开了标题栏视图的)
     **/
    protected View mContentView;
    //当前页面模板
    protected BaseTemplate mTemplate;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mTemplate = createTemplate();
        if (mTemplate != null) {
            if(getContentLayout() != 0) {
                mContentView = LayoutUtil.inflate(mContext, getContentLayout(), null, false);
                mTemplate.addContentView(mContentView);
            }

            mUnbinder = ButterKnife.bind(this, mTemplate.getPageView());
        }

        doOnCreateView(savedInstanceState);

        return mTemplate.getPageView();
    }

    @Override
    public void onDetach() {
        //解绑视图
        mUnbinder.unbind();
        super.onDetach();
    }

    /**
     * 做初始化工作
     * @param savedInstanceState
     */
    protected abstract void doOnCreateView(Bundle savedInstanceState);

    /**
     * 创建模板，默认返回EmptyTemplate
     *
     * @return
     */
    protected BaseTemplate createTemplate() {
        return new EmptyTemplate(mContext);
    }

    /**
     * 返回内容view layout
     *
     * @return
     */
    protected abstract int getContentLayout();
}

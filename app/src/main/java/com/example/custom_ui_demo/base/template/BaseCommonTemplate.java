package com.example.custom_ui_demo.base.template;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.util.DensityUtils;

/**
 * yishe
 * 2019/7/3
 */
public abstract class BaseCommonTemplate extends BaseTemplate implements View.OnClickListener {
    private Context mContext;
    //整个页面的视图View
    public LinearLayout mPageView;
    //顶部的layout包含状态栏和title
    public LinearLayout mTopLayout;
    //透明通知栏时，自定义状态栏背景
    public View mStatusBar;
    /**
     * 标题栏布局区域
     **/
    public RelativeLayout mTitleLayout;
    public ImageView mLeftImg;
    public ImageView mBackImg;
    public TextView mLeftText;
    public TextView mTitleText;
    public ImageView mRightImg;
    public TextView mRightText;
    public ImageView mShadowImg;
    //内容显示区
    protected FrameLayout mContainerLayout;

    private View.OnClickListener iBackListener;

    protected BaseCommonTemplate(Context context) {
        super(context);

        mContext = context;

        mPageView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_common_title_template, null, false);

        mStatusBar = mPageView.findViewById(R.id.statusbar);
        mTitleLayout = (RelativeLayout) mPageView.findViewById(R.id.rl_title);
        mContainerLayout = (FrameLayout) mPageView.findViewById(R.id.container);

        mStatusBar.setVisibility(View.GONE);

        mLeftImg = (ImageView) mPageView.findViewById(R.id.img_left);
        mBackImg = (ImageView) mPageView.findViewById(R.id.img_back);
        mLeftText = (TextView) mPageView.findViewById(R.id.text_left);

        mTitleText = (TextView) mPageView.findViewById(R.id.text_title);

        mRightImg = (ImageView) mPageView.findViewById(R.id.img_right);
        mRightText = (TextView) mPageView.findViewById(R.id.text_right);

        mShadowImg = (ImageView) mPageView.findViewById(R.id.img_shadow);

        mBackImg.setOnClickListener(this);
    }

    @Override
    public View getPageView() {
        return mPageView;
    }

    public void setStatusBarVisibility(int visibility){
        mStatusBar.setVisibility(visibility);
    }

    public void setTitleLayoutVisibility(int visibility){
        mTitleLayout.setVisibility(visibility);
    }

    /**
     * 设置title高度
     * @param height 单位dp
     */
    public void setTitleHeight(int height){
        ViewGroup.LayoutParams params = mTitleLayout.getLayoutParams();
        params.height = DensityUtils.dp2px(mContext, height);
    }


    public void setBackOnClickListener(final View.OnClickListener listener) {
        iBackListener = listener;
    }

    public void setBackVisibility(int visibility){
        mBackImg.setVisibility(visibility);
    }

    public void setImageResource(int resid){
        mBackImg.setVisibility(View.VISIBLE);
        mBackImg.setImageResource(resid);
    }

    public void setTitleText(@NonNull String str){
        mTitleText.setVisibility(View.VISIBLE);
        mTitleText.setText(str);
    }

    public void setTitleColor(@NonNull int color){
        mTitleText.setTextColor(color);
    }

    public void setTitleColorRes(@NonNull int res){
        mTitleText.setTextColor(mContext.getResources().getColor(res));
    }

    public void setRightTextEnabled(boolean enabled) {
        mRightText.setEnabled(enabled);
    }

    public void setRightText(@NonNull String str){
        mRightText.setText(str);
    }

    public void setRightTextColor(@NonNull int color){
        mRightText.setTextColor(color);
    }

    public void setShadowVisibility(int visibility){
        mShadowImg.setVisibility(visibility);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.img_back){
            if(iBackListener != null){
                iBackListener.onClick(view);
            }else {
                if(mContext instanceof Activity){
                    ((Activity)mContext).finish();
                }
            }
        }
    }
}

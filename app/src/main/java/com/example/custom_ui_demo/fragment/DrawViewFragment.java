package com.example.custom_ui_demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.view.DrawView;

import butterknife.BindView;

/**
 * yishe
 * 2019/7/6
 */
public class DrawViewFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.draw_view)
    DrawView mDrawView;
    @BindView(R.id.scroll_by)
    Button mScrollBy;
    @BindView(R.id.scroll_to)
    Button mScrollTo;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.draw_container)
    LinearLayout mContainer;
    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        mScrollTo.setOnClickListener(this);
        mScrollBy.setOnClickListener(this);
        mText.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_draw_view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scroll_to:
                scrollTo(-100,-100);
                break;
            case R.id.scroll_by:
                scrollBy(-100,-100);
                break;
            case R.id.text:
                Toast.makeText(mContext,((View)mDrawView.getParent()).getScrollX()+"",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void scrollTo(int x,int y){
       // mText.scrollTo(x,y);
       // mContainer.scrollTo(x,y);
        ((View)mDrawView.getParent()).scrollTo(x,y);

    }

    private void scrollBy(int dx,int dy){
      //  mText.scrollBy(dx,dy);
      //  mContainer.scrollBy(dx,dy);
        ((View)mDrawView.getParent()).scrollBy(dx,dy);
    }
}

package com.example.custom_ui_demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.view.CheckView;

import butterknife.BindView;

public class CheckViewFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.check_view)
    CheckView mCheckView;
    @BindView(R.id.btn_check)
    Button mCheckBtn;
    @BindView(R.id.btn_uncheck)
    Button mUnCheckBtn;


    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        mCheckBtn.setOnClickListener(this);
        mUnCheckBtn.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_check_view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_check:
                mCheckView.check();
                break;
            case R.id.btn_uncheck:
                mCheckView.unCheck();
                break;
        }
    }


}

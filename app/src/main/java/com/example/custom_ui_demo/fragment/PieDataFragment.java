package com.example.custom_ui_demo.fragment;

import android.os.Bundle;

import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.entity.PieData;
import com.example.custom_ui_demo.view.PieView;

import java.util.ArrayList;

import butterknife.BindView;

public class PieDataFragment extends BaseFragment {

    @BindView(R.id.pie_view)
    PieView mPieView;


    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_pie_data;
    }


    private void initView(){
        ArrayList<PieData> list = new ArrayList<>();
        list.add(new PieData("优秀",20));
        list.add(new PieData("良好",40));
        list.add(new PieData("中等",30));
        list.add(new PieData("差",10));
        mPieView.setData(list);
    }



}

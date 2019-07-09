package com.example.custom_ui_demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.custom_ui_demo.adapter.DemoListAdapter;
import com.example.custom_ui_demo.base.activity.BaseActivity;
import com.example.custom_ui_demo.base.template.BaseTemplate;
import com.example.custom_ui_demo.base.template.impl.BackTitleTemplate;
import com.example.custom_ui_demo.fragment.AudioRangeFragment;
import com.example.custom_ui_demo.fragment.CheckViewFragment;
import com.example.custom_ui_demo.fragment.ClockViewFragment;
import com.example.custom_ui_demo.fragment.DrawViewFragment;
import com.example.custom_ui_demo.fragment.DrawViewGroupFragment;
import com.example.custom_ui_demo.fragment.GuaGuaViewFragment;
import com.example.custom_ui_demo.fragment.HexagonLoadingViewFragment;
import com.example.custom_ui_demo.fragment.LeafProgressFragment;
import com.example.custom_ui_demo.fragment.PercentViewFragment;
import com.example.custom_ui_demo.fragment.PieDataFragment;
import com.example.custom_ui_demo.fragment.RadarViewFragment;
import com.example.custom_ui_demo.fragment.RemoteControlFragment;
import com.example.custom_ui_demo.fragment.SpeedUpBallFragment;
import com.example.custom_ui_demo.fragment.StickyLayoutFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private String[] mData = {"饼状图","打勾图","加速球","比例图","音频条","粘性布局","拖动图",
    "侧边栏布局","时钟","刮刮卡","叶子进度条","清理加速雷达图","遥控器","六边形进度条"};
    private Class[] mFragment = {PieDataFragment.class, CheckViewFragment.class, SpeedUpBallFragment.class, PercentViewFragment.class, AudioRangeFragment.class,
    StickyLayoutFragment .class, DrawViewFragment.class, DrawViewGroupFragment.class, ClockViewFragment.class, GuaGuaViewFragment.class, LeafProgressFragment.class,
            RadarViewFragment.class, RemoteControlFragment.class, HexagonLoadingViewFragment.class
};

    @BindView(R.id.view_demo_list)
    ListView mListView;

    DemoListAdapter mAdapter;

    @Override
    protected BaseTemplate createTemplate() {
        return new BackTitleTemplate(this,"自定义View Demo");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void doOnCreate(Bundle savedInstanceState) {
        initView();
    }

    private void initView(){
        mAdapter = new DemoListAdapter(this,mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }


    private void jumpToContent(String tag,String title){
        Intent intent = new Intent(MainActivity.this,ContentActivity.class);
        intent.putExtra("fragment",tag);
        intent.putExtra("title",title);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position > mFragment.length - 1) return;
        jumpToContent(mFragment[position].getCanonicalName(),mData[position]);
    }
}

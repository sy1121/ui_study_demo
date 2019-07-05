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
import com.example.custom_ui_demo.fragment.CheckViewFragment;
import com.example.custom_ui_demo.fragment.PercentViewFragment;
import com.example.custom_ui_demo.fragment.PieDataFragment;
import com.example.custom_ui_demo.fragment.SpeedUpBallFragment;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private String[] mData = {"饼状图","打勾图","加速球","比例图"};
    private Class[] mFragment = {PieDataFragment.class, CheckViewFragment.class, SpeedUpBallFragment.class, PercentViewFragment.class};

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
        Log.i(TAG,"doOnCreate ");
        initView();
    }

    private void initView(){
        Log.i(TAG,"initView ");
        mAdapter = new DemoListAdapter(this,mData);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void jumpToContent(String tag,String title){
        Log.i(TAG,"jumpToContent tag = "+tag);
        Intent intent = new Intent(MainActivity.this,ContentActivity.class);
        intent.putExtra("fragment",tag);
        intent.putExtra("title",title);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"onItemClick position = "+position);
        if(position > mFragment.length - 1) return;
        jumpToContent(mFragment[position].getCanonicalName(),mData[position]);
    }
}

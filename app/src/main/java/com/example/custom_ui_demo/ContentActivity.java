package com.example.custom_ui_demo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.custom_ui_demo.base.activity.BaseActivity;
import com.example.custom_ui_demo.base.template.BaseTemplate;
import com.example.custom_ui_demo.base.template.impl.BackTitleTemplate;

import butterknife.BindView;


public class ContentActivity extends BaseActivity {

    private static final String TAG = "ContentActivity";

    @BindView(R.id.fragment_container)
    FrameLayout mContainer;

    BackTitleTemplate mBackTitleTemplate;
    @Override
    protected BaseTemplate createTemplate() {
        mBackTitleTemplate = new BackTitleTemplate(this,"示例");
        return mBackTitleTemplate;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_content;
    }

    @Override
    protected void doOnCreate(Bundle savedInstanceState) {
        Log.i(TAG,"doOnCreate");
        Intent intent = getIntent();
        String tag = intent.getStringExtra("fragment");
        String title = intent.getStringExtra("title");
        mBackTitleTemplate.setTitle(title);
        doFragmentRouter(tag);
    }

    private void doFragmentRouter(String tag){
        Log.i(TAG,"doFragmentRouter tag = " + tag);
        if(tag == null || tag.isEmpty()){
            Log.w(TAG,"not find fragment, please check tag!");
            return ;
        }
        Fragment curFragment = createFragmentByReflect(tag);
        if(curFragment != null){
            showFragment(curFragment);
        }
    }

    private Fragment createFragmentByReflect(String className){
        try {
            Class fragmentClass = Class.forName(className);
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            return fragment;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void showFragment(Fragment fragment){
        Log.i(TAG,"showFragment");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment,fragment.getClass().getSimpleName());
        transaction.commit();
    }



}
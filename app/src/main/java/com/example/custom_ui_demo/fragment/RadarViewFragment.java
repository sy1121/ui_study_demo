package com.example.custom_ui_demo.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custom_ui_demo.Const;
import com.example.custom_ui_demo.R;
import com.example.custom_ui_demo.base.fragment.BaseFragment;
import com.example.custom_ui_demo.service.ScanService;
import com.example.custom_ui_demo.view.RadarView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

import butterknife.BindView;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * yishe
 * 2019-07-07
 */
public class RadarViewFragment extends BaseFragment implements ServiceConnection {

    @BindView(R.id.anim_layout)
    RelativeLayout mAnimLayout;
    @BindView(R.id.radar_view)
    RadarView mRadarView;
    @BindView(R.id.number_text)
    TextView mRubbishSize;
    @BindView(R.id.unit_text)
    TextView mRubbishUnit;
    @BindView(R.id.path_text)
    TextView mCurFilePath;


    private static final int MSG_SCAN_FINISH = 0x01;
    private static final int MSG_SCAN_SIZE_CHANGE = 0x02;
    private static final int MSG_SCAN_PATH_CHANGE = 0x03;


    private ScanService.ScanCallback mScanCallback;
    private long curSize;

    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SCAN_SIZE_CHANGE:
                    changeRubbishSize((long)msg.obj);
                    break;
                case MSG_SCAN_PATH_CHANGE:
                    mCurFilePath.setText((String)msg.obj);
                    break;
                case MSG_SCAN_FINISH:
                    mRadarView.fadeOutAnimation();
                    break;

            }
        }
    };
    @Override
    protected void doOnCreateView(Bundle savedInstanceState) {
        mRadarView.startAnimation();
        Random random = new Random(System.currentTimeMillis());
        mHandler.sendEmptyMessageDelayed(0,5000 + random.nextInt(10) * 1000);
        mRadarView.setmAnimListener(new RadarView.AnimListener() {
            @Override
            public void onAnimEnd() {
                mRadarView.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"动画结束",Toast.LENGTH_SHORT).show();
            }
        });


        try {
            mScanCallback = new Callback();
            Intent service = new Intent(getContext(), ScanService.class);
            service.putExtra(Const.SCAN_ROOT_PATH, Environment.getExternalStorageDirectory().getCanonicalPath());
            getContext().bindService(service,this,BIND_AUTO_CREATE);
            getContext().startService(service);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected int getContentLayout() {
        return R.layout.fragment_radar_view;
    }

    private void changeRubbishSize(long size){
        long mb = size / (1024 * 1024);
        if(mb > 1024){
            mRubbishUnit.setText("GB");
            mRubbishSize.setText("" + new DecimalFormat(".00").format(size / (1024 * 1024 * 1024.0)));
        }else{
            mRubbishSize.setText("" + new DecimalFormat(".00").format(size / (1024 * 1024.0)));
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ScanService.ScanBinder binder = (ScanService.ScanBinder)(service);
        ScanService scanService = binder.getService();
        scanService.setCallback(mScanCallback);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public class Callback implements ScanService.ScanCallback{
        @Override
        public void sizeChange(long size) {
            curSize += size;
            Message msg = mHandler.obtainMessage(MSG_SCAN_SIZE_CHANGE);
            msg.obj = curSize;
            mHandler.sendMessage(msg);
        }

        @Override
        public void foundNewPath(String path) {
            Message msg = mHandler.obtainMessage(MSG_SCAN_PATH_CHANGE);
            msg.obj = path;
            mHandler.sendMessage(msg);
        }

        @Override
        public void scanFinish() {
            mHandler.sendEmptyMessage(MSG_SCAN_FINISH);
        }
    }
}

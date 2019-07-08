package com.example.custom_ui_demo.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * yishe
 * 2019-07-07
 */
public class ScanService extends IntentService {

    private static final String TAG = "ScanService";
    private ScanCallback mCallback;

    public ScanService(){
        super("ScanService");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ScanService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        Log.i(TAG,"onHandleIntent");
        String rootPath = intent.getStringExtra("root_path");
        scanFile(rootPath);
        if(mCallback != null){
            mCallback.scanFinish();
        }
    }

    private void scanFile(String mRootPath){
        try{
            if(TextUtils.isEmpty(mRootPath)) return ;
            File root = new File(mRootPath);
            if(root.isDirectory()){
                for(File file : root.listFiles()){
                    if(file.isDirectory()){
                        scanFile(file.getCanonicalPath());
                    }else{
                        if(file.length() > 1024 * 1024) continue; //大于1MB的不要了 这里模拟一下
                        if(mCallback != null){
                            mCallback.foundNewPath(file.getCanonicalPath());
                            mCallback.sizeChange(file.length());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ScanBinder();
    }

    public class ScanBinder extends Binder {
        public ScanService getService(){
            return ScanService.this;
        }
    }


    /**
     * 扫描过程回调
     */
    public interface ScanCallback{
        void sizeChange(long size);
        void foundNewPath(String path);
        void scanFinish();
    }

    public void setCallback(ScanCallback callBack){
        mCallback = callBack;
    }

    public void removeCallback(){
        mCallback = null;
    }

}

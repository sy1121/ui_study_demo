package com.example.custom_ui_demo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by xiaoguochang on 2016/2/25.
 * AbsListview帮助类
 */
public class LayoutUtil {
    public static View inflate(Context context, int resource, ViewGroup root, boolean attachToRoot){
        return LayoutInflater.from(context).inflate(resource, root, attachToRoot);
    }
    /**
     * 动态设置listview的高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        if (listView == null) {
            return;
        }

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + listView.getPaddingTop() + listView.getPaddingBottom();
        listView.setLayoutParams(params);
    }

    /**
     * 设置gridview高度
     * @param gridView
     * @param verticalSpacing
     */
    public static void setGridViewHeight(GridView gridView, int verticalSpacing) {
        if (gridView == null) {
            return;
        }

        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int col = gridView.getNumColumns();
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight() + verticalSpacing;
        }

        //上面多加了一个垂直间距，所以要减去
        totalHeight -= verticalSpacing;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 设置高度
        params.height = totalHeight + gridView.getPaddingTop() + gridView.getPaddingBottom();
        gridView.setLayoutParams(params);
    }

    /**
     * 设置View为正方形
     * @param v
     */
    public static void setLayoutSquare(final View v){
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(AppUtils.getSDKVersion() >= 16) {
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else {
                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int width = v.getWidth();
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.height = width;
                v.setLayoutParams(params);
            }
        });
    }

    /**
     * 设置view的高为宽的1/4
     * @param v
     */
    public static void setLayout(final View v){
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(AppUtils.getSDKVersion() >= 16) {
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else {
                    v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                int width = v.getWidth();
                ViewGroup.LayoutParams params = v.getLayoutParams();
                params.height = width / 4;
                v.setLayoutParams(params);
            }
        });
    }

    /**
     * 测量view的宽高后重新设置
     * @param view
     */
    public static void measureView(View view, View image){
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);

        image.measure(width, height);
        width = image.getMeasuredWidth();
        height = image.getMeasuredHeight();

        width = view.getMeasuredWidth();
        height = view.getMeasuredHeight();

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = width;
        view.setLayoutParams(params);
    }
}

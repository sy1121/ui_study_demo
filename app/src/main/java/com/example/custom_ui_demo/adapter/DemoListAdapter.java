package com.example.custom_ui_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.custom_ui_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * yishe
 * 2019/7/4
 */
public class DemoListAdapter extends BaseAdapter {

    private Context sContext;
    private String[] sDatas;

    public DemoListAdapter(Context context, String[] datas){
        sContext = context;
        sDatas = datas;
    }

    @Override
    public int getCount() {
        return sDatas.length;
    }

    @Override
    public String getItem(int position) {
        return sDatas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(sContext).inflate(R.layout.layout_view_demo_list_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.mBtn = convertView.findViewById(R.id.view_demo_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mBtn.setText(getItem(position)+"");
        return convertView;
    }


    static class ViewHolder{
        @BindView(R.id.view_demo_item)
        Button mBtn;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}

package com.coder.yun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coder.yun.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 三级查房中查看笔记的list适配器
 */

public class CheckRoomAdapter extends BaseAdapter {

    private List<Map<String, Object>> list1 = new ArrayList<>();
    //List<String> list = new ArrayList<>();
    private Context context;


    public CheckRoomAdapter(List<Map<String, Object>> list1, Context context) {
        this.list1 = list1;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list1.size();
    }

    @Override
    public Object getItem(int position) {
        return list1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder hold;
        if (convertView == null) {
            hold = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_note_list, null);
            hold.textView1 = (TextView) convertView.findViewById(R.id.textView21);
            hold.textView2 = (TextView) convertView.findViewById(R.id.textView22);

            convertView.setTag(hold);
        } else {
            hold = (ViewHolder) convertView.getTag();
        }
        hold.textView1.setText(list1.get(position).get("roomNo").toString());
        hold.textView2.setText(list1.get(position).get("patientName").toString());


        return convertView;
    }

    class ViewHolder {
        TextView textView1, textView2;
    }
}


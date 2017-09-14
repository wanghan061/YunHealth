package com.coder.yun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.yun.R;

/**
 *
 */

public class MyGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private String[] names;
    private int[] images;

    public MyGridViewAdapter(Context context, String[] names, int[] images) {
        this.mContext = context;
        this.names = names;
        this.images = images;

    }


    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.gridview_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        imageView.setImageResource(images[position]);
        textView.setText(names[position]);
        return view;
    }
}

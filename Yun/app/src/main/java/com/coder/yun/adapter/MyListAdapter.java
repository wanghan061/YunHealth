package com.coder.yun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder.yun.R;


public class MyListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] textData = {"预约挂号", "报告查询", "个人中心", "设置"};
    private int[] imageData = {R.drawable.yuyueguahao, R.drawable.baogaochaxun, R.drawable.gerenzhongxin, R.drawable.shezhi};

    public MyListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return textData.length;
    }

    @Override
    public Object getItem(int position) {
        return textData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder = null;
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.left_list_item, null);
            mViewHolder = new MyViewHolder();
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_item);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_item);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        mViewHolder.mImageView.setImageResource(imageData[position]);
        mViewHolder.mTextView.setText(textData[position]);

        return convertView;
    }


    static class MyViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }


}

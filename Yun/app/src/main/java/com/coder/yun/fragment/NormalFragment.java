package com.coder.yun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.coder.yun.R;
import com.coder.yun.activity.normal.DrugRemindActivity;
import com.coder.yun.activity.normal.ExpertAppointActivity;
import com.coder.yun.activity.normal.NormalCheckAdvice;
import com.coder.yun.activity.normal.PerFileContext;
import com.coder.yun.activity.normal.QuestionConsultActivity;
import com.coder.yun.adapter.MyGridViewAdapter;
import com.coder.yun.adapter.MyPagerAdapter;
import com.coder.yun.base.BaseFragment;

/**
 * 患者
 */

public class NormalFragment extends BaseFragment {
    private MyGridViewAdapter myGridViewAdapter;
    private String[] names;
    private int[] images;

    public NormalFragment() {
        initData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_images);
        GridView gridView = (GridView) view.findViewById(R.id.gv_work);
        myGridViewAdapter = new MyGridViewAdapter(getActivity(), names, images);
        gridView.setAdapter(myGridViewAdapter);
        viewPager.setAdapter(new MyPagerAdapter(getActivity()));

        //gridView中每个功能的点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://专家预约
                        intent = new Intent(getActivity(),ExpertAppointActivity.class);
                        startActivity(intent);
                        break;
                    case 1://问题咨询
                        intent = new Intent(getActivity(), QuestionConsultActivity.class);
                        startActivity(intent);
                        break;
                    case 2://个人家健康管理
                        intent = new Intent(getActivity(), PerFileContext.class);
                        startActivity(intent);
                        break;
                    case 3://用药提醒
                        intent = new Intent(getActivity(), DrugRemindActivity.class);
                        startActivity(intent);
                        break;
                    case 4://我的医嘱
                        intent = new Intent(getActivity(), NormalCheckAdvice.class);
                        startActivity(intent);
                        break;


                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        names = new String[]{"专家预约", "问题咨询", "个人健康管理", "用药提醒", "我的医嘱"};
        images = new int[]{R.drawable.zhuanjiayuyue, R.drawable.wentizixun, R.drawable.jiangkangdangan, R.drawable.yongyaotixing, R.drawable.wodexiaoxi};

    }
}

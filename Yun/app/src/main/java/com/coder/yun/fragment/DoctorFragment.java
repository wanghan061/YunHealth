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
import com.coder.yun.activity.doctor.CheckMyQuestion;
import com.coder.yun.activity.doctor.CheckRoom;
import com.coder.yun.activity.doctor.DoctorAdviceList;
import com.coder.yun.activity.doctor.FileManage;
import com.coder.yun.activity.doctor.MobileNote;
import com.coder.yun.activity.doctor.ProblemFeedback;
import com.coder.yun.adapter.MyGridViewAdapter;
import com.coder.yun.adapter.MyPagerAdapter;
import com.coder.yun.base.BaseFragment;

/**
 * 医生
 */

public class DoctorFragment extends BaseFragment {
    private MyGridViewAdapter myGridViewAdapter;
    private String[] names;
    private int[] images;

    public DoctorFragment() {
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

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), ProblemFeedback.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), CheckRoom.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), DoctorAdviceList.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), MobileNote.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), FileManage.class);
                        startActivity(intent);
                        break;
                    case 5:
                        //HealthList.class
                        intent = new Intent(getActivity(), FileManage.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), CheckMyQuestion.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {
        names = new String[]{"问题反馈", "三级查房", "开具医嘱", "移动笔记", "健康档案管理", "生命体征", "我的问题"};
        images = new int[]{R.drawable.wentifankui, R.drawable.sanjichafang, R.drawable.kaijuyizhu, R.drawable.yidongjibi, R.drawable.jiangkangdangan, R.drawable.shengmingtizheng, R.drawable.wodexiaoxi};

    }
}

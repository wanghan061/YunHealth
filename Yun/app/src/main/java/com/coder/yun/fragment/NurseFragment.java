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
import com.coder.yun.activity.doctor.FileManage;
import com.coder.yun.activity.nurse.AddRemind;
import com.coder.yun.activity.nurse.AdviceExecute;
import com.coder.yun.activity.nurse.BedManage;
import com.coder.yun.activity.nurse.WardInspection;
import com.coder.yun.adapter.MyGridViewAdapter;
import com.coder.yun.adapter.MyPagerAdapter;
import com.coder.yun.base.BaseFragment;

/**
 * 护士
 */

public class NurseFragment extends BaseFragment {
    private MyGridViewAdapter myGridViewAdapter;
    private String[] names;
    private int[] images;

    public NurseFragment() {
        initData();
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                        intent = new Intent(getActivity(), AdviceExecute.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), WardInspection.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), BedManage.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), FileManage.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), AddRemind.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {
        names = new String[]{"医嘱执行", "病房巡视", "床位安排", "生命体征", "用药提醒"};
        images = new int[]{R.drawable.yizhuzhixing, R.drawable.bingfangxunshi, R.drawable.chuangweianpai, R.drawable.shengmingtizheng, R.drawable.jianyanbaogao};

    }
}

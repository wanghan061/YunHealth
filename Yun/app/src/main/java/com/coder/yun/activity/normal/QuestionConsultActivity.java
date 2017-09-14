package com.coder.yun.activity.normal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.adapter.DoctorListAdapter;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.Constants;
import com.coder.yun.util.IStringRequest;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 问题咨询
 */

public class QuestionConsultActivity extends BaseActivity {

    private ListView listView;
    private List<Map<String, Object>> list1 = new ArrayList<>();
    private DoctorListAdapter adapter;
    private String name, department;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wentizixun);
        initTitle();
        getDoctorInfo();
        initView();

    }

    private void getDoctorInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        IStringRequest request = new IStringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                "servlet/QueryDoctorServlet",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("TAG", s.toString());

                        parsedoc(s);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.toString());
                    }
                });
        queue.add(request);
    }

    private void parsedoc(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            list1 = doctors;
            adapter = new DoctorListAdapter(list1, getBaseContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    name = list1.get(position).get("name").toString();
                    department = list1.get(position).get("department").toString();
                    Intent intent = new Intent(QuestionConsultActivity.this, QuestionContext.class);
                    intent.putExtra("name", name);
                    intent.putExtra("department", department);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.lv_yishengliebiao);
    }


    @Override
    public void initAction() {


    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("医生列表");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

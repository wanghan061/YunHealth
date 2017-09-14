package com.coder.yun.activity.nurse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.activity.doctor.DoctorCheckAdvice;
import com.coder.yun.adapter.PatientListAdapter;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.Constants;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 医嘱执行的病人列表
 */

public class AdviceExecute extends BaseActivity {


    private ListView listView;
    private String department;
    private List<Map<String, Object>> list1 = new ArrayList<>();
    private PatientListAdapter adapter;
    private String name, bedNo, userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wentizixun);
        initTitle();
        initView();
        getDoctorInfo();
    }

    private void getDoctorInfo() {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "servlet/DoctorAdviceNurseQueryServlet?userName=" + userName,
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
                            Toast.makeText(AdviceExecute.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        queue.add(request);

    }

    private void parsedoc(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            list1 = doctors;
            adapter = new PatientListAdapter(list1, getBaseContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(AdviceExecute.this, DoctorCheckAdvice.class);
                    name = list1.get(position).get("patientName").toString();
                    bedNo = list1.get(position).get("roomNo").toString();
                    intent.putExtra("patientName", name);
                    intent.putExtra("roomNo", bedNo);
                    startActivity(intent);
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
//        SharedPreferences pref = getSharedPreferences("userdata",MODE_PRIVATE);
//        department = pref.getString("department","");
        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");

        listView = (ListView) findViewById(R.id.lv_yishengliebiao);
    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("患者列表");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

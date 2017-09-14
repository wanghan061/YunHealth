package com.coder.yun.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.Constants;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import java.util.List;
import java.util.Map;

/**
 * 个人中心的用户信息
 */

public class PersonInfo extends BaseActivity {

    private TextView tv_name;
    private Spinner sp_sex;
    private TextView tv_sex;
    private TextView tv_tel;
    private TextView tv_idcard;        //身份证号
    private TextView tv_department;       //科室
    private Button btn_editInfo;
    private String userName, name, sex, tel, idcard, department, temp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        requestInfo();

        initTitle();

        btn_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonInfo.this, PersonInfoEdit.class);
                intent.putExtra("name", name);
                intent.putExtra("sex", sex);
                intent.putExtra("tel", tel);
                intent.putExtra("idcard", idcard);
                intent.putExtra("department", department);
                startActivity(intent);
            }
        });
    }

    private void requestInfo() {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                "servlet/SelfMessageQueryServlet?userName=" + userName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        parsedoc(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(PersonInfo.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                    }
                });
        mQueue.add(request);
    }

    private void parsedoc(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        Map<String, Object> doctor;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            doctor = doctors.get(0);
            name = doctor.get("name").toString();
            sex = doctor.get("sex").toString();
            tel = doctor.get("tel").toString();
            idcard = doctor.get("idcard").toString();
            department = doctor.get("department").toString();

            tv_name.setText(name);
            tv_sex.setText(sex);
            tv_tel.setText(tel);
            tv_idcard.setText(idcard);
            tv_department.setText(department);

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

        tv_name = (TextView) findViewById(R.id.tv_name);
        sp_sex = (Spinner) findViewById(R.id.spinner1);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_idcard = (TextView) findViewById(R.id.tv_idcard);
        tv_department = (TextView) findViewById(R.id.tv_department);
        btn_editInfo = (Button) findViewById(R.id.btn_editInfo);

        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");
    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("个人信息");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

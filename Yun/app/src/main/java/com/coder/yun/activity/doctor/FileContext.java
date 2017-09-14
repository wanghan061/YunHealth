package com.coder.yun.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 健康档案内容
 */

public class  FileContext extends BaseActivity {

    private TextView name;
    private TextView bedNo;
    private TextView temp;
    private TextView pulse;
    String userName, patientName, bed;
    private List<Map<String, Object>> list1 = new ArrayList<>();
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_context);
        initTitle();
        initView();
        getInfo();
    }

    private void getInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                    "servlet/TemperatureQueryServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8"),
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
                            Toast.makeText(FileContext.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);
    }

    private void parsedoc(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        Map<String, Object> doctor;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            doctor = doctors.get(0);
            temp.setText(doctor.get("temp").toString());
            pulse.setText(doctor.get("pulse").toString());
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
        name = (TextView) findViewById(R.id.tv_name);
        bedNo = (TextView) findViewById(R.id.tv_bed);

        temp = (TextView) findViewById(R.id.tv_temp);
        pulse = (TextView) findViewById(R.id.tv_pulse);

        intent = getIntent();
        patientName = intent.getStringExtra("patientName");

    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("健康记录");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

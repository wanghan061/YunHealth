package com.coder.yun.activity.nurse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.Constants;
import com.coder.yun.util.IStringRequest;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看某个病房记录细节
 */

public class WardInspectRecord extends BaseActivity {

    private TextView name;
    private TextView bedNo;
    private TextView healthStatus;
    private TextView others;
    private Button edit;
    private Button cancel;
    private String patientName, bed, state, other;
    private Map<String, Object> data = new HashMap<>();
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ward_record);
        initTitle();
        initView();
        getHealthInfo();
    }

    private void getHealthInfo() {
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        IStringRequest request = null;
        try {
            request = new IStringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "servlet/CheckHealthShowServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8"),
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
                            Toast.makeText(WardInspectRecord.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
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
            state = doctor.get("state").toString();
            other = doctor.get("other").toString();
            healthStatus.setText(state);
            others.setText(other);

//            data = object.get("data");
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
        healthStatus = (TextView) findViewById(R.id.tv_status);
        others = (TextView) findViewById(R.id.tv_others);
        edit = (Button) findViewById(R.id.bt_edit);
        cancel = (Button) findViewById(R.id.bt_cancel);

        intent = getIntent();
        patientName = intent.getStringExtra("patientName");
        bed = intent.getStringExtra("roomNo");
        name.setText(patientName);
        bedNo.setText(bed);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WardInspectRecord.this, EditBedInfo.class);
                intent.putExtra("patientName", patientName);
                intent.putExtra("bed", bed);
                intent.putExtra("state", state);
                intent.putExtra("other", other);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("详细信息");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.coder.yun.activity.nurse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.coder.yun.util.IStringRequest;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看床位安排的细节
 */

public class BedDetailChange extends BaseActivity {

    private TextView name;
    private EditText bedNo;
    private Button wancheng;
    private Button cancel;
    private String patientName, bed, roomNo, state, other;
    private Map<String, Object> data = new HashMap<>();
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bed_detail_change);
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
                            //parsedoc(s);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(BedDetailChange.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
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
        bedNo = (EditText) findViewById(R.id.et_bed);
        wancheng = (Button) findViewById(R.id.bt_wancheng);
        cancel = (Button) findViewById(R.id.bt_cancel);

        intent = getIntent();
        patientName = intent.getStringExtra("patientName");
        bed = intent.getStringExtra("bed");
        name.setText(patientName);
        bedNo.setText(bed);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfo();
            }
        });

    }

    private void submitInfo() {
        roomNo = bedNo.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "servlet/UpdateRoomNoServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8") + "&roomNo=" + roomNo,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("TAG", s.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");
                                if ("10002".equals(code)) {
                                    Toast.makeText(BedDetailChange.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(BedDetailChange.this, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(BedDetailChange.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);
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

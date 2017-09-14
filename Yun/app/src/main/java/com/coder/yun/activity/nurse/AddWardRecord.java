package com.coder.yun.activity.nurse;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.coder.yun.util.TitleBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 病房巡视的添加纪录按钮
 */

public class AddWardRecord extends BaseActivity {

    private EditText name;
    private EditText bedNo;
    private EditText healthStatus;
    private EditText others;
    private Button submit;
    private Button cancel;
    private String patientName, roomNo, state, other, userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ward_record);
        initTitle();
        initView();
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        name = (EditText) findViewById(R.id.et_name);
        bedNo = (EditText) findViewById(R.id.et_bed);
        healthStatus = (EditText) findViewById(R.id.et_status);
        others = (EditText) findViewById(R.id.et_others);
        submit = (Button) findViewById(R.id.bt_submit);
        cancel = (Button) findViewById(R.id.bt_cancel);

        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");
        Log.d("TAG",userName);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientName = name.getText().toString();
                roomNo = bedNo.getText().toString();
                state = healthStatus.getText().toString();
                other = others.getText().toString();

                if (!TextUtils.isEmpty(patientName) && !TextUtils.isEmpty(roomNo) && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(other)) {
                    submitInfo();
                } else {
                    Toast.makeText(AddWardRecord.this, "请输入完整的信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "servlet/CheckHealthAddServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8") + "&roomNo=" + roomNo + "&state=" + URLEncoder.encode(state, "UTF-8") + "&other=" + URLEncoder.encode(other, "UTF-8") + "&userName=" + userName,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("TAG", s.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");
                                if ("10002".equals(code)) {
                                    Toast.makeText(AddWardRecord.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddWardRecord.this, "添加失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(AddWardRecord.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
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
        titleBarUtils.setTitle("病房巡视");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

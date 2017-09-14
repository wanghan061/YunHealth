package com.coder.yun.activity.nurse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.coder.yun.util.TitleBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 编辑查房信息
 */

public class EditBedInfo extends BaseActivity {

    private TextView name;
    private TextView bedNo;
    private EditText healthStatus;
    private EditText others;
    private Button submit;
    private Button cancel;
    private String patientName, roomNo, state, other;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ward_record);
        initTitle();
        initView();
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        name = (TextView) findViewById(R.id.tv_name);
        bedNo = (TextView) findViewById(R.id.tv_bed);
        healthStatus = (EditText) findViewById(R.id.et_status);
        others = (EditText) findViewById(R.id.et_others);
        submit = (Button) findViewById(R.id.bt_submit);
        cancel = (Button) findViewById(R.id.bt_cancel);

        Intent intent = getIntent();
        patientName = intent.getStringExtra("patientName");
        roomNo = intent.getStringExtra("bed");
        state = intent.getStringExtra("state");
        other = intent.getStringExtra("other");
        name.setText(patientName);
        bedNo.setText(roomNo);
        healthStatus.setText(state);
        others.setText(other);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //patientName = name.getText().toString();
                roomNo = bedNo.getText().toString();
                state = healthStatus.getText().toString();
                other = others.getText().toString();

                if (!TextUtils.isEmpty(patientName) && !TextUtils.isEmpty(roomNo) && !TextUtils.isEmpty(state) && !TextUtils.isEmpty(other)) {
                    submitInfo();
                } else {
                    Toast.makeText(EditBedInfo.this, "请输入完整的信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void submitInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "servlet/UpdateRoomServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8") + "&roomNo=" + roomNo + "&state=" + URLEncoder.encode(state, "UTF-8") + "&other=" + URLEncoder.encode(other, "UTF-8"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("TAG", s.toString());

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");
                                if ("10002".equals(code)) {
                                    Toast.makeText(EditBedInfo.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(EditBedInfo.this, "修改失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(EditBedInfo.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
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
        titleBarUtils.setTitle("编辑信息");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

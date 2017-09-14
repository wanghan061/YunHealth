package com.coder.yun.activity.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 * 问题列表点击的详情
 */

public class ProblemDetail extends BaseActivity {

    private TextView name;
    private TextView problem;
    private EditText feedBack;
    private Button queding;
    private Button cancel;
    private String patientName, question, huiFu, userName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.problem_ditail);
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
        problem = (TextView) findViewById(R.id.tv_question);
        feedBack = (EditText) findViewById(R.id.et_context);
        queding = (Button) findViewById(R.id.bt_queding);
        cancel = (Button) findViewById(R.id.bt_cancel);

        Intent intent = getIntent();
        patientName = intent.getStringExtra("patientName");
        question = intent.getStringExtra("question");
        name.setText(patientName);
        problem.setText(question);

        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huiFu = feedBack.getText().toString();
                if (!TextUtils.isEmpty(huiFu)) {
                    sendFeedback();
                } else {
                    Toast.makeText(ProblemDetail.this, "请输入回复内容", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendFeedback() {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                    "servlet/DoctorAnswerAddServlet?question=" + URLEncoder.encode(question, "UTF-8") + "&answer=" + URLEncoder.encode(huiFu, "UTF-8") + "&patientName=",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");
                                if ("10002".equals(code)) {
                                    Toast.makeText(ProblemDetail.this, "回复成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ProblemDetail.this, "回复失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ProblemDetail.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mQueue.add(request);
    }

    @Override
    public void initAction() {

    }


    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("问题详情");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

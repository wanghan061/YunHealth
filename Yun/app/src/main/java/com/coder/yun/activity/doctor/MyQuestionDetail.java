package com.coder.yun.activity.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.List;
import java.util.Map;

/**
 * 医生查看自己已经回答的问题详情
 */

public class MyQuestionDetail extends BaseActivity {

    private TextView name;
    private TextView problem;
    private TextView feedBack;
    private Button queding;
    private Button cancel;
    private String patientName, question, huiFu, userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_question_detail);
        initView();
        initTitle();
        getFeedback();
    }

    private void getFeedback() {
        RequestQueue queue = Volley.newRequestQueue(this);
        IStringRequest request = null;
        try {
            request = new IStringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                    "servlet/PatientQuestionSelfServlet?userName=" + userName + "&question=" + URLEncoder.encode(question, "UTF-8"),
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
            //temp1.setText(doctor.get("temp1").toString());
            feedBack.setText(doctor.get("answer").toString());
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
        problem = (TextView) findViewById(R.id.tv_question);
        feedBack = (TextView) findViewById(R.id.tv_context);
        queding = (Button) findViewById(R.id.bt_queding);
        cancel = (Button) findViewById(R.id.bt_cancel);

        Intent intent = getIntent();
        patientName = intent.getStringExtra("patientName");
        question = intent.getStringExtra("question");
        name.setText(patientName);
        problem.setText(question);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

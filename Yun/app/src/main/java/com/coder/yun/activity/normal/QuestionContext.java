package com.coder.yun.activity.normal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.coder.yun.util.Constants;
import com.coder.yun.util.TitleBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 问题详情页
 */

public class QuestionContext extends Activity {

    private EditText et_question;
    private Button cancel;
    private Button save;
    private String context, doctorName, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_context);
        initTitle();
        initView();
    }

    private void submitQuestion() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "servlet/PatientQuestionAddServlet?userName=" + URLEncoder.encode(userName, "UTF-8") + "&doctorName=" + URLEncoder.encode(doctorName, "UTF-8") + "&question=" + URLEncoder.encode(context, "UTF-8"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("TAG2", s.toString());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");

                                if ("10002".equals(code)) {
                                    Toast.makeText(QuestionContext.this, "提交问题成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(QuestionContext.this, "提交问题失败,请重试", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(QuestionContext.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);
    }

    private void initView() {
        et_question = (EditText) findViewById(R.id.et_context);
        cancel = (Button) findViewById(R.id.btn_cancel);
        save = (Button) findViewById(R.id.btn_submit);

        Intent intent = getIntent();
        doctorName = intent.getStringExtra("name");

        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = et_question.getText().toString();

                if (!TextUtils.isEmpty(context)) {
                    submitQuestion();
                    Intent intent = new Intent(QuestionContext.this, QuestionConsultActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(QuestionContext.this, "请输入问题内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("问题咨询");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

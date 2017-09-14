package com.coder.yun.activity.normal;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.Constants;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * （回答详情）我的问题列表中的点击
 */

public class CheckAnswer extends BaseActivity {

    private TextView question;
    private TextView answer;
    private Button queding;
    private Button cancel;
    private String wenti, huida, userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_detail);
        initTitle();
        initView();
        getAnswer();
    }

    private void getAnswer() {
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "/servlet/PatientQuestionCheckServlet?question=" + URLEncoder.encode(wenti, "UTF-8") + "&userName=" + userName,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("TAG", s.toString());
                            getInfo(s);
//                                JSONObject jsonObject = new JSONObject(s);
//                                huida = jsonObject.getString("answer");
//                                answer.setText(huida);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(CheckAnswer.this, "网络连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);
    }

    private void getInfo(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        Map<String, Object> doctor = null;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            doctor = doctors.get(0);
            answer.setText(doctor.get("answer").toString());
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
        question = (TextView) findViewById(R.id.tv_question);
        answer = (TextView) findViewById(R.id.tv_context);
        queding = (Button) findViewById(R.id.bt_queding);
        cancel = (Button) findViewById(R.id.bt_cancel);

        Intent intent = getIntent();
        wenti = intent.getStringExtra("question");
        question.setText(wenti);

        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
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
        titleBarUtils.setTitle("回答详情");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

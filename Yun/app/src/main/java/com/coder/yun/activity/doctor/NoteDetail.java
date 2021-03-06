package com.coder.yun.activity.doctor;

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
 * 三级查房笔记详情页
 */

public class NoteDetail extends BaseActivity {

    private TextView context;
    private TextView roomNo;
    private Button submit;
    private Button cancel;
    String bedNo,date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_detail);
        initTitle();
        initView();
        getInfo();

        submit.setOnClickListener(new View.OnClickListener() {
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

    private void getInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                "servlet/ChcekRoomRecordServlet?roomNo=" + bedNo,
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
                        Toast.makeText(NoteDetail.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }

    private void parsedoc(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        Map<String, Object> doctor = null;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            doctor = doctors.get(0);
            context.setText(doctor.get("roomRecord").toString());
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
        context = (TextView) findViewById(R.id.tv_context);
        roomNo = (TextView) findViewById(R.id.tv_bedNo);
        submit = (Button) findViewById(R.id.bt_queding);
        cancel = (Button) findViewById(R.id.bt_cancel);

        Intent intent = getIntent();
        bedNo = intent.getStringExtra("bedNo");
        date = intent.getStringExtra("date");

        roomNo.setText(bedNo);

    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("笔记详情");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

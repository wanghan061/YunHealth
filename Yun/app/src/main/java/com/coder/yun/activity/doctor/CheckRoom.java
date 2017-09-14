package com.coder.yun.activity.doctor;

/*
* 三级查房
* */


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Calendar;

public class CheckRoom extends AppCompatActivity {

    private Button btn_loadnote;
    private Button btn_checknote;
    private EditText roomNumber;
    private EditText record;
    String roomNo, roomRecord, name, date, patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_room);
        btn_loadnote = (Button) findViewById(R.id.btn_loadnote);
        btn_checknote = (Button) findViewById(R.id.btn_checknote);
        roomNumber = (EditText) findViewById(R.id.et_roomNumber);
        record = (EditText) findViewById(R.id.et_record);
        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        name = pref.getString("name", "");

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        date = year + "-" + month + "-" + day;
        Log.d("TIME", year + "年" + month + "月" + day + "日");

        btn_loadnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadnote();
            }
        });

        btn_checknote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckRoom.this, CheckNote.class);
                startActivity(intent);
            }
        });
        initTitle();
    }

    private void loadnote() {
        roomNo = roomNumber.getText().toString();
        roomRecord = record.getText().toString();
        if (!TextUtils.isEmpty(roomNo) && !TextUtils.isEmpty(roomRecord)) {
            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = null;
            try {
                request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                        "servlet/CheckRoomAddServlet?roomNo=" + roomNo + "&roomRecord=" + URLEncoder.encode(roomRecord, "UTF-8") + "&userName=" + name + "&date=" + URLEncoder.encode(date, "UTF-8") ,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String code = jsonObject.getString("code");
                                    if ("10002".equals(code)) {
                                        Toast.makeText(CheckRoom.this, "上传笔记成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CheckRoom.this, CheckNote.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(CheckRoom.this, "上传笔记失败", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(CheckRoom.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mQueue.add(request);
        } else {
            Toast.makeText(CheckRoom.this, "请输入病房号和查房记录", Toast.LENGTH_SHORT).show();
        }
    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("三级查房");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.coder.yun.activity.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 * 移动笔记
 */

public class MobileNote extends BaseActivity {

    private Button loadnote;
    private Button checkNote;
    private EditText noteContext;
    String note, userName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_note);
        loadnote = (Button) findViewById(R.id.btn_loadnote);
        checkNote = (Button) findViewById(R.id.btn_checkNote);
        noteContext = (EditText) findViewById(R.id.et_note);

        initTitle();

        loadnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = noteContext.getText().toString();
                SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
                userName = pref.getString("name", "");

                if (!TextUtils.isEmpty(note)) {
                    sendNote();
                } else {
                    Toast.makeText(MobileNote.this, "请输入笔记内容", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MobileNote.this, MobileNoteList.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void sendNote() {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                    "servlet/NoteAddServlet?notes=" + URLEncoder.encode(note, "UTF-8") + "&userName=" + userName,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");
                                if ("10002".equals(code)) {
                                    Toast.makeText(MobileNote.this, "上传笔记成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MobileNote.this, MobileNoteList.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MobileNote.this, "上传笔记失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MobileNote.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mQueue.add(request);
    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("移动笔记");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initAction() {

    }
}

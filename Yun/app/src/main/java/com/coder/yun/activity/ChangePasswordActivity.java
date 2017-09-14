package com.coder.yun.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.coder.yun.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * 修改密码页
 */

public class ChangePasswordActivity extends AppCompatActivity {

    private Button btnSubmit;
    private EditText etAccount;
    private EditText etPwd;
    private EditText etPwd1;

    private String s1, s2, s3;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        initTitle();
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        etAccount = (EditText) findViewById(R.id.et_account);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etPwd1 = (EditText) findViewById(R.id.et_pwd1);
        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s1 = etAccount.getText().toString();
                s2 = etPwd.getText().toString();
                s3 = etPwd1.getText().toString();

                if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2) || TextUtils.isEmpty(s3)) {
                    ToastUtil.ToastShow(getBaseContext(), "输入内容不能为空", true);

                } else if (s2.length() < 6 || s2.length() > 18) {
                    ToastUtil.ToastShow(getBaseContext(), "请输入正确的密码位数", true);
                } else if (!s2.equals(s3)) {
                    ToastUtil.ToastShow(getBaseContext(), "再次输入的密码不一致", true);
                } else {

                    change();
                }
            }


        });
    }

    //用户修改密码成功后系统上传数据到后台
    private void change() {
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                "servlet/UpdatePasswordServlet?s1=" + s1 + "&password=" + s2 + "&repassword=" + s3 + "&userName=" + userName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("TAG",s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");

                            if ("10002".equals(code)) {
                                Toast.makeText(ChangePasswordActivity.this, "修改密码成功，请重新登陆", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "修改密码失败,请重试", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ChangePasswordActivity.this, "网络连接出现问题", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);

    }


    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("修改密码");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });

    }

}

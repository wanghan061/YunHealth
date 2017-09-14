package com.coder.yun.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_reg;
    private EditText et_user;
    private EditText et_password;
    private RadioGroup rg_type;
    private String type = "0";
    String userName, password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_reg = (Button) findViewById(R.id.btn_reg);
        et_user = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);
        rg_type = (RadioGroup) findViewById(R.id.rg_type);


        btn_reg.setOnClickListener(new View.OnClickListener() {     //注册按钮监听事件
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {       //登录按钮监听事件
            @Override
            public void onClick(View v) {

                RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

                userName = et_user.getText().toString();
                password = et_password.getText().toString();     //拿到user和password的内容

                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                    int checkedRadioButtonId = rg_type.getCheckedRadioButtonId();
                    switch (checkedRadioButtonId) {
                        case R.id.rb_normal:
                            type = "0";
                            break;
                        case R.id.rb_nurse:
                            type = "1";
                            break;
                        case R.id.rb_doctor:
                            type = "2";
                            break;

                        default:

                            break;
                    }

                    StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                            "servlet/LoginAndroidServlet?userName=" + userName + "&password=" + password + "&type=" + type,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.d("TAG", s.toString());

                                    sendToShared();

                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String code = jsonObject.getString("code");
                                        //String fail456ure = jsonObject.getString("failure");
                                        if ("10002".equals(code)) {
                                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("type", type);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(LoginActivity.this, "服务器出现问题", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", volleyError.getMessage(), volleyError);
                                }
                            }
                    );

                    mQueue.add(request);

                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToShared() {
        SharedPreferences.Editor editor = getSharedPreferences("nameData", MODE_PRIVATE).edit();
        editor.putString("name", userName);
        editor.commit();
    }
}

package com.coder.yun.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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


public class RegActivity extends AppCompatActivity {

    private EditText et_user;
    private EditText et_password;
    private EditText et_repeat_password;
    private Button btn_reg;
    private RadioGroup rg_reg_type;
    private RadioButton rb_normal, rb_doctor, rb_nurse;
    private String userName, password, repassword;
    private String type;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        et_user = (EditText) findViewById(R.id.et_user);
        et_password = (EditText) findViewById(R.id.et_password);
        et_repeat_password = (EditText) findViewById(R.id.et_repeat_password);
        btn_reg = (Button) findViewById(R.id.btn_reg);
        rg_reg_type = (RadioGroup) findViewById(R.id.rg_reg_type);
        rb_normal = (RadioButton) findViewById(R.id.rb_normal);
        rb_doctor = (RadioButton) findViewById(R.id.rb_doctor);
        rb_nurse = (RadioButton) findViewById(R.id.rb_nurse);

        btn_reg.setOnClickListener(new View.OnClickListener() {     //注册按钮点击事件
            @Override
            public void onClick(View v) {
                userName = et_user.getText().toString();
                password = et_password.getText().toString();
                repassword = et_repeat_password.getText().toString();


                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(repassword)) {
                    if (password.equals(repassword)) {
                        getUserType();

                        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                                "servlet/RegisterServlet?userName=" + userName + "&password=" + password + "&repassword=" + repassword + "&type=" + type,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        Log.d("TAG", s.toString());
                                        //int type = rg_reg_type.getCheckedRadioButtonId();
                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            String code = jsonObject.getString("code");
                                            if ("10002".equals(code)) {
                                                Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegActivity.this, AddPersonActivity.class);
                                                intent.putExtra("extra_type", type);
                                                intent.putExtra("username",userName);
                                                startActivity(intent);
                                                finish();
                                            } else if ("10001".equals(code)) {
                                                Toast.makeText(RegActivity.this, "该用户名已被注册", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Log.d("TAG", volleyError.toString());
                                        Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        mQueue.add(request);

                    } else {
                        Toast.makeText(RegActivity.this, "两次输入的密码不一致",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RegActivity.this, "用户名或密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getUserType() {
        int check_type = rg_reg_type.getCheckedRadioButtonId();
        switch (check_type) {
            case R.id.rb_reg_normal:
                type = "0";
                break;
            case R.id.rb_reg_nurse:
                type = "1";
                break;
            case R.id.rb_reg_doctor:
                type = "2";
                break;

            default:
                break;
        }
    }
}


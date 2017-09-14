package com.coder.yun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

/**
 * 编辑个人信息类
 */

public class PersonInfoEdit extends BaseActivity {

    private EditText et_name;
    private Spinner sp_sex;
    private EditText et_sex;
    private EditText et_tel;
    private EditText et_idcard;        //身份证号
    private EditText et_department;       //科室
    private Button btn_save;
    String name, sex, tel, idcard, department;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        initTitle();
        initView();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    private void saveInfo() {
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                "/servlet/NurseRegisterAddServlet?name=" + name + "&sex=" + sex + "&tel=" + tel + "&idcard=" + idcard + "&department=" + department,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("TAG", s);

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");

                            if ("10002".equals(code)) {
                                Toast.makeText(PersonInfoEdit.this, "保存成功,请重新登陆", Toast.LENGTH_SHORT).show();
                                intent = new Intent(PersonInfoEdit.this, LoginActivity.class);
                                startActivity(intent);
                                finishAffinity();//退出栈内所有activity
                            }else{
                                Toast.makeText(PersonInfoEdit.this, "保存信息失败，请重试", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("err", volleyError.toString());
                        Toast.makeText(PersonInfoEdit.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();

        et_name = (EditText) findViewById(R.id.et_name);
        et_sex = (EditText) findViewById(R.id.editText);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_idcard = (EditText) findViewById(R.id.et_idcard);
        et_department = (EditText) findViewById(R.id.et_department);
        btn_save = (Button) findViewById(R.id.btn_submit);

        et_name.setText(intent.getStringExtra("name"));
        et_sex.setText(intent.getStringExtra("sex"));
        et_tel.setText(intent.getStringExtra("tel"));
        et_idcard.setText(intent.getStringExtra("idcard"));
        et_department.setText(intent.getStringExtra("department"));
    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("编辑个人信息");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

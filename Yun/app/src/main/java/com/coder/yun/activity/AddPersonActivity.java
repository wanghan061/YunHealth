package com.coder.yun.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.coder.yun.util.TitleBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 添加个人信息页
 */

public class AddPersonActivity extends BaseActivity {

    private TextView tv_userName;
    private EditText et_name;
    private Spinner sp_sex;
    private Spinner sp_department;
    private TextView et_sex;
    private EditText et_tel;
    private EditText et_idcard;        //身份证号
    private TextView et_department;       //科室
    private Button bt_save;
    private String name, tel, idcard, department;
    private String sex;
    private String userName;
    private ArrayAdapter<CharSequence> adapter1, adapter2;
    String type;
    Intent intent;
    RequestQueue queue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        initTitle();
        initView();

        sp_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_sex.setText(adapter1.getItem(position));
                sex = (String) adapter1.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_department.setText(adapter2.getItem(position));
                department = (String) adapter2.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = tv_userName.getText().toString();
                name = et_name.getText().toString();
                tel = et_tel.getText().toString();
                idcard = et_idcard.getText().toString();
                //department = et_department.getText().toString();
                queue = Volley.newRequestQueue(getBaseContext());

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(idcard) && !TextUtils.isEmpty(department)) {

                    switch (type) {
                        case "0":
                            addPatientMethod();
                            break;
                        case "1":
                            addNurseMethod();
                            break;
                        case "2":
                            addDoctorMethod();
                            break;
                        default:
                            Toast.makeText(AddPersonActivity.this, "错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddPersonActivity.this, "请填写您的完整信息", Toast.LENGTH_SHORT).show();
                }

            }

            private void addPatientMethod() {

                StringRequest request = null;
                try {
                    request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                            "/servlet/PatientRegisterAddServlet?userName=" + userName + "&name=" + URLEncoder.encode(name, "UTF-8") + "&sex=" + URLEncoder.encode(sex, "UTF-8") + "&tel=" + tel + "&idcard=" + idcard + "&department=" + URLEncoder.encode(department, "UTF-8"),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.i("TAG", s);

                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String code = jsonObject.getString("code");

                                        if ("10002".equals(code)) {
                                            saveInfo();
                                            Toast.makeText(AddPersonActivity.this, "添加成功,请重新登陆", Toast.LENGTH_SHORT).show();
                                            intent = new Intent(AddPersonActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(AddPersonActivity.this, "添加信息失败，请重试", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(AddPersonActivity.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                queue.add(request);


            }
        });
    }

    //将用户信息存储到数据库中
    private void saveInfo() {
        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
        editor.putString("userName", userName);
        editor.putString("name", name);
        editor.putString("sex", sex);
        editor.putString("tel", tel);
        editor.putString("idcard", idcard);
        editor.putString("department", department);
        editor.commit();
    }

    private void addDoctorMethod() {
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                    "/servlet/DoctorRegisterAddServlet?userName=" + userName + "&name=" + URLEncoder.encode(name, "UTF-8") + "&sex=" + URLEncoder.encode(sex, "UTF-8") + "&tel=" + tel + "&idcard=" + idcard + "&department=" + URLEncoder.encode(department, "UTF-8"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("TAG", s);

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");

                                if ("10002".equals(code)) {
                                    saveInfo();
                                    Toast.makeText(AddPersonActivity.this, "添加成功,请重新登陆", Toast.LENGTH_SHORT).show();
                                    intent = new Intent(AddPersonActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AddPersonActivity.this, "添加信息失败，请重试", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddPersonActivity.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);

    }

    private void addNurseMethod() {
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                    "/servlet/NurseRegisterAddServlet?userName=" + userName + "&name=" + URLEncoder.encode(name, "UTF-8") + "&sex=" + URLEncoder.encode(sex, "UTF-8") + "&tel=" + tel + "&idcard=" + idcard + "&department=" + URLEncoder.encode(department, "UTF-8"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i("TAG", s);

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");

                                if ("10002".equals(code)) {
                                    saveInfo();
                                    Toast.makeText(AddPersonActivity.this, "添加成功,请重新登陆", Toast.LENGTH_SHORT).show();
                                    intent = new Intent(AddPersonActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AddPersonActivity.this, "添加信息失败，请重试", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddPersonActivity.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);

    }


    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        tv_userName = (TextView) findViewById(R.id.tv_username);
        et_name = (EditText) findViewById(R.id.ed1);
        sp_sex = (Spinner) findViewById(R.id.spinner1);
        sp_department = (Spinner) findViewById(R.id.spinner2);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sex.setAdapter(adapter1);
        sp_sex.setVisibility(View.VISIBLE);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.department, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_department.setAdapter(adapter2);
        sp_department.setVisibility(View.VISIBLE);

        et_sex = (TextView) findViewById(R.id.editText);
        et_tel = (EditText) findViewById(R.id.ed2);
        et_idcard = (EditText) findViewById(R.id.ed3);
        et_department = (TextView) findViewById(R.id.ed4);
        bt_save = (Button) findViewById(R.id.btn_submit);
        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("username");
        type = intent1.getStringExtra("extra_type");
        tv_userName.setText(userName);

    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("个人信息");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

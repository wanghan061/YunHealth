package com.coder.yun.activity.nurse;

import android.app.Activity;
import android.os.Bundle;
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
import com.coder.yun.util.Constants;
import com.coder.yun.util.TitleBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 添加提醒内容
 */

public class AddRemind extends Activity {

    private EditText drugName;
    private EditText name;
    private EditText drugWay;
    private Button btn_cancel;
    private Button btn_save;
    String patientName, medicine, medicineWay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_remind);
        initTitle();
        initView();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRemind();
            }
        });
    }

    private void saveRemind() {
        patientName = name.getText().toString();
        medicine = drugName.getText().toString();
        medicineWay = drugWay.getText().toString();
        if (!TextUtils.isEmpty(patientName) && !TextUtils.isEmpty(medicine) && !TextUtils.isEmpty(medicineWay)) {
            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = null;
            try {
                request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                        "servlet/MedicineAddServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8") + "&medicine=" + URLEncoder.encode(medicine, "UTF-8") + "&medicineWay=" + URLEncoder.encode(medicineWay, "UTF-8"),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(s);
                                    String code = jsonObject.getString("code");
                                    if ("10002".equals(code)) {
                                        Toast.makeText(AddRemind.this, "添加提醒成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(AddRemind.this, "添加提醒失败", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(AddRemind.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mQueue.add(request);
        } else {
            Toast.makeText(AddRemind.this, "请输入完整的提醒信息", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        drugName = (EditText) findViewById(R.id.et_drug_name);
        name = (EditText) findViewById(R.id.et_name);
        drugWay = (EditText) findViewById(R.id.et_drug_way);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("添加提醒");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

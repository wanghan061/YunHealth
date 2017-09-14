package com.coder.yun.activity.doctor;

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
import java.util.Calendar;

/**
 * 医嘱列表中的添加医嘱按钮
 */

public class AddDoctorAdvice extends BaseActivity {


    private EditText name;
    private EditText bedNo;
    private TextView department;
    private EditText context;
    private TextView type;
    private Spinner sp_type;
    private Spinner sp_department;
    private Button add;
    private Button cancel;
    private String userName, patientName, keshi, bed, suggest, adviceType, date;
    private ArrayAdapter<CharSequence> adapter1, adapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_advice);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        date = year + "-" + month + "-" + day;

        initTitle();
        initView();


        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type.setText(adapter1.getItem(position));
                adviceType = (String) adapter1.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        name = (EditText) findViewById(R.id.et_name);
        bedNo = (EditText) findViewById(R.id.et_bed);
        department = (TextView) findViewById(R.id.tv_department);
        context = (EditText) findViewById(R.id.et_context);
        type = (TextView) findViewById(R.id.tv_type);
        sp_type = (Spinner) findViewById(R.id.spinner3);
        //sp_department= (Spinner) findViewById(R.id.spinner4);
        add = (Button) findViewById(R.id.bt_add);
        cancel = (Button) findViewById(R.id.bt_cancel);
        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");

        adapter1 = ArrayAdapter.createFromResource(this, R.array.yizhuleixing, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapter1);
        sp_type.setVisibility(View.VISIBLE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientName = name.getText().toString();
                bed = bedNo.getText().toString();
//                keshi = department.getText().toString();
                suggest = context.getText().toString();
                adviceType = type.getText().toString();

                if (!TextUtils.isEmpty(patientName) && !TextUtils.isEmpty(bed) && !TextUtils.isEmpty(suggest) && !TextUtils.isEmpty(adviceType)) {
                    sendAdvice();
                } else {
                    Toast.makeText(AddDoctorAdvice.this, "请输入完整的信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendAdvice() {
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "/servlet/DoctorAdviceAddServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8") + "&roomNo=" + bed + "&department=" + "&suggest=" + URLEncoder.encode(suggest, "UTF-8") + "&type=" + URLEncoder.encode(adviceType, "UTF-8") + "&userName=" + userName + "&date=" + date,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("TAG", s.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");

                                if ("10002".equals(code)) {
                                    Toast.makeText(AddDoctorAdvice.this, "添加医嘱成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddDoctorAdvice.this, "添加医嘱失败,请重试", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(AddDoctorAdvice.this, "网络连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);

    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("添加医嘱");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

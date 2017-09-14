package com.coder.yun.activity.normal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 专家预约
 */

public class AppointDetail extends BaseActivity {

    private TextView doctorName;
    private TextView zhicheng;
    private TextView yiling;
    private TextView jianjie;
    private Button yuyue;
    private String name,userName;

    private TextView showDate = null;
    private Button pickDate = null;
    private int mYear;
    private int mMonth;
    private int mDay;

    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_TIMEPICK = 2;
    private static final int TIME_DIALOG_ID = 3;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment);
        initTitle();
        initView();
        getInfo();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        setDateTime();

    }

    private void getInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                    "servlet/AppointmentQueryServlet?name=" + URLEncoder.encode(name,"UTF-8"),
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
                            Toast.makeText(AppointDetail.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        queue.add(request);
    }

    private void parsedoc(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        Map<String, Object> doctor;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            doctor = doctors.get(0);

            doctorName.setText(doctor.get("name").toString());
            zhicheng.setText(doctor.get("position").toString());
            yiling.setText(doctor.get("year").toString());
            jianjie.setText(doctor.get("introduce").toString());
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
        doctorName = (TextView) findViewById(R.id.tv_doctor_name);
        zhicheng = (TextView) findViewById(R.id.tv_zhicheng);
        yiling = (TextView) findViewById(R.id.tv_keshi);
        jianjie = (TextView) findViewById(R.id.tv_jianjie);
        yuyue = (Button) findViewById(R.id.bt_yuyue);

        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");
        Log.d("456",userName);

        yuyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInfo();
            }
        });

        showDate = (TextView) findViewById(R.id.tv_date);
        pickDate = (Button) findViewById(R.id.bt_selectDate);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickDate.equals((Button) v)) {
                    msg.what = AppointDetail.SHOW_DATAPICK;
                }
                AppointDetail.this.dateandtimeHandler.sendMessage(msg);
            }
        });


        Intent intent = getIntent();
        name = intent.getStringExtra("name");

    }

    private void sendInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = null;
        try {
            request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                    "servlet/AppointmentAddServlet?userName=" + URLEncoder.encode(userName, "UTF-8") + "&doctorName=" + URLEncoder.encode(name, "UTF-8") + "&date=" + showDate.getText()+"&stage="+URLEncoder.encode("上午", "UTF-8"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("TAG2", s.toString());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");

                                if ("10002".equals(code)) {
                                    Toast.makeText(AppointDetail.this, "预约成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AppointDetail.this, "预约失败,请重试", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(AppointDetail.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        queue.add(request);
    }

    /**
     * 设置日期
     */
    private void setDateTime(){
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();
    }

    /**
     * 更新日期显示
     */
    private void updateDateDisplay(){
        showDate.setText(new StringBuilder().append(mYear).append("-")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                .append((mDay < 10) ? "0" + mDay : mDay));
    }

    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            updateDateDisplay();
        }
    };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
//            case TIME_DIALOG_ID:
//                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
        }

        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
//            case TIME_DIALOG_ID:
//                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
//                break;
        }
    }

    /**
     * 处理日期和时间控件的Handler
     */
    Handler dateandtimeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppointDetail.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case AppointDetail.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }

    };



    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("专家预约");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

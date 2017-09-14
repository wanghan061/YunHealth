package com.coder.yun.activity.nurse;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
 * 调整床位
 */

public class AddBed extends BaseActivity {

    private EditText name;
    private EditText bedId;
    private TextView date;
    private Button submit;
    private Button cancel;
    String patientName, roomNo, outDate;

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
        setContentView(R.layout.bed_add);
        initTitle();
        initView();


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        setDateTime();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientName = name.getText().toString();
                roomNo = bedId.getText().toString();
                outDate = date.getText().toString();
                submitInfo();
            }
        });
    }

    private void setDateTime() {
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();
    }

    private void updateDateDisplay() {
        showDate.setText(new StringBuilder().append(mYear).append("-")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                .append((mDay < 10) ? "0" + mDay : mDay));
    }

    private void submitInfo() {
        if (!TextUtils.isEmpty(patientName) && !TextUtils.isEmpty(roomNo) && !TextUtils.isEmpty(outDate)) {
            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = null;
            try {
                request = new StringRequest(Request.Method.GET, Constants.SERVER_ADDRESS +
                        "servlet/CheckBedAddServlet?patientName=" + URLEncoder.encode(patientName, "UTF-8") + "&roomNo=" + roomNo + "&outDate=" + showDate.getText(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String code = jsonObject.getString("code");
                                    if ("10002".equals(code)) {
                                        Toast.makeText(AddBed.this, "增加床位成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(AddBed.this, "增加床位失败", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(AddBed.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mQueue.add(request);
        } else {
            Toast.makeText(AddBed.this, "请输入完整的床位信息", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        name = (EditText) findViewById(R.id.et_name);
        bedId = (EditText) findViewById(R.id.et_bed_id);
        date = (TextView) findViewById(R.id.tv_date);
        submit = (Button) findViewById(R.id.btn_submit);
        cancel = (Button) findViewById(R.id.btn_cancel);

        showDate = (TextView) findViewById(R.id.tv_date);
        pickDate = (Button) findViewById(R.id.bt_selectDate);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickDate.equals((Button) v)) {
                    msg.what = AddBed.SHOW_DATAPICK;
                }
                AddBed.this.dateandtimeHandler.sendMessage(msg);
            }
        });

    }

    @Override
    public void initAction() {

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
                case AddBed.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case AddBed.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }

    };

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("床位调整");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

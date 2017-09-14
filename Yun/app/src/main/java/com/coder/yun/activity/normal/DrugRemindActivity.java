package com.coder.yun.activity.normal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.adapter.DrugRemindAdapter;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.Constants;
import com.coder.yun.util.IStringRequest;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用药提醒的提醒列表
 */

public class DrugRemindActivity extends BaseActivity {

    private ListView listView;
    private List<Map<String, Object>> list1 = new ArrayList<>();
    private DrugRemindAdapter adapter;
    private String userName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_remind);
        initTitle();
        initView();
        getRemindInfo();
    }

    private void getRemindInfo() {
        RequestQueue queue = Volley.newRequestQueue(this);
        IStringRequest request = new IStringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                "servlet/MedicineInfoShowServlet?userName=" + userName,
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
                        Log.e("TAG", volleyError.toString());
                        Toast.makeText(DrugRemindActivity.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }

    private void parsedoc(String s) {
        Map<String, Object> object = null;
        List<Map<String, Object>> doctors = null;
        try {
            object = JsonUtils.getMapObj(s);
            doctors = JsonUtils.getListMap(object.get("data").toString());
            list1 = doctors;
            adapter = new DrugRemindAdapter(list1, getBaseContext());
            listView.setAdapter(adapter);

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
        listView = (ListView) findViewById(R.id.list_view);
        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");
    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("提醒列表");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

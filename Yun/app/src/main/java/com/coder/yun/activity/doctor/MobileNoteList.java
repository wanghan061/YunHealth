package com.coder.yun.activity.doctor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coder.yun.R;
import com.coder.yun.adapter.NoteListAdapter;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.Constants;
import com.coder.yun.util.JsonUtils;
import com.coder.yun.util.TitleBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 移动笔记中的查看笔记
 */

public class MobileNoteList extends BaseActivity {

    private ListView noteList;
    private List<Map<String, Object>> list1 = new ArrayList<>();
    private NoteListAdapter adapter;
    String name,note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        initTitle();
        initView();
        getNoteList();
    }

    private void getNoteList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.SERVER_ADDRESS +
                "servlet/NoteQueryServlet?userName=" + name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("TAG", s.toString());
                        //Toast.makeText(MobileNoteList.this, s.toString(), Toast.LENGTH_SHORT).show();
                        parsedoc(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MobileNoteList.this, "服务器连接出现问题", Toast.LENGTH_SHORT).show();
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
            adapter = new NoteListAdapter(list1, getBaseContext());
            noteList.setAdapter(adapter);
            noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(MobileNoteList.this, FileContext.class);
//                    name = list1.get(position).get("userName").toString();
//                    note = list1.get(position).get("notes").toString();
//                    intent.putExtra("userName",name);
//                    intent.putExtra("note",note);
//                    startActivity(intent);
                }
            });
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
        noteList = (ListView) findViewById(R.id.lv_note);
        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        name = pref.getString("name", "");

    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("所有笔记");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.coder.yun.activity.normal;

/*
* 个人档案管理
* */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.coder.yun.R;
import com.coder.yun.activity.doctor.FileContext;
import com.coder.yun.util.TitleBarUtils;

public class NorFileActivity extends AppCompatActivity {

    private LinearLayout jiankangdangan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nor_file);
        initTitle();

        jiankangdangan = (LinearLayout) findViewById(R.id.jiangkangdangan);

        jiankangdangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NorFileActivity.this, FileContext.class);
                startActivity(intent);
            }
        });

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("档案管理");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

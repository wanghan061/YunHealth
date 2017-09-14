package com.coder.yun.activity.nurse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.coder.yun.R;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.TitleBarUtils;

/**
 * 记录列表中的记录内容
 */

public class RecordContext extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspect_record);
        initTitle();
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initAction() {

    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("记录列表");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

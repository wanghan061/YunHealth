package com.coder.yun.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coder.yun.R;
import com.coder.yun.activity.doctor.HealthList;
import com.coder.yun.activity.normal.CheckAnswer;
import com.coder.yun.activity.normal.MyQuestionList;
import com.coder.yun.activity.nurse.AdviceExecute;
import com.coder.yun.base.BaseActivity;
import com.coder.yun.util.TitleBarUtils;

import static com.coder.yun.R.id.tab_message;
import static com.coder.yun.R.id.wodejiuzhenren;

public class NurseUserActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageView4;
    private TextView username_text;

    private TextView textView5;
    private TextView id_sum;
    private TextView textView8;
    private LinearLayout dangan;
    private LinearLayout yizhu;
    private LinearLayout yijian;
    private LinearLayout about;
    private LinearLayout update;
    private LinearLayout xiugaimima;
    private LinearLayout resiglogin;



    TextView idSum;
    private Intent intent;
    private String userName;
    private String idNo, gender;
    //private RequestQueue queue;
    private String token;
    boolean first = true;
    private String mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_nurse);
        mType = getIntent().getStringExtra("type");
        initView();
        initTitle();

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    private void initTitle() {
        TitleBarUtils titleBarUtils = (TitleBarUtils) findViewById(R.id.titleBar);
        titleBarUtils.setTitle("个人中心");
        titleBarUtils.setLeftButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NurseUserActivity.this, MainActivity.class); //个人中心工具栏返回键点击崩溃???
                intent.putExtra("type", mType);
                startActivity(intent);
            }
        });
    }

    public void initView() {

        xiugaimima = (LinearLayout) findViewById(R.id.xiugaimima);
        update = (LinearLayout) findViewById(R.id.update);
        yizhu = (LinearLayout) findViewById(R.id.yizhu);
        about = (LinearLayout) findViewById(R.id.about);
        resiglogin = (LinearLayout) findViewById(R.id.resiglogin);
        username_text = (TextView) findViewById(R.id.username_text);
        dangan = (LinearLayout) findViewById(R.id.dangan);
        yijian = (LinearLayout) findViewById(R.id.yijian);

        SharedPreferences pref = getSharedPreferences("nameData", MODE_PRIVATE);
        userName = pref.getString("name", "");
        username_text.setText(userName);

        xiugaimima.setOnClickListener(this);
        update.setOnClickListener(this);
        about.setOnClickListener(this);
        yizhu.setOnClickListener(this);
        username_text.setOnClickListener(this);
        resiglogin.setOnClickListener(this);
        dangan.setOnClickListener(this);
        yijian.setOnClickListener(this);

    }

    @Override
    public void initAction() {

    }


    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.username_text:
                intent = new Intent(NurseUserActivity.this, PersonInfo.class);
                intent.putExtra("userInfo",mType);
                startActivity(intent);
                break;
            case tab_message:
                //intent = new Intent(NormalUserActivity.this, UnreadMessagesActivity.class);
                intent = new Intent(NurseUserActivity.this, CheckAnswer.class);
                startActivity(intent);
                break;
            case wodejiuzhenren:
                intent =new Intent(NurseUserActivity.this, MyQuestionList.class);
                startActivity(intent);
                break;
            case R.id.dangan:
                intent =new Intent(NurseUserActivity.this, HealthList.class);
                startActivity(intent);
                break;
            case R.id.yizhu:
                intent =new Intent(NurseUserActivity.this, AdviceExecute.class);
                startActivity(intent);
                break;
            case R.id.yijian:

                break;
            case R.id.about:
                intent = new Intent(NurseUserActivity.this, AboutUs.class);
                startActivity(intent);
                break;
            case R.id.update:

                break;
            case R.id.xiugaimima:
                intent = new Intent(NurseUserActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.resiglogin:
                intent = new Intent(NurseUserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


}

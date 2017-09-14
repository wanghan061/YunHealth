package com.coder.yun.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.coder.yun.R;
import com.coder.yun.adapter.MyListAdapter;
import com.coder.yun.fragment.DoctorFragment;
import com.coder.yun.fragment.NormalFragment;
import com.coder.yun.fragment.NurseFragment;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mListView;
    private FrameLayout mContentFrame;//内容区域
    private FragmentManager mSupportFragmentManager;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mListView = (ListView) findViewById(R.id.left_drawer);
        mToolbar.setTitle("医疗助手");//设置ToolBar 标题
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));//设置标题颜色
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mListView.setAdapter(new MyListAdapter(MainActivity.this));

        mType = getIntent().getStringExtra("type");
        Log.d("mtype", mType);

        mSupportFragmentManager = getSupportFragmentManager();
        NormalFragment mNormalFragment = new NormalFragment();
        NurseFragment mNurseFragment = new NurseFragment();
        DoctorFragment mDoctorFragment = new DoctorFragment();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//左边栏的按钮列表
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
//                        mDrawerLayout.closeDrawers();
//                        startActivity(new Intent(MainActivity.this, ExpertAppointActivity.class));
                        break;
                    case 1:
                        break;
                    case 2:
                        whichType();
//                        mDrawerLayout.closeDrawers();
//                        Intent intent = new Intent(MainActivity.this, NormalUserActivity.class);
//                        intent.putExtra("type", mType);
//                        startActivity(intent);
                        break;
                    case 3:
                        break;
                }
            }
        });

        switch (mType) {
            case "0":

                mSupportFragmentManager.beginTransaction().replace(R.id.content_frame, mNormalFragment, null).commit();
                break;
            case "1":

                mSupportFragmentManager.beginTransaction().replace(R.id.content_frame, mNurseFragment, null).commit();
                break;
            case "2":

                mSupportFragmentManager.beginTransaction().replace(R.id.content_frame, mDoctorFragment, null).commit();
                break;

            default:
                NurseFragment mFragment = new NurseFragment();
                mSupportFragmentManager.beginTransaction().replace(R.id.content_frame, mNormalFragment, null).commit();
                break;
        }


    }

    public void whichType() {

        switch (mType) {
            case "0":
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, NormalUserActivity.class);
                intent.putExtra("type", mType);
                startActivity(intent);
                //finish();
                break;
            case "1":
                mDrawerLayout.closeDrawers();
                Intent intent1 = new Intent(MainActivity.this, NurseUserActivity.class);
                intent1.putExtra("type", mType);
                startActivity(intent1);
                //finish();
                break;
            case "2":
                mDrawerLayout.closeDrawers();
                Intent intent2 = new Intent(MainActivity.this, DoctorUserActivity.class);
                intent2.putExtra("type", mType);
                startActivity(intent2);
                //finish();
                break;
        }


    }


    long time = 0;

    //按两次返回键退出程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (System.currentTimeMillis() - time > 2000) {
                time = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
        return true;
    }


    /**
     * 沉浸式
     *
     * @param hasFocus
     */

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

package com.android.guidepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.android.guidepage.util.SharedPreferencesUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private Context context;
    private boolean isFirstopen = false;
    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
    }



    private void initView() {

    }

    private void initData() {
        context = SplashActivity.this;
        sharedPreferencesUtil = new SharedPreferencesUtil(this);
        isFirstopen = sharedPreferencesUtil.getBooleanValue();
        Log.i("wj","isFirstopen ==" + isFirstopen);
        if(!isFirstopen){ // 不是第一次登陆
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    context.startActivity(intent);
                    finish();
                    return;
                }
            },3000);

        }else{
            new Handler().postDelayed(new Runnable() {//第一次登陆时1秒后跳到引导页
                @Override
                public void run() {
                    Intent intent=new Intent(context,WelcomeActivity.class);
                    context.startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 3000);
        }
    }
}

package com.android.guidepage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Window;

import com.android.guidepage.R;
import com.android.guidepage.base.BaseActivity;
import com.android.guidepage.util.SharedPreferencesUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends BaseActivity  {

    private Context context;
    private boolean isFirstopen = false;
    private boolean isFirstLogin = false;
    private SharedPreferencesUtil sharedPreferencesUtil;



    @Override
    public int getResId() {
        return R.layout.activity_splash;
    }



    public void initBaseData() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void initData() {
        context = SplashActivity.this;
        sharedPreferencesUtil = new SharedPreferencesUtil(this);
                isFirstopen = sharedPreferencesUtil.getBooleanValue(SharedPreferencesUtil.is_frist_open);
                isFirstLogin = sharedPreferencesUtil.getBooleanValue(SharedPreferencesUtil.login);
                if(!isFirstopen){ // 不是第一次登陆
                    if (!isFirstLogin){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent= new Intent(SplashActivity.this, LoginActivity.class);
                                context.startActivity(intent);
                                finish();
                                return;
                            }
                        },3000);
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent= new Intent(SplashActivity.this, MainActivity.class);
                                context.startActivity(intent);
                                finish();
                                return;
                            }
                        },3000);
            }


        }else{
            new Handler().postDelayed(new Runnable() {//第一次登陆时1秒后跳到引导页
                @Override
                public void run() {
                    Intent intent=new Intent(context, WelcomeActivity.class);
                    context.startActivity(intent);
                    sharedPreferencesUtil.putBooleanValue(SharedPreferencesUtil.is_frist_open,false);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 3000);
        }
    }




}

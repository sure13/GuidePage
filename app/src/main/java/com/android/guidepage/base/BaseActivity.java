package com.android.guidepage.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.guidepage.util.SharedPreferencesUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity  extends AppCompatActivity {

    private Context context;
    private SharedPreferencesUtil sharedPreferencesUtil;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseData();
        setContentView(getResId());
        initView();
        initData();
        initListener();
    }

    public void initBaseData() {
    }


    public void initView() {

    }

    public void initData() {

    }

    abstract public int getResId();


    public void initListener() {

    }

}

package com.android.guidepage.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.guidepage.R;
import com.android.guidepage.util.SharedPreferencesUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;

public class TheamActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout redLinearLayout;
    private LinearLayout greenLinearLayout;
    private LinearLayout blueLinearLayout;

    private ImageView redImageView;
    private ImageView greenImageView;
    private ImageView blueImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theam_activity);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        redLinearLayout = (LinearLayout) findViewById(R.id.red_theam);
        greenLinearLayout = (LinearLayout) findViewById(R.id.green_theam);
        blueLinearLayout = (LinearLayout) findViewById(R.id.blue_theam);
        redImageView = (ImageView) findViewById(R.id.red_theam_image);
        blueImageView = (ImageView) findViewById(R.id.blue_theam_image);
        greenImageView = (ImageView) findViewById(R.id.green_theam_image);
    }

    private void initData() {
        redImageView.setVisibility(View.GONE);
        greenImageView.setVisibility(View.GONE);
        blueImageView.setVisibility(View.VISIBLE);
    }


    private void initListener() {
        redLinearLayout.setOnClickListener(this);
        greenLinearLayout.setOnClickListener(this);
        blueLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.red_theam:
                setTheamColor(redImageView);
                SharedPreferencesUtil.putIntValue("theam",1);
                break;
            case R.id.green_theam:
                setTheamColor(greenImageView);
                SharedPreferencesUtil.putIntValue("theam",2);
                break;
            case R.id.blue_theam:
                setTheamColor(blueImageView);
                SharedPreferencesUtil.putIntValue("theam",3);
                break;
        }
    }


    private void setTheamColor(ImageView imageView){
        redImageView.setVisibility(View.GONE);
        greenImageView.setVisibility(View.GONE);
        blueImageView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        int theamValue = SharedPreferencesUtil.getIntValue("theam");
        switch (theamValue){
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

   
}

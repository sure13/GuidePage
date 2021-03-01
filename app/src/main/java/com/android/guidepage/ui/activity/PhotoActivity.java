package com.android.guidepage.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.android.guidepage.R;
import com.android.guidepage.base.BaseActivity;
import com.bumptech.glide.Glide;

import retrofit2.http.Url;

public class PhotoActivity extends BaseActivity {

    private ImageView imageView;
    private String url;
    @Override
    public int getResId() {
        return R.layout.photo_activity;
    }

    @Override
    public void initView() {
       imageView = (ImageView) findViewById(R.id.photo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        Log.i("wxy","-----------------url---------------"+url);
        Glide.with(this).load(url).error(R.mipmap.view_pager1).into(imageView);
    }
}

package com.android.guidepage.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.guidepage.R;
import com.android.guidepage.adapter.MyPagerAdapter;
import com.android.guidepage.base.BaseActivity;
import com.android.guidepage.rxjava.MeiZiBean;

import java.util.ArrayList;
import java.util.List;

public class PhotoShowActivity extends BaseActivity {

    private ViewPager viewPager;
    private ArrayList<MeiZiBean.DataBean> imageData ;
    private TextView textView;
    private MyPagerAdapter pagerAdapter;

    private ImageView saveImage;
    private int currentPosition = 0;
    private String url;



    @Override
    public int getResId() {
        return R.layout.dialog_photo;
    }

    @Override
    public void initData() {
        imageData = new ArrayList<>();
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("position",0);
        url = intent.getStringExtra("url");
        imageData = intent.getParcelableArrayListExtra("list");
        initAdapter();
    }

    private void initAdapter() {
        pagerAdapter = new MyPagerAdapter(PhotoShowActivity.this);
        pagerAdapter.setDataList(imageData);
        viewPager.setAdapter(pagerAdapter);
        textView.setText(currentPosition + 1 + "/" + imageData.size());
        viewPager.setCurrentItem(currentPosition,false);
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.dialog_photo_vp);
        textView = findViewById(R.id.dialog_photo_tv);
        saveImage = findViewById(R.id.save_image);
    }

    @Override
    public void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(position + 1 + "/" + imageData.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter.setOnClickListener(new MyPagerAdapter.OnClickListener() {
            @Override
            public void onItemCliclListener() {
                close();
            }
        });
    }

    private void close() {
        this.finish();
    }
}

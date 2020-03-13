package com.android.guidepage.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.guidepage.R;
import com.android.guidepage.adapter.ViewPagerAdapter;
import com.android.guidepage.base.BaseActivity;
import com.android.guidepage.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class WelcomeActivity  extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private LinearLayout pointLayout;
    private ViewPagerAdapter adapter;
    private List views;
    private Button startButton;
    private int[] guidePics = { R.layout.guide1, R.layout.guide2,
            R.layout.guide3 };//引导页中的三张图片布局
    private ImageView[] dots;//代表视图索引的圆圈
    private int currentIndex;//表示前一个视图的索引
    private Context context;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private boolean isFirstLogin = true;


    @Override
    public void initBaseData() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = WelcomeActivity.this;
        if(Build.VERSION.SDK_INT >= 19) {// android 4.4 以上 沉浸式状态栏
            Window window = getWindow();
            window.addFlags(67108864);
        }
    }

    @Override
    public int getResId() {
        return R.layout.activity_welcome;
    }


    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pointLayout=(LinearLayout) findViewById(R.id.pointLayout);
    }

    public void initData() {
        views = new ArrayList();
        sharedPreferencesUtil = new SharedPreferencesUtil(context);
        initGuidePage();
        setViewPagerAdapter();
        initDots();

    }

    private void initGuidePage() {
        for (int i = 0; i < guidePics.length; i++) {
            View view = LayoutInflater.from(context).inflate(guidePics[i], null);
            if ( i == guidePics.length - 1){
                startButton= (Button) view.findViewById(R.id.start_button);
                startButton.setOnClickListener(this);
            }
            views.add(view);
        }

    }

    private void initDots() {
        dots = new ImageView[guidePics.length];
        for (int i = 0; i < guidePics.length; i++) {
            dots[i] = (ImageView) pointLayout.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
        dots[currentIndex].setImageResource(R.mipmap.point_select);

    }

    private void setViewPagerAdapter() {
        adapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        isFirstLogin = sharedPreferencesUtil.getBooleanValue(SharedPreferencesUtil.login);
        if (!isFirstLogin){
            Intent intent = new Intent(context,MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else{
            Intent intent=new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    //设置当前视图
    private void setCurrentView(int position) {
        if(position < 0||position > guidePics.length - 1){
            return;
        }
        viewPager.setCurrentItem(position);

    }


    //设置当前圆圈状态
    private void setCurrentDot(int position) {
        if(position<0||position>guidePics.length-1||currentIndex == position){
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
        for (int i = 0 ; i < dots.length ; i ++){
            dots[i].setImageResource(R.mipmap.point_normal);
        }
        dots[position].setImageResource(R.mipmap.point_select);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
// 如果切换到后台，就设置下次不进入功能引导页
// SharePerferenceUtil.setFirstLogin(context, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDot(position);//设置圆圈状态
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

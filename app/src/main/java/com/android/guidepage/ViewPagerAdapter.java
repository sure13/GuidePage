package com.android.guidepage;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {

    private List views;

    public ViewPagerAdapter(List views){
        this.views = views;
    }

    //获得视图总数
    @Override
    public int getCount() {
        if (views != null){
            return views.size();
        }
        return 0;
    }

    //判断是否由对象生成视图
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    //初始化position位置的视图
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager)container).addView((View)views.get(position), 0);
        return views.get(position);
    }


    //销毁position位置的视图
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager)container).removeView((View)views.get(position));
    }

}

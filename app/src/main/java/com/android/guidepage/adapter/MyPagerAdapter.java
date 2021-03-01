package com.android.guidepage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.android.guidepage.rxjava.MeiZiBean;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private List<MeiZiBean.DataBean> dataList;
    private OnClickListener onClickListener;

    public MyPagerAdapter(Context context){
        this.context = context;
    }

    public void setDataList(List<MeiZiBean.DataBean> dataList) {
        this.dataList = dataList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);
        Glide.with(context).load(dataList.get(position).getUrl()).into(photoView);
        container.addView(photoView);

        photoView.setOnClickListener(new View.OnClickListener() { // 给每个ViewPager加载页添加点击事件,点击消失
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onItemCliclListener();
                }
            }
        });

        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface OnClickListener{
        void onItemCliclListener();
    }
}

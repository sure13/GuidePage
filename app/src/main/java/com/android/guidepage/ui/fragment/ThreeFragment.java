package com.android.guidepage.ui.fragment;


import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.guidepage.R;
import com.android.guidepage.bean.News;
import com.android.guidepage.util.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.android.guidepage.util.OkHttpUtils.getNewsList;

public class ThreeFragment extends Fragment {

    public static ThreeFragment threeFragment;

    private TextView textView;

    public static final ThreeFragment getInstance(){
        if (threeFragment == null){
            threeFragment = new ThreeFragment();
        }
        return threeFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_fragment,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        textView = (TextView) view.findViewById(R.id.test_web);
    }



    private List<News> newsList;
    String url ="http://military.people.com.cn/" ;
    @Override
    public void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                newsList = getNewsList(url);
                Log.i("wj","newsList 000  ===" + newsList);
            }
        }).start();

    }
}

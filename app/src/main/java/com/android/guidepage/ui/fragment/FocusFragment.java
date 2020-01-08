package com.android.guidepage.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.guidepage.R;
import com.android.guidepage.adapter.RecyclerViewAdapter;
import com.android.guidepage.bean.News;
import com.android.guidepage.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.android.guidepage.util.OkHttpUtils.getNewsList;

public class FocusFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private static Context context;
    private static  FocusFragment focusFragment;
    private List<News> newsList;
    String url ="https://www.huanqiu.com/" ;


    public static FocusFragment getInstance(final Context mContext){
        context = mContext;
        if (focusFragment == null){
            focusFragment = new FocusFragment();
        }
       return focusFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout_fragment,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }



    private void initData() {
        newsList = new ArrayList<News>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        new Thread(new Runnable() {
            @Override
            public void run() {
                newsList = getNewsList(url);
            }
        }).start();

    }


    @Override
    public void onStart() {
        super.onStart();
       handler.sendEmptyMessageDelayed(REFESH,3000);

    }

    private static final int REFESH = 1;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFESH:
                    adapter = new RecyclerViewAdapter(context,newsList);
                    recyclerView.setAdapter(adapter);
                    break;
                 default:
                     break;

            }
        }
    };

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }
}

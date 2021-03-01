package com.android.guidepage.ui.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.android.guidepage.util.OkHttpUtils.getNewsList;

public class FocusFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private static Context context;
    private static  FocusFragment focusFragment;
    private List<News> newsList;
    String url ="https://www.huanqiu.com/" ;
    private SwipeRefreshLayout swipeRefreshLayout;


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
        getData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setNewsList(newsList);
        swipeRefreshLayout.setColorSchemeResources(R.color.md_red_A100,R.color.md_teal_A400,R.color.md_indigo_A400);
        swipeRefreshLayout.setOnRefreshListener(this);
        showLoadingDialog();
    }

    private void showLoadingDialog() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("加载中........");
        dialog.show();
        handler.sendEmptyMessageDelayed(REFESH,3000);
        handler.sendEmptyMessageDelayed(LOADING,3000);
    }

    public void getData(){
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
    }


    private ProgressDialog dialog;
    @Override
    public void onResume() {
        super.onResume();
    }

    private static final int REFESH = 1;
    private static final  int LOADING =2;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFESH:
                    adapter.setNewsList(newsList);
                    adapter.notifyDataSetChanged();
                    break;
                case LOADING:
                    dialog.dismiss();
                    break;
                 default:
                     break;

            }
        }
    };

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
    }

    @Override
    public void onRefresh() {
        refreshUI();
    }


    public void refreshUI(){
        getData();
        handler.sendEmptyMessageDelayed(REFESH,2000);
        swipeRefreshLayout.setRefreshing(false);
    }
}

package com.android.guidepage.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.guidepage.MyApplication;
import com.android.guidepage.R;
import com.android.guidepage.adapter.TwoFragmentAdapter;
import com.android.guidepage.rxjava.Contast;
import com.android.guidepage.rxjava.MeiZiBean;
import com.android.guidepage.rxjava.PhotoService;
import com.android.guidepage.rxjava.RetrofitManager;
import com.android.guidepage.ui.activity.MainActivity;
import com.android.guidepage.util.NetworkUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TwoFragment extends Fragment {

    public static TwoFragment twoFragment;
    public static MainActivity mainActivity;
    private Context context;

    private RecyclerView recyclerView;
    private TwoFragmentAdapter adapter;
    private List<MeiZiBean.DataBean> dataBeanList;

    private TextView networkText;


    public static final TwoFragment getInstance(final MainActivity mActivity){
        if (twoFragment == null){
            twoFragment = new TwoFragment();
        }
        mainActivity = mActivity;
        return twoFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_fragment,container,false);
        initView(view);
        initData();
        return view;
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_two);
        networkText = (TextView) view.findViewById(R.id.network_text);
    }

    private void initData() {
        dataBeanList = new ArrayList<>();
        context = MyApplication.getInstance();
////        getData(1,45);
//////        Retrofit retrofit = RetrofitManager.getRetrofitInstance(Contast.API_SERVER);
//        adapter = new TwoFragmentAdapter(context,dataBeanList);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
//                4,StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        adapter.setOnclickListener(new TwoFragmentAdapter.setOnclickListener() {
            @Override
            public void onItemClickListener() {
                Log.i("wxy","---------------------onItemClickListener---------------------------");
            }

            @Override
            public void onLongClickListener() {
                Log.i("wxy","---------------------onLongClickListener---------------------------");
            }
        });
    }


    public void refeshUI(){
        adapter = new TwoFragmentAdapter(context,dataBeanList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setDataBeanList(dataBeanList);
        adapter.notifyDataSetChanged();
        initListener();
    }

    private void getData(int page, int number) {
        final Retrofit retrofit = RetrofitManager.getRetrofitInstance(Contast.API_SERVER);
        PhotoService photoService = retrofit.create(PhotoService.class);
        Observable<MeiZiBean> observable = photoService.getJsonList(page,number);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MeiZiBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("wxy","------onCompleted---------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        networkText.setText(R.string.connetction_error);
                        networkText.setVisibility(View.VISIBLE);
                        Log.i("wxy","------onError---------");
                    }

                    @Override
                    public void onNext(MeiZiBean meiZiBean) {
                        dataBeanList = meiZiBean.getData();
                        Log.i("wxy","------onNext---------"+dataBeanList);
                        refeshUI();
//                        adapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        boolean networkState = NetworkUtil.isNetworkReachable(context);
        networkText.setVisibility(View.GONE);
        if (networkState){
            getData(1,45);
        }else{
            networkText.setVisibility(View.VISIBLE);
        }

    }

}

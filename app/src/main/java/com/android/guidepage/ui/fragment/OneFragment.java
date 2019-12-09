package com.android.guidepage.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.guidepage.R;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

public class OneFragment extends Fragment {

    public static OneFragment oneFragment;

    private SearchView searchView;
    private View view;

    public static final OneFragment getInstance(){
        if (oneFragment == null){
            oneFragment = new OneFragment();
        }
        return oneFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment,container,false);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view){
        searchView = (SearchView) view.findViewById(R.id.serachview);
    }

    private void initData() {
        searchView.setQueryHint("请输入搜索内容");
        searchView.setIconifiedByDefault(false);

    }
}

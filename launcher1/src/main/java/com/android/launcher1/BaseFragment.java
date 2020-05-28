package com.android.launcher1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment implements BaseInterface{

	protected View rootView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(setResouce(), null);
		initView();
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initdate();
		initListener();
	}

	@Override
	public int setResouce() {
		return 0;
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void initdate() {
		
	}

	@Override
	public void initListener() {
		
	}
	
	
	
}

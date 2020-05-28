package com.android.launcher1;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class MyFragmentAdapter extends FragmentPagerAdapter {

	ArrayList<BaseFragment> fragments;

	public MyFragmentAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	//解决ViewPager数据源改变时，刷新无效的解决办法
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
    @Override 
    public long getItemId(int position) { 
    	return fragments.get(position).hashCode(); 
    }

}

package com.android.guidepage.adapter;


import android.content.Context;

import com.android.guidepage.ui.fragment.AmusementFragment;
import com.android.guidepage.ui.fragment.FocusFragment;
import com.android.guidepage.ui.fragment.PhotoFragment;
import com.android.guidepage.ui.fragment.RecommendFragment;
import com.android.guidepage.ui.fragment.ThreeFragment;
import com.android.guidepage.ui.fragment.TwoFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> titleList;
    private Context context;

    public TabViewPagerAdapter(FragmentManager fragmentManager,List<String> titleList,Context context){
        super(fragmentManager);
        this.titleList = titleList;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return  RecommendFragment.getInstance(context);
        }else if (position == 2){
            return  PhotoFragment.getInstance(context);
        }else if (position == 3){
            return new ThreeFragment();
        }else if (position == 4){
            return AmusementFragment.getInstance(context);
        }else if (position == 5){
            return new ThreeFragment();
        }else if (position == 6){
            return new ThreeFragment();
        }else if (position == 7){
            return new ThreeFragment();
        }
        return FocusFragment.getInstance(context);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return FragmentPagerAdapter.POSITION_NONE;
    }


}

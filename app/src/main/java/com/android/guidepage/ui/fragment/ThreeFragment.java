package com.android.guidepage.ui.fragment;

import android.app.Fragment;

public class ThreeFragment extends Fragment {

    public static ThreeFragment threeFragment;

    public static final ThreeFragment getInstance(){
        if (threeFragment == null){
            threeFragment = new ThreeFragment();
        }
        return threeFragment;
    }
}

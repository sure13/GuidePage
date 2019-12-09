package com.android.guidepage.ui.fragment;

import android.app.Fragment;

public class FourFragment extends Fragment {

    public static FourFragment fourFragment;

    public static final FourFragment getInstance(){
        if (fourFragment == null){
            fourFragment = new FourFragment();
        }
        return fourFragment;
    }
}

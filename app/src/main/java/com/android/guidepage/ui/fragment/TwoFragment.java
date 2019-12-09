package com.android.guidepage.ui.fragment;

import android.app.Fragment;

public class TwoFragment extends Fragment {

    public static TwoFragment twoFragment;

    public static final TwoFragment getInstance(){
        if (twoFragment == null){
            twoFragment = new TwoFragment();
        }
        return twoFragment;
    }
}

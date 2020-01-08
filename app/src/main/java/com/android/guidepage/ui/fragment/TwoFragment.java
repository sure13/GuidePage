package com.android.guidepage.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.guidepage.R;
import com.android.guidepage.ui.activity.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TwoFragment extends Fragment {

    public static TwoFragment twoFragment;
    public static MainActivity mainActivity;

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
        return view;
    }
}

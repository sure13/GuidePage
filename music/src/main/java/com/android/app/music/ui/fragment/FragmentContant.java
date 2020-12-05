package com.android.app.music.ui.fragment;

import android.content.Context;

public interface FragmentContant {

    interface LoginView{
        public void startMusicActivity(int position);
        public void initListView();
        public void initData();
    }

    interface LoginPersent{
        public void setMusicPlay(int position);
        public void getMusicInfo(Context context);
    }

    interface OnlineView{

    }

    interface OnlinePersent{

    }

}

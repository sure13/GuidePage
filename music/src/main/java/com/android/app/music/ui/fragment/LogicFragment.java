package com.android.app.music.ui.fragment;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;


import com.android.app.music.adapter.MusicAdapter;
import com.android.app.music.bean.Music;
import com.android.app.music.ui.activity.MusicActivity;
import com.android.app.music.utils.Common;
import com.master.permissionhelper.PermissionHelper;
import com.android.app.music.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass
 */

public class LogicFragment extends Fragment implements FragmentContant.LoginView {

    private String TAG = LogicFragment.class.getSimpleName();                                      //下面两个属性和获取mediadatabase的权限有关系，可查阅代码块下的链接
    private PermissionHelper permissionHelper;
    private ListView listView;                                                          //创建ListView的对象
    private List<Music> musicList;                                                          //将Music放入List集合中，并实例化List<Music>
    private List<ListView> listViewList;
    private MusicAdapter adapter;
    private Context mContext;
    public MyPersenter myPersenter;


    public LogicFragment() {
        // Required empty public constructor

    }

    //LoginFrangment中的onCreate()方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logic, container, false);        //创建View对象，返回view
        initData();
         // 关于权限的代码
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                initListView();                                          //获取权限后扫描数据库获取信息
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {
            }

            @Override
            public void onPermissionDenied() {

            }

            @Override
            public void onPermissionDeniedBySystem() {

            }
        });
// 权限代码结束

        //对Listview进行监听
        listView = view.findViewById(R.id.logic_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {             //将listView的每一个item实现监听
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myPersenter.setMusicPlay(position);
                adapter.notifyDataSetChanged();
                startMusicActivity(position);
            }
        });

        adapter = new MusicAdapter(getActivity(), musicList);                //创建MusicAdapter的对象，实现自定义适配器的创建
        listView.setAdapter(adapter);                                                 //listView绑定适配器
        return view;
    }


    @Override
    public void initData() {
        myPersenter = new MyPersenter();
        myPersenter.setLoginView(this);
        mContext = getActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startMusicActivity(int position) {
        //intent实现页面的跳转，getActivity()获取当前的activity， MusicActivity.class将要调转的activity
        Intent intent = new Intent(mContext, MusicActivity.class);
        //使用putExtra（）传值
        intent.putExtra("position", position);
        startActivity(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.nf_title_tv, Common.musicList.get(position).title);
        remoteViews.setTextViewText(R.id.nf_artist_tv, Common.musicList.get(position).artist);
        if (Common.musicList.get(position).albumBip != null) {
            remoteViews.setImageViewBitmap(R.id.nf_album_imgv, Common.musicList.get(position).albumBip);
        }
        //通知
        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContent(remoteViews);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("我的通知");
        builder.setContentText("正在播放音乐");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    // 权限代码
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
// 权限代码结束

    //nitListView()实现对手机中MediaDataBase的扫描
    public void initListView() {
       myPersenter.getMusicInfo(mContext);                                                               //关闭游标
    }



    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}

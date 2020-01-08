package com.android.guidepage.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.guidepage.R;
import com.android.guidepage.adapter.TabViewPagerAdapter;
import com.android.guidepage.ui.activity.MainActivity;
import com.android.guidepage.util.OkHttpUtils;
import com.google.android.material.tabs.TabLayout;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import static android.app.Activity.RESULT_OK;

public class OneFragment extends Fragment implements View.OnClickListener {

    public static OneFragment oneFragment;

    private SearchView searchView;
    private View view;
    private Button button;
    private ImageView searchImage;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String[] title;

    private static MainActivity mainActivity;
    private static final int REQUEST_CODE_SCAN = 1 ;
    private static final int RC_PERMISSION = 2;

    public static final OneFragment getInstance(final MainActivity mContext){
        if (oneFragment == null){
            oneFragment = new OneFragment();
        }
        mainActivity = mContext;
        return oneFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment,container,false);
        initView(view);
        initData();
        initListener();
        return view;
    }

//    ArrayList<Article> article;
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case  1:
//                    article = (ArrayList<Article>) msg.obj;
//                    Log.i("wj","test == " + article);
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    };

    public void initView(View view){
        searchView = (SearchView) view.findViewById(R.id.serachview);
        button = (Button) view.findViewById(R.id.test);
        searchImage = (ImageView) view.findViewById(R.id.image_search);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewPager = (ViewPager) view.findViewById(R.id.tab_view_pager);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         //       final String url = "http://www.baidu.com//";
                final String url ="http://www.ifeng.com/" ;
                new Thread(){
                    public void run(){
                        String html = OkHttpUtils.getArticleData(url);
                        Log.i("wj","html == " + html);
               //         ArrayList<Article> articles = OkHttpUtils.getArticleList(url);
                   //     Log.i("wj","articles == " + articles);
                        //发送信息给handler用于更新UI界面
//                        Message message = handler.obtainMessage();
//                        message.what = 1;
//                        message.obj = articles;
//                        handler.sendMessage(message);
                    }
                }.start();

            }
        });
    }

    private void initListener() {
        searchImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_search:
                checkPerssion();
                startZxing();//扫一扫
                break;
            default:
                break;


        }
    }

    private void checkPerssion() {
        if (!(ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            || !(ActivityCompat.checkSelfPermission(mainActivity,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            //没有权限，申请权限
            String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
            //申请权限，其中RC_PERMISSION是权限申请码，用来标志权限申请的
            ActivityCompat.requestPermissions(mainActivity,permissions, RC_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION && grantResults.length == 2 &&
                grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Log.i("wxy","权限申请成功");
        }else{
            Log.i("wxy","权限申请失败");
        }
    }

    private void startZxing() {
        Intent intent = new Intent(mainActivity, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
               // result.setText("扫描结果为：" + content);
            }
        }
    }

    private void initData() {
        searchView.setQueryHint("请输入搜索内容");
        searchView.setIconifiedByDefault(false);
        title = new String[]{"关注","推荐","视频","体育","娱乐","文化","热点","小说"};
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        initTitltAndFragment();
        adapter = new TabViewPagerAdapter(getFragmentManager(),titleList,mainActivity);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);//与ViewPage建立关系
        initTablayout();
    }

    private void initTablayout() {
        one = tabLayout.getTabAt(0);
        two = tabLayout.getTabAt(1);
        three = tabLayout.getTabAt(2);
        four = tabLayout.getTabAt(3);
        five = tabLayout.getTabAt(4);
        six = tabLayout.getTabAt(5);
        seven = tabLayout.getTabAt(6);
        eight = tabLayout.getTabAt(7);
    }

    private TabLayout.Tab one,two,three,four,five,six,seven,eight;

    private void initTitltAndFragment() {
        for(int i = 0; i < title.length; i++){
            titleList.add(title[i]);
        }

    }
}

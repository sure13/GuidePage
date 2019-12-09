package com.android.guidepage.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.guidepage.Dao.Dao;
import com.android.guidepage.R;
import com.android.guidepage.ui.fragment.FourFragment;
import com.android.guidepage.ui.fragment.OneFragment;
import com.android.guidepage.ui.fragment.ThreeFragment;
import com.android.guidepage.ui.fragment.TwoFragment;
import com.android.guidepage.view.CircleImageView;
import com.google.android.material.navigation.NavigationView;
import com.yanzhenjie.album.Album;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View view;
    private CircleImageView circleImageView;

    private FrameLayout frameLayout;
    private RelativeLayout relativeLayout_message;
    private RelativeLayout relativeLayout_news;
    private RelativeLayout relativeLayout_contacts;
    private RelativeLayout relativeLayout_setting;

    private OneFragment oneFragment;
    private TwoFragment twoFragment ;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;
    private FragmentManager manager;
    private FragmentTransaction transaction ;

    private AlertDialog.Builder dialog;
    private String[] items;
    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CROP_PHOTO = 2;//裁剪
    public static final int SELECT_PIC = 0;//从相册选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        initView();
        initListener();
        initData();
    }

    private void initView(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        view = navigationView.getHeaderView(0);
        circleImageView = (CircleImageView) view.findViewById(R.id.circle);
        frameLayout = (FrameLayout) findViewById(R.id.content);
        relativeLayout_message = (RelativeLayout) findViewById(R.id.message_layout);
        relativeLayout_contacts = (RelativeLayout) findViewById(R.id.contacts_layout);
        relativeLayout_news = (RelativeLayout) findViewById(R.id.news_layout);
        relativeLayout_setting = (RelativeLayout) findViewById(R.id.setting_layout);

    }


    private void initListener() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        circleImageView.setOnClickListener(this);
        relativeLayout_setting.setOnClickListener(this);
        relativeLayout_news.setOnClickListener(this);
        relativeLayout_contacts.setOnClickListener(this);
        relativeLayout_message.setOnClickListener(this);

    }

    private void initData() {
        setTabSelection(0);
        items = new String[]{"拍照中获取","从相册中选择","取消"};
    }


    @Override
    protected void onDestroy() {
        oneFragment = null;
        twoFragment = null;
        threeFragment = null;
        fourFragment = null;
        super.onDestroy();
    }

    private void setTabSelection(int index) {
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case 0:
                if (oneFragment == null){
                    oneFragment = OneFragment.getInstance();
                    transaction.add(R.id.content,oneFragment);
                }else{
                    transaction.show(oneFragment);
                }
                break;

            case 1:
                if (twoFragment == null){
                    twoFragment = TwoFragment.getInstance();
                    transaction.add(R.id.content,twoFragment);
                }else{
                    transaction.show(twoFragment);
                }
                break;
            case 2:
                if (threeFragment == null){
                    threeFragment = ThreeFragment.getInstance();
                    transaction.add(R.id.content,threeFragment);
                }else{
                    transaction.show(threeFragment);
                }
                break;
            case 3:
                if (fourFragment == null){
                    fourFragment = FourFragment.getInstance();
                    transaction.add(R.id.content,fourFragment);
                }else{
                    transaction.show(fourFragment);
                }
                break;
            default:
                break;

        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (oneFragment != null){
            transaction.hide(oneFragment);
        }
        if (twoFragment != null){
            transaction.hide(twoFragment);
        }
        if (threeFragment != null){
            transaction.hide(threeFragment);
        }
        if (fourFragment != null){
            transaction.hide(fourFragment);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.circle:
                showChooseDialog();
                break;
            case R.id.message_layout:
                setTabSelection(0);
                break;
            case R.id.news_layout:
                setTabSelection(1);
                break;
            case R.id.contacts_layout:
                setTabSelection(2);
                break;
            case R.id.setting_layout:
                setTabSelection(3);
                break;
        }

    }

    private void showChooseDialog() {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("更换头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case TAKE_PHOTO://拍照
                                goCamera();
                                break;
                            case SELECT_PIC://从相册选择
                                goPhotoAlbum();
                                break;
                            case 3:
                                break;
                        }

                    }
                });
        dialog.create().show();
    }

    public static final int ACTIVITY_REQUEST_SELECT_PHOTO = 100;

    private void goCamera() {
        Intent intent = new Intent();
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        startActivityForResult(intent,1);

    }


    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photoPath;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = data.getParcelableExtra("data");
            circleImageView.setImageBitmap(bitmap);
            Uri uri = data.getData();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                //获取数据
                //获取内容解析者对象
                try {
                    Bitmap mBitmap = BitmapFactory.decodeStream(getApplication().getContentResolver().openInputStream(data.getData()));
                    circleImageView.setImageBitmap(mBitmap);
                    savePicturePath(bitmabToBytes(mBitmap));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == 100 && resultCode == RESULT_OK) {
            List<String> pathList = Album.parseResult(data);

        }
    }

    //图片转为二进制数据
    public byte[] bitmabToBytes(Bitmap bitmap){
        //将图片转化为位图
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        }catch (Exception e){
        }finally {
            try {
                bitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    @Override
    protected void onStart() {
        super.onStart();
        byte[] imgData= getPicturePath();
        if (imgData != null){
            Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            circleImageView.setImageBitmap(imagebitmap);
        }else {
            circleImageView.setImageResource(R.mipmap.icon);
        }
    }


    private void savePicturePath(byte[] bytes) {
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.action.test/" + Dao.USER_TABLE );
        ContentValues values = new ContentValues();
        values.put(Dao.PICTURE,bytes);
        resolver.update(uri,values,null,null);
    }

    private byte[] getPicturePath() {
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.action.test/" + Dao.USER_TABLE );
        byte[] imgData = null;
        Cursor cursor = resolver.query(uri,new String[]{Dao.ID,Dao.PICTURE},null,null,null);
        while (cursor.moveToNext()) {
            imgData = cursor.getBlob(cursor.getColumnIndex(Dao.PICTURE));
        }
        if (cursor != null){
            cursor.close();
        }
        return  imgData ;

    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.modle:
                Toast.makeText(this,"this is modle ",Toast.LENGTH_LONG).show();
                break;
            case  R.id.setting:
                Toast.makeText(this,"this is setting ",Toast.LENGTH_LONG).show();
                break;
            case R.id.farvorate:
                Toast.makeText(this,"this is farvorate ",Toast.LENGTH_LONG).show();
                break;
            case R.id.theam:
                Toast.makeText(this,"this is theam ",Toast.LENGTH_LONG).show();
                break;
            case R.id.about:
                Toast.makeText(this,"this is about ",Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
}

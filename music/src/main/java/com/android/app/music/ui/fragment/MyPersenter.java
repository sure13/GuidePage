package com.android.app.music.ui.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.android.app.music.bean.Music;
import com.android.app.music.utils.Common;
import com.android.app.music.R;


public class MyPersenter implements FragmentContant.LoginPersent {


    public  FragmentContant.LoginView loginView;


  public void setLoginView(FragmentContant.LoginView loginView){
        this.loginView = loginView;
  }


    @Override
    public void setMusicPlay(int position) {
        for (Music m : Common.musicList
        ) {
            m.isPlaying = false;
        }
        Common.musicList.get(position).isPlaying = true;
    }

    @Override
    public void getMusicInfo(Context context) {
        Common.musicList.clear();
        //获取ContentResolver的对象，并进行实例化
        ContentResolver resolver = context.getContentResolver();
        //获取游标
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER); //创建游标MediaStore.Audio.Media.EXTERNAL_CONTENT_URI获取音频的文件，后面的是关于select筛选条件，这里填土null就可以了
        //游标归零
        if(cursor.moveToFirst()){
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));            //获取歌名
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));         //获取歌唱者
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));           //获取专辑名
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));            //获取专辑图片id
                int length = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //创建Music对象，并赋值
                Music music = new Music();
                music.length = length;
                music.title = title;
                music.artist = artist;
                music.album = album;
                music.path = path;
                music.albumBip = getAlbumArt(albumID,context);
                //将music放入musicList集合中
                Common.musicList.add(music);
            }  while (cursor.moveToNext());
        }else {
            Toast.makeText(context, "本地没有音乐哦", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    //获取专辑图片的方法
    private Bitmap getAlbumArt(int album_id,Context context) {                              //前面我们只是获取了专辑图片id，在这里实现通过id获取掉专辑图片
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {
            bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.touxiang1);
        }
        return bm;
    }

}

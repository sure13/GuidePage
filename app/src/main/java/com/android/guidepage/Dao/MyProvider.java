package com.android.guidepage.Dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Administrator on 2019/7/29 0029.
 */

public class MyProvider extends ContentProvider {


    private Context context;
    public static final String AUTHORITY = "com.action.test";
    private DaoHelper daoHelper = null;
    private SQLiteDatabase db = null ;

    private static final  UriMatcher mMatcher;
    private static final int USER_CODE = 1;


    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, Dao.USER_TABLE,USER_CODE);
    }



    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public boolean onCreate() {

        context = getContext();
        daoHelper = new DaoHelper(context);
        return false;
    }




    @Override
    public Uri insert( Uri uri,  ContentValues contentValues) {
          String tableName = getTableName(uri);
          db = daoHelper.getWritableDatabase();
          db.insert(tableName,null,contentValues);
          getContext().getContentResolver().notifyChange(uri,null);

        return uri;
    }



    @Override
    public Cursor query( Uri uri,  String[] strings,  String s,  String[] strings1,  String s1) {
        String table = getTableName(uri);
        db = daoHelper.getWritableDatabase();
        return db.query(table,strings,s,strings1,null,null,s1);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues,  String s,  String[] strings) {
        String table = getTableName(uri);
        db = daoHelper.getWritableDatabase();
        int update = db.update(table,contentValues,s,strings);
        getContext().getContentResolver().notifyChange(uri,null);
        return update;
    }


    @Override
    public int delete( Uri uri, String s,  String[] strings) {
        String table = getTableName(uri);
        db = daoHelper.getWritableDatabase();
        int delete = db.delete(table,s,strings);
        getContext().getContentResolver().notifyChange(uri,null);
        return delete;
    }


    private String getTableName(Uri uri){
        String tableName = null;
        switch (mMatcher.match(uri)){
            case USER_CODE:
                tableName = Dao.USER_TABLE;
                break;
        }
        return tableName;
    }
}

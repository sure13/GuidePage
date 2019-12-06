package com.android.guidepage.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Administrator on 2019/7/29 0029.
 */

public class DaoHelper   extends SQLiteOpenHelper {


    public DaoHelper(Context context ){
        super(context, Dao.DATABASE_NAME,null , Dao.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
   sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  " + Dao.USER_TABLE  + " ( "
          + Dao.ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
           + Dao.NAME  + " VARCHAR(30),"
           + Dao.ACCOUNT + " VARCHAR(30),"
           + Dao.PASSWORD  + " VARCHAR(30)" + ");");

   initData(sqLiteDatabase);

    }

    public void  initData(SQLiteDatabase sqLiteDatabase){

        ContentValues values = new ContentValues();
        values.put(Dao.ID,Dao.ID_1);
        values.put(Dao.NAME,Dao.TEST_NAME);
        values.put(Dao.PASSWORD,Dao.TEST_PASSWORD);
        values.put(Dao.ACCOUNT,Dao.TEST_ACCOUNT);
        sqLiteDatabase.insert(Dao.USER_TABLE,null,values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Dao.USER_TABLE  );
        onCreate(sqLiteDatabase);
    }
}

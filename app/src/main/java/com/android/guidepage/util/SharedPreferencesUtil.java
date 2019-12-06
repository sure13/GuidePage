package com.android.guidepage.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    public static String SharedPreferencesName = "share_name";
    public static String is_frist_open = "is_frist_open";
    public static String login = "login";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public Context context;

    public  SharedPreferencesUtil(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SharedPreferencesName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

//    public static boolean getBooleanValue(){
//        boolean value = sharedPreferences.getBoolean(is_frist_open,true);
//        return value;
//    }
//
//    public static void putBooleanValue(boolean value){
//        editor.putBoolean(is_frist_open,value);
//        editor.commit();
//
//    }

    public static boolean getBooleanValue(String key){
        boolean value = sharedPreferences.getBoolean(key,true);
        return value;
    }

    public static void putBooleanValue(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();

    }


}

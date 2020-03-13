package com.android.guidepage.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.guidepage.Dao.Dao;
import com.android.guidepage.R;
import com.android.guidepage.base.BaseActivity;
import com.android.guidepage.util.SharedPreferencesUtil;

import net.anumbrella.customedittext.FloatLabelView;
import com.android.guidepage.util.pubFun;


/**
 * Created by Administrator on 2019/7/26 0026.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

   private Button button_login;
   private Button button_register;
   private TextView forget_text;
   private FloatLabelView phone_float;
   private FloatLabelView password_float;
   private SharedPreferencesUtil sharedPreferencesUtil;
   private ProgressDialog dialog;



    @Override
    public int getResId() {
        return R.layout.login;
    }



    public void initView(){
   //     button_back = (Button) findViewById(R.id.btn_back);
        button_login = (Button) findViewById(R.id.btn_login);
        button_register = (Button) findViewById(R.id.btn_register);
        forget_text = (TextView) findViewById(R.id.forget_password);
        phone_float = (FloatLabelView) findViewById(R.id.login_phone);
        password_float = (FloatLabelView) findViewById(R.id.login_password);

    }


    public void initListener(){
        forget_text.setOnClickListener(this);
        button_register.setOnClickListener(this);
        button_login.setOnClickListener(this);
    //    button_back.setOnClickListener(this);
    }


    public void initData() {
        password_float.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请稍等");
        dialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        dialog.setCancelable(false);
        sharedPreferencesUtil = new SharedPreferencesUtil(this);
    }



    public void check(){
        String phone = phone_float.getEditText().getText().toString().trim();
        String password = password_float.getEditText().getText().toString().trim();

        if (pubFun.isEmpty(phone) || pubFun.isEmpty(password)){
            dialog.dismiss();
            Toast.makeText(this,"账号或密码不能为空",Toast.LENGTH_LONG).show();
            return ;

        }
        if (!pubFun.isPhoneNumberValid(phone)){
            dialog.dismiss();
            Toast.makeText(this,"账号无效，请输入有效账号",Toast.LENGTH_LONG).show();
            return ;
        }


        if (checkName(phone,password)){
            dialog.dismiss();
            Toast.makeText(this,"登录中",Toast.LENGTH_LONG).show();
            sharedPreferencesUtil.putBooleanValue(SharedPreferencesUtil.login,true);
     //       PreferenceUtil.setBoolen(getApplication(),PreferenceUtil.SHOW_LOGIN,true);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            dialog.dismiss();
            Toast.makeText(this,"账号不存在",Toast.LENGTH_LONG).show();
        }
    }

    private Boolean checkName(String nameValue,String passwordValue) {
        ContentResolver provider = this.getContentResolver();
        Uri uri = Uri.parse("content://com.action.test/" + Dao.USER_TABLE);
        Cursor cursor = provider.query(uri,null,null,null,null);
        String phone = null;
        String password = null;
        int num = cursor.getCount();
      /*  if (cursor != null /*&& cursor.moveToFirst()* ){*/
           while (cursor.moveToNext()) {
               phone = cursor.getString(cursor.getColumnIndex(Dao.ACCOUNT));
                password = cursor.getString(cursor.getColumnIndex(Dao.PASSWORD));
                if (nameValue.equals(phone) && password.equals(passwordValue)) {
                    return true;
                }

        //  }
        }
        if (cursor != null){
            cursor.close();
        }
        return false;

    }




    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.forget_password:
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:
                Intent intent_register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent_register);
                break;
            case R.id.btn_login:
                dialog.show();
               check();
                break;
     //       case R.id.btn_back:
    //            break;
            default:
                break;
        }

    }

private long firstTime = 0 ;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000){
                    Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_LONG).show();
                    firstTime = secondTime;
                    return  true;
                }else{
                    System.exit(0);
                }
                break;
        }

        return super.onKeyUp(keyCode, event);
    }
}

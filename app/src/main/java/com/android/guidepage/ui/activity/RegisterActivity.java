package com.android.guidepage.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.guidepage.Dao.Dao;
import com.android.guidepage.R;

import net.anumbrella.customedittext.FloatLabelView;

import com.android.guidepage.base.BaseActivity;
import com.android.guidepage.util.pubFun;


/**
 * Created by Administrator on 2019/7/30 0030.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{


    private Button button_register;
    private FloatLabelView phone_float;
    private FloatLabelView name_float;
    private FloatLabelView password_float;
    private FloatLabelView password_again_float;
    private RadioButton service_text;

    private ProgressDialog dialog;
    private boolean isOk = false;



    @Override
    public int getResId() {
        return R.layout.activity_register;
    }



    public void initListener() {

        button_register.setOnClickListener(this);
        service_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    isOk = true;
                }else{
                    isOk = false ;
                }


            }
        });
    }

    public void initView() {
        name_float = (FloatLabelView) findViewById(R.id.register_username);
        password_again_float = (FloatLabelView) findViewById(R.id.register_password_again);
        password_float = (FloatLabelView) findViewById(R.id.register_password);
        phone_float = (FloatLabelView) findViewById(R.id.register_phone);
        button_register = (Button) findViewById(R.id.btn_regist);
        service_text = (RadioButton) findViewById(R.id.service_text);
    }


    public void initData() {

        password_float.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password_again_float.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog = new ProgressDialog(this);
        dialog.setMessage("请稍等");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regist:
                dialog.show();
                register();
                break;
        }
    }

    private void register() {
        String name = name_float.getEditText().getText().toString().trim();
        String passwor = password_again_float.getEditText().getText().toString().trim();
        String sure_password = password_float.getEditText().getText().toString().trim();
        String phone = phone_float.getEditText().getText().toString().trim();

        if (isOk){

            if(pubFun.isEmpty(phone) || pubFun.isEmpty(passwor) || pubFun.isEmpty(sure_password)){
                dialog.dismiss();
                Toast.makeText(this,"账号和密码不能为空",Toast.LENGTH_LONG).show();
                return ;
            }


            if (!pubFun.isPhoneNumberValid(phone)){
                dialog.dismiss();
                Toast.makeText(this,"账号无效，请输入有效账号",Toast.LENGTH_LONG).show();
                return ;
            }

            if (!pubFun.isEmpty(phone) && !pubFun.isEmpty(passwor) && !pubFun.isEmpty(sure_password) ){

                ContentResolver resolver = getContentResolver();
                Uri uri = Uri.parse("content://com.action.test/" + Dao.USER_TABLE );
                Cursor cursor = resolver.query(uri,null,null,null,null);
                while (cursor.moveToNext()){
                    String phone_value = cursor.getString(cursor.getColumnIndex(Dao.ACCOUNT));
                    if (phone.equals(phone_value)){
                        dialog.dismiss();
                        Toast.makeText(this,"账号已存在，请重新输入账号",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (cursor != null){
                    cursor.close();
                }
                if (passwor.equals(sure_password)){
                    dialog.dismiss();
                    ContentValues values  = new ContentValues();
                    values.put(Dao.NAME,name);
                    values.put(Dao.PASSWORD,passwor);
                    values.put(Dao.ACCOUNT,phone);
                    resolver.insert(uri,values);
                    Toast.makeText(this,"注册完成",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    dialog.dismiss();
                    Toast.makeText(this,"密码不一致，请输入正确的密码",Toast.LENGTH_LONG).show();
                }
            }

        }else{
            dialog.dismiss();
            Toast.makeText(this,"你必需同意协议",Toast.LENGTH_LONG).show();
        }





    }
}

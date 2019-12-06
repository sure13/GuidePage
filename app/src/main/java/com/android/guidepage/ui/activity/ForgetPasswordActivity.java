package com.android.guidepage.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import net.anumbrella.customedittext.FloatLabelView;

import com.android.guidepage.Dao.Dao;
import com.android.guidepage.R;
import com.android.guidepage.util.CodeUtils;
import com.android.guidepage.util.pubFun;

/**
 * Created by Administrator on 2019/7/30 0030.
 */

public class ForgetPasswordActivity extends Activity implements View.OnClickListener{


    private FloatLabelView phone_float;
    private FloatLabelView password_float;
    private FloatLabelView code_float;
    private ImageView image_code;
    private Button button_change;
    private CodeUtils codeUtils;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        initView();
        initListener();
        initData();
    }



    private void initView() {
        password_float = (FloatLabelView) findViewById(R.id.find_pass_phone);
        code_float = (FloatLabelView) findViewById(R.id.find_pass_code);
        password_float = (FloatLabelView) findViewById(R.id.find_pass_new);
        image_code = (ImageView) findViewById(R.id.imge_code);
        button_change = (Button) findViewById(R.id.find_pass_upadate_pass);
    }



    private void initListener() {
        button_change.setOnClickListener(this);
    }


    private void initData() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("请稍等");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);

        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        image_code.setImageBitmap(bitmap);
        image_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeUtils = CodeUtils.getInstance();
                Bitmap bitmap = codeUtils.createBitmap();
                image_code.setImageBitmap(bitmap);
                String code = codeUtils.getCode().toLowerCase();

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.find_pass_upadate_pass:;
                updatePassword();
                break;
        }
    }

    private void updatePassword() {

        dialog.show();
        String phone = phone_float.getEditText().getText().toString().trim();
        String password = password_float.getEditText().getText().toString().trim();
        String code = codeUtils.getCode().toLowerCase();
        if (pubFun.isEmpty(password) || pubFun.isEmpty(phone)){
            dialog.dismiss();
            Toast.makeText(this,"账号或密码不能为空",Toast.LENGTH_LONG).show();
            return;
        }

        if (pubFun.isPhoneNumberValid(phone)){
            dialog.dismiss();
            Toast.makeText(this,"账号无效，请输入有效账号",Toast.LENGTH_LONG).show();
            return ;
        }

        if (code == null || code.equals("") || !code.equals(code_float.getEditText().getText().toString().trim())){
            dialog.dismiss();
            Toast.makeText(this,"验证码不正确",Toast.LENGTH_LONG).show();
            return ;
        }

        if (check(phone,code)){
            dialog.dismiss();
            ContentResolver resolver = getApplication().getContentResolver();
            Uri uri = Uri.parse("content://com.action.test/" + Dao.USER_TABLE);
            ContentValues values = new ContentValues();
            values.put(Dao.ACCOUNT,phone);
            values.put(Dao.PASSWORD,password);
            resolver.update(uri,values,null,null);
            Toast.makeText(this,"密码修改成功",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }

    }

    public boolean check(String phoneValue,String codeValue) {
        ContentResolver resolver = getApplication().getContentResolver();
        Uri uri = Uri.parse("content://com.action.test/" + Dao.USER_TABLE);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            String phone = cursor.getString(cursor.getColumnIndex(Dao.ACCOUNT));
            if (phone.equals(phoneValue)) {
                if (codeValue.equals(code_float.getEditText().toString().trim())) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    return false;
    }


}

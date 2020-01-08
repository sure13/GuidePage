package com.android.guidepage.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.guidepage.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {


    private Button signOutButton;
    private Button editInfoButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.setting_activity);
        initView();
        initListener();
    }

    private void initView() {
        signOutButton = (Button) findViewById(R.id.sign_out);
        editInfoButton = (Button) findViewById(R.id.edit_info);
    }


    private void initListener() {
        signOutButton.setOnClickListener(this);
        editInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_out:
                Toast.makeText(SettingActivity.this," Sign Out",Toast.LENGTH_LONG).show();
                break;
            case R.id.edit_info:
                Toast.makeText(SettingActivity.this,"Edit Infomation",Toast.LENGTH_LONG).show();
                break;
        }
    }
}

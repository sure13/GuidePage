package com.android.guidepage.ui.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.guidepage.R;
import com.android.guidepage.base.BaseActivity;
import com.android.guidepage.util.SharedPreferencesUtil;


public class TheamActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout redLinearLayout;
    private LinearLayout greenLinearLayout;
    private LinearLayout blueLinearLayout;

    private ImageView redImageView;
    private ImageView greenImageView;
    private ImageView blueImageView;



    @Override
    public int getResId() {
        return R.layout.theam_activity;
    }


    public void initView() {
        redLinearLayout = (LinearLayout) findViewById(R.id.red_theam);
        greenLinearLayout = (LinearLayout) findViewById(R.id.green_theam);
        blueLinearLayout = (LinearLayout) findViewById(R.id.blue_theam);
        redImageView = (ImageView) findViewById(R.id.red_theam_image);
        blueImageView = (ImageView) findViewById(R.id.blue_theam_image);
        greenImageView = (ImageView) findViewById(R.id.green_theam_image);
    }

    public void initData() {
        redImageView.setVisibility(View.GONE);
        greenImageView.setVisibility(View.GONE);
        blueImageView.setVisibility(View.VISIBLE);
    }




    public void initListener() {
        redLinearLayout.setOnClickListener(this);
        greenLinearLayout.setOnClickListener(this);
        blueLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.red_theam:
                setTheamColor(redImageView);
             //   setTheme(R.style.AppThemeRed);
            //    recreate();

                break;
            case R.id.green_theam:
                setTheamColor(greenImageView);
            //    setTheme(R.style.AppThemeGreen);
            //    recreate();

                break;
            case R.id.blue_theam:
                setTheamColor(blueImageView);
            //    setTheme(R.style.AppTheme);
            //    recreate();

                break;
        }
    }


    private void setTheamColor(ImageView imageView){
        redImageView.setVisibility(View.GONE);
        greenImageView.setVisibility(View.GONE);
        blueImageView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        int theamValue = SharedPreferencesUtil.getIntValue("theam");
        switch (theamValue){
            case 1:
                setTheamColor(redImageView);
                break;
            case 2:
                setTheamColor(greenImageView);
                break;
            case 3:
                setTheamColor(blueImageView);
                break;
        }
    }

//    public void initTheam(){
//        int theamValue = SharedPreferencesUtil.getIntValue("theam");
//        switch (theamValue){
//            case 1:
//                setTheme(R.style.AppThemeRed);
//                Toast.makeText(this,"red theam",Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                setTheme(R.style.AppThemeGreen);
//                Toast.makeText(this,"green theam",Toast.LENGTH_SHORT).show();
//                break;
//            case 3:
//                setTheme(R.style.AppTheme);
//                Toast.makeText(this,"blue theam",Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }


}

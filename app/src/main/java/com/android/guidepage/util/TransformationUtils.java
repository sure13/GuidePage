package com.android.guidepage.util;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.Random;


/**
 * 设置图片等比缩放
 */
public class TransformationUtils  extends ImageViewTarget<Bitmap> {


    private ImageView target;
    private int random ;


    public TransformationUtils(ImageView target) {
        super(target);
        this.target = target;
    }

    @Override
    protected void setResource(Bitmap resource) {
        view.setImageBitmap(resource);
        //获取原图的宽高
        int height = resource.getHeight();
        int width = resource.getWidth();

        //获取imageView的宽
        int imageViewWidth = target.getWidth();

        //计算缩放比例
        float scale = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);

        //计算图片等比例放大后的高
        int imageViewHeight = (int) (height *scale);
        ViewGroup.LayoutParams params = target.getLayoutParams();
        params.height = imageViewHeight;
        target.setLayoutParams(params);
    }
}

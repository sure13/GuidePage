package com.android.guidepage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.guidepage.R;
import com.android.guidepage.rxjava.MeiZiBean;
import com.android.guidepage.util.TransformationUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.http.Url;


public class TwoFragmentAdapter  extends RecyclerView.Adapter<TwoFragmentAdapter.TwoFragmentViewHolder> {

    private View view;
    private Context context;
    private List<MeiZiBean.DataBean> dataBeanList = new ArrayList<>();


    public TwoFragmentAdapter(Context context){
        this.context = context;
    }


    public void setDataBeanList(List<MeiZiBean.DataBean> dataBeanList){
        this.dataBeanList = dataBeanList;
    }


    @NonNull
    @Override
    public TwoFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.two_item,parent,false);
        return new TwoFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TwoFragmentViewHolder holder, final int position) {
        holder.authorText.setText(dataBeanList.get(position).getAuthor());
        String time = dataBeanList.get(position).getCreatedAt();
        String[] list = time.split(" ");
        holder.timeText.setText(list[0]);
        holder.desText.setText(dataBeanList.get(position).getDesc());
        String url = dataBeanList.get(position).getUrl();
        final ImageView imageView = new ImageView(context);//你要加载图片的容器

     //   GlideUrl newUrl= new GlideUrl(url, new LazyHeaders.Builder() .addHeader("User-Agent", PhoneUtils.getUserAgent(context)).build());
        Glide.with(context)
                .load(url)
                .asBitmap()
                .fitCenter()
                .placeholder(R.mipmap.view_pager1)//加载成功前显示的图片
                .fallback(R.mipmap.view_pager1)//url为空的时候，显示的图片
                .error(R.mipmap.view_pager1)
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        //这个bitmap就是你图片url加载得到的结果
                        //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.photoImage.getLayoutParams();//获取你要填充图片的布局的layoutParam
                        layoutParams.height = (int) (((float) bitmap.getHeight()) / bitmap.getWidth() * getScreenWidth(context) / 4 );
                        //因为是2列,所以宽度是屏幕的一半,高度是根据bitmap的高/宽*屏幕宽的一半
                        layoutParams.width = getScreenWidth(context) / 4;//这个是布局的宽度
                        imageView.setLayoutParams(layoutParams);//容器的宽高设置好了
                        bitmap = zoomImg(bitmap, layoutParams.width, layoutParams.height);
                        // 然后在改变一下bitmap的宽高
                        holder.photoImage.setImageBitmap(bitmap);
                    }


                });


        holder.photoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickListener != null){
                    String url = dataBeanList.get(position).getUrl();
                    onclickListener.onItemClickListener(url,position);
                }
            }
        });


    }


    //改变bitmap尺寸的方法
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
    //获取屏幕宽度的方法
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }



    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public class TwoFragmentViewHolder extends RecyclerView.ViewHolder{

        private ImageView photoImage;
        private TextView authorText;
        private TextView timeText;
        private TextView desText;

        public TwoFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = (ImageView) itemView.findViewById(R.id.image_photo);
            authorText = (TextView) itemView.findViewById(R.id.text_author);
            timeText = (TextView) itemView.findViewById(R.id.text_time);
            desText = (TextView) itemView.findViewById(R.id.text_des);
        }
    }


    public interface setOnclickListener{
        void onItemClickListener(String url,int position);
    //    void onLongClickListener();
    }

    public void setOnclickListener(setOnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    private setOnclickListener onclickListener;



}

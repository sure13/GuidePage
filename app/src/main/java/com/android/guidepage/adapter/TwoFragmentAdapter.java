package com.android.guidepage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.guidepage.R;
import com.android.guidepage.rxjava.MeiZiBean;
import com.android.guidepage.util.TransformationUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TwoFragmentAdapter  extends RecyclerView.Adapter<TwoFragmentAdapter.TwoFragmentViewHolder> {

    private View view;
    private Context context;
    private List<MeiZiBean.DataBean> dataBeanList = new ArrayList<>();
//    private List<Integer> hegihtList = new ArrayList<>();
//    private List<Integer> widthList = new ArrayList<>();
//    private int random;

    public TwoFragmentAdapter(Context context,List<MeiZiBean.DataBean> dataBeanList){
        this.context = context;
        this.dataBeanList = dataBeanList;
        Log.i("wxy","----------dataBeanList-------::::"+dataBeanList);
//        if ((dataBeanList.size() > 0) && (dataBeanList != null)){
////            for (int i = 0 ; i<dataBeanList.size();i++){
////                random = new Random().nextInt(200)+100;//[100,300)
////                hegihtList.add(random);
////                widthList.add(250);
////            }
////        }

        if ((dataBeanList.size() > 0) && (dataBeanList != null)){
            for (int i = 0;i<dataBeanList.size();i++){
                dataBeanList.get(i).setState(false);
            }
        }
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
//        int height = hegihtList.get(position);
//        int width = widthList.get(position);
//        ViewGroup.LayoutParams layoutParams = holder.photoImage.getLayoutParams();
//        layoutParams.height = height;
//        layoutParams.width = width;

        holder.checkBox.setTag(position);//防止复用导致的checkbox显示错乱
        holder.authorText.setText(dataBeanList.get(position).getAuthor());
        String time = dataBeanList.get(position).getCreatedAt();
        String[] list = time.split(" ");
        holder.timeText.setText(list[0]);
        holder.desText.setText(dataBeanList.get(position).getDesc());
        Glide.with(context)
                .load(dataBeanList.get(position).getUrl())
                .asBitmap()
                .placeholder(R.mipmap.view_pager1)//加载成功前显示的图片
                .fallback(R.mipmap.view_pager1)//url为空的时候，显示的图片
                .error(R.mipmap.view_pager1)
                .into(new TransformationUtils(holder.photoImage));
        holder.photoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickListener != null){
                    onclickListener.onItemClickListener();
                }
            }
        });
        holder.photoImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onclickListener != null){
                    onclickListener.onLongClickListener();
                }
                return true;
            }
        });

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
        private CheckBox checkBox;

        public TwoFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = (ImageView) itemView.findViewById(R.id.image_photo);
            authorText = (TextView) itemView.findViewById(R.id.text_author);
            timeText = (TextView) itemView.findViewById(R.id.text_time);
            desText = (TextView) itemView.findViewById(R.id.text_des);
            checkBox = (CheckBox) itemView.findViewById(R.id.delte);
        }
    }


    public interface setOnclickListener{
        void onItemClickListener();
        void onLongClickListener();
    }

    public void setOnclickListener(setOnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    private setOnclickListener onclickListener;



}

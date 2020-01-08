package com.android.guidepage.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.guidepage.R;
import com.android.guidepage.bean.News;
import com.bumptech.glide.Glide;

import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter implements AdapterView.OnItemClickListener {

    private Context context;
    private List<News> newsList;
    private int flag = 0;

    public RecyclerViewAdapter(Context context,List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    public RecyclerViewAdapter(Context context,List<News> newsList,int flag){
        this.context = context;
        this.newsList = newsList;
        this.flag = flag;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        News news = newsList.get(position);
        if (flag == 1){
            ((NewsViewHolder)holder).title_text.setText(news.getTitle());
            ((NewsViewHolder)holder).author_text.setText(news.getAuthor());
            ((NewsViewHolder)holder).time_text.setText(news.getData());
            Glide.with(context).load(news.getImage()).into(((NewsViewHolder) holder).imageView);
        }else{
            String html = "";
            String href = news.getUrl();
            String title = news.getTitle();
            html +="<a href='" + href + "'>" + title + "</a>";
            ((NewsViewHolder)holder).title_text.setMovementMethod(LinkMovementMethod.getInstance());
            ((NewsViewHolder)holder).title_text.setText(Html.fromHtml(html));
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        public NewsViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            title_text = (TextView) itemView.findViewById(R.id.item_text);
            author_text = (TextView) itemView.findViewById(R.id.author);
            time_text = (TextView) itemView.findViewById(R.id.time);
        }

        ImageView imageView;
        TextView title_text;
        TextView author_text;
        TextView time_text;
    }

    public interface  OnItemClickListener{
        void onClick(int position);
    }

}

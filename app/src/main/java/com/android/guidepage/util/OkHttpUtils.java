package com.android.guidepage.util;


import com.android.guidepage.bean.News;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {

    public static String getArticleData(String uri){
        String html = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(uri).build();
        try{
            Response response = client.newCall(request).execute();
            html = response.body().string();
        }catch (IOException e){
            e.printStackTrace();
        }
        return html;
    }

    public static ArrayList<News> getNewsList(String url) {

        ArrayList<News> newsList = new ArrayList<>();
        ArrayList<Element> elementList = new ArrayList<>();
        Connection connection = Jsoup.connect(url);
        try {
            Document document = connection.get();
            Elements elements = document.getElementsByTag("a");
            for(int i = 0 ; i < elements.size(); i++) {
                Element element = elements.get(i);
                String href = element.attr("href");//获取链接的url值
                String text = element.text();//获取链接的标题
                if (text != null && !text.equals("") && text.length() > 10 && href.startsWith("http")){
                    elementList.add(element);
                    if (text.contains("京公网安备")|| text.contains("京ICP证") || text.contains("许可证")||text.contains("资格证书") ){
                        elementList.remove(element);
                    }
                }
            }

            for (int j = 0 ; j <elementList.size(); j ++){
                String href = elementList.get(j).attr("href");
                String title = elementList.get(j).text();
                News news = new News();
                news.setTitle(title);
                news.setUrl(href);
                newsList.add(news);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return newsList;
    }


    public static void sendOkhttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client =new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);//enqueue方法在内部开好了子线程并最终将结果回调到okhttp3.Callback当中。


}

}


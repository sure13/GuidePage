package com.android.guidepage.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.guidepage.R;
import com.android.guidepage.adapter.RecyclerViewAdapter;
import com.android.guidepage.bean.News;
import com.android.guidepage.util.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AmusementFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private static Context context;
    private static  AmusementFragment amusementFragment;
    private List<News> newsList;
    String url ="http://ic.snssdk.com/2/article/v25/stream/?count=20&min_behot_time=1504621638&bd_latitude=4.9E-324&bd_longitude=4.9E-324&bd_loc_time=1504622133&loc_mode=5&loc_time=1504564532&latitude=35.00125&longitude=113.56358166666665&city=%E7%84%A6%E4%BD%9C&lac=34197&cid=23201&iid=14534335953&device_id=38818211465&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SM-E7000&os_api=19&os_version=4.4.2&uuid=357698010742401&openudid=74f06d2f9d8c9664";



    public static AmusementFragment getInstance(final Context mContext){
        context = mContext;
        if (amusementFragment == null){
            amusementFragment = new AmusementFragment();
        }
        return amusementFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_layout_fragment,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }



    private void initData() {
        newsList = new ArrayList<News>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(context,newsList,1);
        recyclerView.setAdapter(adapter);
        getNews();
        adapter.notifyDataSetChanged();

    }



    private void getNews() {
        OkHttpUtils.sendOkhttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("wj","failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wj","sucessful");
                parseJsonWithJsonObject(response);
            }
        });
    }



    private void parseJsonWithJsonObject(Response response) throws IOException {
        String responseData = response.body().string();
        Log.i("wj","responseData ==" +responseData);
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            Log.i("wj","jsonArray ==" +jsonArray);
            for (int i = 0 ; i < jsonArray.length(); i ++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                News news = new News();
                String title = jsonObject.getString("title");
                String time = jsonObject.getString("ptime");
                String image = jsonObject.getString("url");
                String author = jsonObject.getString("source");
                String url = jsonObject.getString("url");
                news.setAuthor(author);
                news.setData(time);
                news.setImage(image);
                news.setTitle(title);
                news.setUrl(url);
                Log.i("wj","title == " + title);
                Log.i("wj","time == " + time);
                Log.i("wj","image == " + image);
                Log.i("wj","author == " + author);
                newsList.add(news);
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.i("wj","JSONException e" );
        }


    }


    @Override
    public void onStart() {
        super.onStart();
    }



    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }
}

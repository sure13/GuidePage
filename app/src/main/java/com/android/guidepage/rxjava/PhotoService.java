package com.android.guidepage.rxjava;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface PhotoService {

    @GET("v2/data/category/Girl/type/Girl/page/{page}/count/{number}")
    Observable<MeiZiBean> getJsonList(@Path("page") int page , @Path("number") int number);

}

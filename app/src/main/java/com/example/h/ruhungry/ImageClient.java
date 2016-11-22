package com.example.h.ruhungry;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hemanth on 11/21/16.
 */

public interface ImageClient {
   @GET("/images/{url}")
    Call<ResponseBody>concepts(
        @Path("url")String url
   );
}

package com.example.h.ruhungry;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by h on 11/16/2016.
 */

public class ServiceGenerator {

        public static final String API_BASE_URL = "https://rumobile.rutgers.edu/";
        //public static final String FOOD_SERVER_URL="172.27.46.15/3000";
        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        private static Retrofit.Builder menuBuilder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        /* private static Retrofit.Builder conceptBuilder =
            new Retrofit.Builder()
                    .baseUrl(FOOD_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create());*/

        public static <S> S createMenuService(Class<S> serviceClass) {
            Retrofit retrofit = menuBuilder.client(httpClient.build()).build();
            return retrofit.create(serviceClass);
        }

   /* public static <S> S createImageService(Class<S> serviceClass) {
        Retrofit retrofit = conceptBuilder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }*/


}

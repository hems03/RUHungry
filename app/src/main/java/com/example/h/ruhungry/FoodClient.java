package com.example.h.ruhungry;

import android.content.Intent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by h on 11/16/2016.
 */

public interface FoodClient  {
    @GET("/1/rutgers-dining.txt")
    Call<List<Menu>> foodMenu(

            );
}

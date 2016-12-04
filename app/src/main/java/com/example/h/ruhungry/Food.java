package com.example.h.ruhungry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hemanth on 12/2/16.
 */

public class Food implements Parcelable {
    private String mFoodName;
    private String mCalories;
    private String mFats;
    private String mCarbs;
    public Food(Parcel in){
        String data[]=new String[4];
        in.readStringArray(data);
        mFoodName=data[0];
        mCalories=data[1];
        mFats=data[2];
        mCarbs=data[3];

    }

    public Food(String name, String  calories, String fats, String carbs){
        mFoodName=name;
        mCalories=calories;
        mFats=fats;
        mCarbs=carbs;
    }

    public void setFoodName(String foodName) {
        mFoodName = foodName;
    }

    public void setCalories(String calories) {
        mCalories = calories;
    }

    public void setFats(String fats) {
        mFats = fats;
    }

    public void setCarbs(String carbs) {
        mCarbs = carbs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFoodName() {
        return mFoodName;
    }

    public String getCalories() {
        return mCalories;
    }

    public String getFats() {
        return mFats;
    }

    public String getCarbs() {
        return mCarbs;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                mFoodName,
                mCalories,
                mFats,
                mCarbs
        });


    }



    public static final Parcelable.Creator CREATOR= new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Food(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Food[i];
        }
    };
}

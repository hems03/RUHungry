package com.example.h.ruhungry;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by h on 11/19/2016.
 */

public class Plate {
    private UUID mUUID;
    private String mPlateURL;
    private String mDate;
    private ArrayList<String> mConcepts;

    public Plate(String plateURL, UUID id, String date){
        mPlateURL=plateURL;
        mUUID=id;
        mDate=date;
        mConcepts=new ArrayList<>();

    }
    /*public String getImgPath(){
        return "IMG_"+getID().toString()+".jpg";
    }*/

    public UUID getUUID() {
        return mUUID;
    }

    public String getPlateURL() {
        return mPlateURL;
    }

    public UUID getID(){
        return mUUID;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public ArrayList<String> getmConcepts() {
        return mConcepts;
    }

    public void setmConcepts(ArrayList<String> mConcepts) {
        this.mConcepts = mConcepts;
    }
}

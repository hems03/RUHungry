package com.example.h.ruhungry;

import android.graphics.Bitmap;

import java.util.UUID;

/**
 * Created by h on 11/19/2016.
 */

public class Plate {
    private String mImgPath;
    private Bitmap mBitmap;
    private UUID mUUID;
    public Plate(Bitmap bitmap){
        mBitmap=bitmap;
        mUUID=UUID.randomUUID();

    }
    public String getImgPath(){
        return "IMG_"+getID().toString()+".jpg";
    }
    public Bitmap getBitmap(){
        return mBitmap;
    }
    public UUID getID(){
        return mUUID;
    }
}

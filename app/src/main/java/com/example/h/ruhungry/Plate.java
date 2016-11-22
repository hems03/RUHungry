package com.example.h.ruhungry;

import android.graphics.Bitmap;

import java.util.UUID;

/**
 * Created by h on 11/19/2016.
 */

public class Plate {
    private UUID mUUID;
    private String mBase64Img;
    private Bitmap mBitmap;
    public Plate(Bitmap bitmap, UUID id){
        mBitmap=bitmap;
        mUUID=id;
    }
    /*public String getImgPath(){
        return "IMG_"+getID().toString()+".jpg";
    }*/

    public UUID getUUID() {
        return mUUID;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public UUID getID(){
        return mUUID;
    }
}

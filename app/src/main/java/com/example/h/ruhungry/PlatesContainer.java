package com.example.h.ruhungry;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by h on 11/19/2016.
 */

public class PlatesContainer {
    Context mContext;
    private ArrayList<Plate>mPlates;
    private static PlatesContainer sPlatesContainer;
    public PlatesContainer(Context c){
        mContext=c.getApplicationContext();
        mPlates=new ArrayList<>();
    }
    public static PlatesContainer getPlatesContainer(Context context){
        if(sPlatesContainer==null){
            sPlatesContainer=new PlatesContainer(context);
        }
        return sPlatesContainer;
    }
    public File getPhotoFile(Plate m){
        File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null){
            return null;
        }
        return new File(externalFilesDir, m.getImgPath());
    }
    public void addPlate(Plate m){
        mPlates.add(m);
    }
    public ArrayList<Plate>getPlates(){
        return mPlates;
    }

}

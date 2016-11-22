package com.example.h.ruhungry;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by hemanth on 11/21/16.
 */

public class Constants {
    public static final int GEOFENCE_EXPIRATION_IN_MILLISECONDS=300000;
    public static final int GEOFENCE_RADIUS_IN_METERS=300;
    public static final int LOITERING_DELAY=1000; //NEED TO CHANGE

    public static final HashMap<String, LatLng> RU_DINING_HALLS = new HashMap<String, LatLng>();
    static {

        RU_DINING_HALLS.put("LIVINGSTON_DINING_HALL", new LatLng(40.522815, -74.438234));


        RU_DINING_HALLS.put("BUSCH_DINING_HALL", new LatLng(40.522684, -74.457693));


        RU_DINING_HALLS.put("BROWER COMMONS", new LatLng(40.503652, -74.451679));
    }
}

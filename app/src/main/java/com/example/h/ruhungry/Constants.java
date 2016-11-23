package com.example.h.ruhungry;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by hemanth on 11/21/16.
 */

public class Constants {
    public static final int GEOFENCE_EXPIRATION_IN_MILLISECONDS=300;
    public static final int GEOFENCE_RADIUS_IN_METERS=100;
    public static final int LOITERING_DELAY=1000*60*10; //NEED TO CHANGE

    //SharedPreferences Stuff
    public static final String PREF_NAME="my_shared_preferences";
    public static final String PREF_LOGIN_STATUS="login_status";

    public static final HashMap<String, LatLng> RU_DINING_HALLS = new HashMap<String, LatLng>();
    static {

        RU_DINING_HALLS.put("Livingston Dining Hall", new LatLng(40.502905, -74.447743)); //need to change


        RU_DINING_HALLS.put("Busch Dining Hall", new LatLng(40.522684, -74.457693));


        RU_DINING_HALLS.put("Brower Commons", new LatLng(40.503652, -74.451679));
    }
}

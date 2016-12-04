package com.example.h.ruhungry;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by hemanth on 11/21/16.
 */

public class Constants {
    public static final String LOGIN_KEY="login_key";
    public static final String LAST_ENTRY_KEY="last_entry";
    public static final String GEOFENCE_TOGGLE_KEY="geofence_toggle";
    public static final String PREF_CAMERA="preference_camera";

    public static final int GEOFENCE_EXPIRATION_IN_MILLISECONDS=300;
    public static final int GEOFENCE_RADIUS_IN_METERS=150;
    public static final int LOITERING_DELAY=1000*60/2; //NEED TO CHANGE

    //SharedPreferences Stuff
    public static final String PREF_NAME="my_shared_preferences";
    public static final String PREF_LOGIN_STATUS="login_status";

    public static final HashMap<String, LatLng> RU_DINING_HALLS = new HashMap<String, LatLng>();
    static {

        RU_DINING_HALLS.put("Livingston Dining Hall", new LatLng(40.522830, -74.438115)); //need to change


        RU_DINING_HALLS.put("Busch Dining Hall", new LatLng(40.522684, -74.457693));


        RU_DINING_HALLS.put("Brower Commons", new LatLng(40.503652, -74.451679));
        RU_DINING_HALLS.put("Your dorm", new LatLng(40.502823, -74.447713));

        RU_DINING_HALLS.put("Your House", new LatLng(40.548891, -74.414121));
        RU_DINING_HALLS.put("Busch Campus Center",new LatLng(40.5231995,-74.4587013));
    }
}

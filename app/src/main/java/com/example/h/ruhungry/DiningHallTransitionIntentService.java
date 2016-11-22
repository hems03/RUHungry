package com.example.h.ruhungry;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by h on 11/18/2016.
 */

public class DiningHallTransitionIntentService extends IntentService {
    private static final String TAG="DiningService";
    public DiningHallTransitionIntentService(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"Geo Intent Fired");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Something went wrong!");
            return;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Log.i(TAG,triggeringGeofences.toString());


        } else {

        }
    }
}

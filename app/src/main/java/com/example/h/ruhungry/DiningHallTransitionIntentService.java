package com.example.h.ruhungry;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_food)
                            .setContentTitle("You eatin?")
                            .setContentText("Log what your eating today.")
                            ;

            Intent resultIntent= new Intent(this,SignInActivity.class);




        } else {

        }
    }


}

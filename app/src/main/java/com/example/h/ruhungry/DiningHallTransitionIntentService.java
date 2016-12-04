package com.example.h.ruhungry;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by h on 11/18/2016.
 */

public class DiningHallTransitionIntentService extends BroadcastReceiver {
    private static final String TAG = "DiningService";

    public DiningHallTransitionIntentService() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Geo Intent Fired");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, ""+geofencingEvent.getErrorCode());
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(Constants.PREF_CAMERA,true);
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Log.i(TAG, triggeringGeofences.toString());



            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_food)
                            .setContentTitle("Are you at " + triggeringGeofences.get(0).getRequestId())
                            .setContentText("Log what you're eating")
                            .setAutoCancel(true);
            Intent resultIntent = new Intent(context, SignInActivity.class);
            resultIntent.putExtra(Constants.PREF_CAMERA,true);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(SignInActivity.class);
            taskStackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    taskStackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}

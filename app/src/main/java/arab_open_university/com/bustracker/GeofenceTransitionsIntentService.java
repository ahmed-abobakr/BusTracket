package arab_open_university.com.bustracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmedabobakr on 4/26/17.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(event.hasError()){
            Log.d("TEST", "Gefence Service Error: " + event.getErrorCode() );
            return;
        }

        int geoFenceTransition = event.getGeofenceTransition();
        if(geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            List<Geofence> triggeringGeofences = event.getTriggeringGeofences();
            // Create a detail message with Geofences received

            if(BusTrackerApp.listener != null){
                BusTrackerApp.listener.onGeoLocationEntered(Integer.parseInt(triggeringGeofences.get(0).getRequestId()));
            }
        }else if(geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d("TEST", "Exit");
            List<Geofence> triggeringGeofences = event.getTriggeringGeofences();
            // Create a detail message with Geofences received


            if(BusTrackerApp.listener != null){
                BusTrackerApp.listener.onGeoLocationExited(Integer.parseInt(triggeringGeofences.get(0).getRequestId()));
            }
        }else
            Log.d("TEST", "ERROR ERROR");
    }


}

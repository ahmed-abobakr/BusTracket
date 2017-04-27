package arab_open_university.com.bustracker;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by ahmedabobakr on 4/26/17.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(event.hasError()){
            Log.d("TEST", "Gefence Service Error: " + event.getErrorCode() );
            return;
        }
        int geofenceTransition = event.getGeofenceTransition();
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            Log.d("TEST", "Entered");
        else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            Log.d("TEST", "Exit");
        else
            Log.d("TEST", "ERROR ERROR");
    }
}

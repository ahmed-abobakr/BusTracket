package arab_open_university.com.bustracker;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by akhalaf on 4/29/2017.
 */

public class BusTrackerApp extends Application {

    public static GeofenceListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }


}

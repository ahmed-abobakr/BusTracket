package arab_open_university.com.bustracker;

/**
 * Created by akhalaf on 5/1/2017.
 */

public interface GeofenceListener {

   void onGeoLocationEntered(int index);

   void onGeoLocationExited(int index);
}

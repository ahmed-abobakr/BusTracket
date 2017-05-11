package arab_open_university.com.bustracker;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, LocationListener,
                GeofenceListener, StartTipDialog.OnStartedStationChoosen{

    GoogleMap mMap;
    GoogleApiClient googleApiClient;

    boolean isMapReady = false, isTripStarted = false;

    private final LatLng AkherElShare3 = new LatLng(29.959203, 31.106928);
    private final LatLng ElGame3 = new LatLng(29.960346, 31.107336);
    private final LatLng ElGate2 = new LatLng(29.964213, 31.108248);
    private final LatLng ElGate1 = new LatLng(29.977374, 31.112282);
    private final LatLng elRemaya = new LatLng(29.988330, 31.129931);
    private final LatLng mashaalLatLng = new LatLng(29.986782, 31.141370);
    private final LatLng mariotiaLatLng = new LatLng(29.989988, 31.150007);
    private final LatLng areishLatLng = new LatLng(29.993816, 31.160403);
    private final LatLng carioMallLatLng = new LatLng(29.998824, 31.173492);
    private final LatLng talbiaLatLng = new LatLng(30.001732, 31.181195);
    private final LatLng gizaSquareLatLng = new LatLng(30.015417, 31.212062);
    private final LatLng startLocation = new LatLng(29.959614, 31.103809);

    private final LatLng beforeMashaalLocation = new LatLng(29.984625, 31.136037);
    private final LatLng afterMashaalLocation = new LatLng(29.987394, 31.143183);
    private final LatLng beforeMariotiaaLocation = new LatLng(29.989029, 31.147625);
    private final LatLng afterMariotiaaLocation = new LatLng(29.990711, 31.151906);
    private final LatLng beforeArieshLocation = new LatLng(29.992895, 31.157753);
    private final LatLng afterArieshLocation = new LatLng(29.994577, 31.162366);
    private final LatLng beforeCairoMallLocation = new LatLng(29.998340, 31.172312);
    private final LatLng afterCairoMallLocation = new LatLng(29.999743, 31.175831);
    private final LatLng beforeTalbiaaLocation = new LatLng(30.001285, 31.179962);
    private final LatLng afterTalbiaaLocation = new LatLng(30.003273, 31.185412);
    private final LatLng beforeGizaSquareLocation = new LatLng(30.011700, 31.207052);
    private final LatLng myLocation = new LatLng(30.066119, 31.258727);
    private List<Geofence> busStationsList;
    private PendingIntent mGeofencePendingIntent;
    private List<LatLng> geofenceList;
    private int geofenceListIndex = 0, myLocationChangeListIndex = 0, counter = 0;
    private List<LatLng> myLocationChangedList;
    private Location mLastLocation;
    private FirebaseDatabase database;
    private DatabaseReference busLocationDatabaseRefernce;
    private BusStation currentBusStations;

    //views
    private TextView txtStations, txtLblStation, txtStationName;
    private Button btnTripStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inflate the layout views
        txtStations = (TextView) findViewById(R.id.map_stations);
        txtLblStation = (TextView) findViewById(R.id.map_lbl_next_station);
        txtStationName = (TextView) findViewById(R.id.map_next_station);
        btnTripStatus = (Button) findViewById(R.id.btn_start_trip);

        /* shows map fragment in the activity and asunc download the maps on the activity */
        final MapFragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction().replace(R.id.map_view, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        BusTrackerApp.listener = this;
        BusTrackerApp.onStartedStationChoosen = this;

        busStationsList = new ArrayList<>();
        geofenceList = new ArrayList<>();

        //Intialize firebase database
        database = FirebaseDatabase.getInstance();


        /* shows dialog to determine the start trip  */
        btnTripStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnTripStatus.getText().toString().equals(getString(R.string.start_trip))) {
                    btnTripStatus.setText(getString(R.string.end_trip));
                    if (currentBusStations != null && currentBusStations.getBusStationsNames() != null && currentBusStations.getBusStationsNames().size() > 1) {
                        Bundle args = new Bundle();
                        args.putStringArrayList("stations_list", ((ArrayList<String>) currentBusStations.getBusStationsNames()));
                        StartTipDialog dialog = new StartTipDialog();
                        dialog.setArguments(args);
                        dialog.show(getFragmentManager(), "start stations");
                    }
                }else {
                    btnTripStatus.setText(getString(R.string.start_trip));
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate Menu layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //when ues click logout from menu
            case R.id.logout:
                MainActivity.this.finish();
                return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //CallBack after user accept or reject permissions worked for android 6.0 and upper only
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 101: {
                /* check if user accepts Location permission  if yes set my location enabled true and request
                 * location from google services */
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    //getLastKnownLocation();
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    googleApiClient = new GoogleApiClient.Builder(this)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(LocationServices.API)
                            .enableAutoManage(this, 0, this)
                            .build();

                    googleApiClient.connect();
                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    private void permissionsDenied() {
        Log.w("TEST", "permissionsDenied()");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        /* called after connected to google location services and request  location updates every 6.0 seconds
         * and get last konown location and show bus marker and zoom to bus location
          * and check if user enable location or not if not enabled request from user to enable location
           * and add bus stations to geofence */
        if(isPermissionsGranted(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
            showMarkersAndZoom();




            startLocationUpdates();
            if(geofenceList.size() > 0) {
                LocationServices.GeofencingApi.addGeofences(
                        googleApiClient,
                        busStationsList,
                        getGeofencePendingIntent()
                ).setResultCallback(this);
            }

        }else {
            requestGrantedPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /* called after downloading map  and drawing it on the activity
         * check if user accept location permission on android 6.0 and heigher
          * if no request permission
          * if yes allow my location on map and show button my location on google map and connect to google location services  */
        isMapReady = true;
        mMap = googleMap;
        if(isPermissionsGranted(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, 0, this)
                    .build();

            googleApiClient.connect();
        }else {
            requestGrantedPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    private void  setBusStationsList(){
        /* add bus stations list to geofence by determining the diameter of the station  */
        for(LatLng latLng : geofenceList) {
            String id = UUID.randomUUID().toString();
            busStationsList.add(new Geofence.Builder()
                    .setRequestId(String.valueOf(geofenceList.indexOf(latLng)))
                    .setCircularRegion(latLng.latitude, latLng.longitude,
                            200.0f)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }


    }



    private PendingIntent getGeofencePendingIntent(){
        if(mGeofencePendingIntent != null){
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.d("TEST", "OnResult");
        if(status.isSuccess() ){
            Log.d("TEST", "onResultSuccess");
            }
        }


    private void  showMarkersAndZoom(){
        /* the method is called to show bus location marker and zoom to bus location
        * and adding bus stations markers*/
        mMap.clear();
        if(mLastLocation != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarker)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16), 200, null);
        }
        for(int i = 0; i < geofenceList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(geofenceList.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop24)));
        }

    }

    private void startLocationUpdates(){
        /*  called to start request location  updateds every 30 seconds and
          check if user enable location
        * send the location to the firebase database*/
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(3000)
                .setInterval(3000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(mLastLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16), 200, null);
        }
        DatabaseReference busStationsReference = database.getReference("buses_Info");

        busStationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                outerloop:
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    BusStation busStation = snapshot.getValue(BusStation.class);
                    for(String id : busStation.getBusesIDs()){
                        if(id.equalsIgnoreCase(user.getUid())) {
                          btnTripStatus.setVisibility(View.VISIBLE);
                            currentBusStations = busStation;

                            break outerloop;
                        }
                    }
                }

                Log.d("TEST", "BusNUMOfStations: " + currentBusStations.getNumOfBusStations());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TEST", "Read Canceled");
            }
        });
        LocationSettingsRequest.Builder mBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        mBuilder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, mBuilder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        if (mLastLocation != null)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16), 200, null);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    public  boolean isPermissionsGranted(Context context, String[] grantPermissions) {
        /* this method is called to check if user grant specific permissions for android 6.0 and heigher  */
        boolean accessGranted = true;
        if (grantPermissions == null || grantPermissions.length == 0) {
            accessGranted = false;
        } else {
            for (String permission : grantPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    accessGranted = false;
                    break;
                }
            }
        }
        return accessGranted;
    }

    public  boolean requestGrantedPermissions(Context context, String[] permissions, int requestCode) {
        /* the method request permission on android 6.0 and heigher */
        boolean requestPermission = true;
        if (!isPermissionsGranted(context, permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(permissions, requestCode);
            } else {
                requestPermission = false;
            }
        } else {
            requestPermission = false;
        }
        return requestPermission;
    }


    @Override
    public void onLocationChanged(final Location location) {
        /* called after location changed and send the changed location to the firebase database */
        mLastLocation = location;
        if(location != null){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if(user != null && user.getUid() != null) {
                busLocationDatabaseRefernce = database.getReference("bus_location");
                GeoFire geoFire = new GeoFire(busLocationDatabaseRefernce);
                geoFire.setLocation(user.getUid(), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            Log.d("TEST", "Insert location Error " + error.getCode() + ", message: " + error.getMessage() + " , details: " + error.getDetails());
                        } else {
                            Log.d("TEST", "Inserted Succefully ");
                        }
                    }
                });
            }
        }

        showMarkersAndZoom();
        myLocationChangeListIndex++;
        if(counter == 2){

            counter = 0;
        }else {
            counter++;
        }

    }


    public  static Intent makeNotificationIntent(Context geofenceService, String msg)
    {
        Log.d("Test",msg);
        return new Intent(geofenceService,MainActivity.class);
    }

    @Override
    public void onGeoLocationEntered(final int index) {
        /* called when the bus enter the station */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLblStation.setText("Current Station: ");
                txtStationName.setText(currentBusStations.getBusStationsNames().get(index));
            }
        });
    }

    @Override
    public void onGeoLocationExited(int index) {
        /* called when the bus exit the station */
        index++;
        final  int indexTemp = index;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //index++;
                Toast.makeText(MainActivity.this, "indexTemp = " + indexTemp, Toast.LENGTH_LONG).show();
                if(currentBusStations.getBusStationsNames().size() < indexTemp){
                    txtLblStation.setText("NextStation: ");
                    txtStationName.setText(currentBusStations.getBusStationsNames().get(0));
                }else {
                    txtLblStation.setText("Next Station: ");
                    txtStationName.setText(currentBusStations.getBusStationsNames().get(indexTemp));
                }

            }
        });
    }

    @Override
    public void onStartedStationChoosen(boolean reverseList) {
        /* called when the driver choose the started station  */
        if(reverseList){
            Collections.reverse(currentBusStations.getBusStationsLat());
            Collections.reverse(currentBusStations.getBusStationsLong());
            Collections.reverse(currentBusStations.getBusStationsNames());
        }
        String stationsNames = "";
        for(String busName : currentBusStations.getBusStationsNames()){
            stationsNames += busName + " - ";
        }
        for(int i = 0; i < currentBusStations.getBusStationsLat().size(); i++){
            geofenceList.add(new LatLng(currentBusStations.getBusStationsLat().get(i), currentBusStations.getBusStationsLong().get(i)));
        }
        txtStations.setText(stationsNames);
        showMarkersAndZoom();
        setBusStationsList();
        LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                busStationsList,
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }


}

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
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, LocationListener,
                GeofenceListener{

    GoogleMap mMap;
    GoogleApiClient googleApiClient;

    boolean isMapReady = false;

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
    private TextView txtStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStations = (TextView) findViewById(R.id.map_stations);

        final MapFragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction().replace(R.id.map_view, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        BusTrackerApp.listener = this;

        busStationsList = new ArrayList<>();
        geofenceList = new ArrayList<>();
        /*geofenceList.add(startLocation);
        geofenceList.add(AkherElShare3);
        geofenceList.add(ElGame3);
        geofenceList.add(ElGate2);
        geofenceList.add(ElGate1);
        geofenceList.add(elRemaya);
        geofenceList.add(mashaalLatLng);
        geofenceList.add(mariotiaLatLng);
        geofenceList.add(areishLatLng);
        geofenceList.add(carioMallLatLng);
        geofenceList.add(talbiaLatLng);
        geofenceList.add(gizaSquareLatLng);*/

        myLocationChangedList = new ArrayList<>();
        myLocationChangedList.add(startLocation);
        myLocationChangedList.add(beforeMashaalLocation);
        myLocationChangedList.add(mashaalLatLng);
        myLocationChangedList.add(afterMashaalLocation);
        myLocationChangedList.add(beforeMariotiaaLocation);
        myLocationChangedList.add(mariotiaLatLng);
        myLocationChangedList.add(afterMariotiaaLocation);
        myLocationChangedList.add(beforeArieshLocation);
        myLocationChangedList.add(areishLatLng);
        myLocationChangedList.add(afterArieshLocation);
        myLocationChangedList.add(beforeCairoMallLocation);
        myLocationChangedList.add(carioMallLatLng);
        myLocationChangedList.add(afterCairoMallLocation);
        myLocationChangedList.add(beforeTalbiaaLocation);
        myLocationChangedList.add(talbiaLatLng);
        myLocationChangedList.add(afterTalbiaaLocation);
        myLocationChangedList.add(beforeGizaSquareLocation);
        myLocationChangedList.add(gizaSquareLatLng);

        database = FirebaseDatabase.getInstance();
        DatabaseReference busStationsReference = database.getReference("buses_Info");

            busStationsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    String stationsNames = "";
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        BusStation busStation = snapshot.getValue(BusStation.class);
                        for(String id : busStation.getBusesIDs()){
                            if(id.equalsIgnoreCase(user.getUid())) {
                                currentBusStations = busStation;
                                for(String busName : busStation.getBusStationsNames()){
                                    stationsNames += busName + " - ";
                                }
                                for(int i = 0; i < busStation.getBusStationsLat().size(); i++){
                                    geofenceList.add(new LatLng(busStation.getBusStationsLat().get(i), busStation.getBusStationsLong().get(i)));
                                }
                                continue;
                            }
                        }
                    }
                    txtStations.setText(stationsNames);
                    Log.d("TEST", "BusNUMOfStations: " + currentBusStations.getNumOfBusStations());
                    showMarkersAndZoom();
                    setBusStationsList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TEST", "Read Canceled");
                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("TEST", "request permission Result");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case 101: {
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
        Log.d("TEST", "OnConnected");
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocationChangedList.get(myLocationChangeListIndex), 16), 200, null);


        if(isPermissionsGranted(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
            /*if(LocationServices.FusedLocationApi.getLastLocation(googleApiClient) != null)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocationServices.FusedLocationApi.getLastLocation(googleApiClient).getLatitude(), LocationServices.FusedLocationApi.getLastLocation(googleApiClient).getLongitude()), 16), 200, null);*/

            showMarkersAndZoom();




            startLocationUpdates();
            if(geofenceList.size() > 0) {
                LocationServices.GeofencingApi.addGeofences(
                        googleApiClient,
                        busStationsList,
                        getGeofencePendingIntent()
                ).setResultCallback(this);
            }

           // startCountDownTimer();
            /*    Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onLocationChanged(new Location(LocationManager.GPS_PROVIDER));
                    }
                }, 3000);*/
            /*Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                    });
                }
            },0, 3000);*/
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
        Log.d("TEST", "SETBusStationsList");
        for(LatLng latLng : geofenceList) {
            String id = UUID.randomUUID().toString();
            busStationsList.add(new Geofence.Builder()
                    .setRequestId(String.valueOf(geofenceList.indexOf(latLng)))
                    .setCircularRegion(latLng.latitude, latLng.longitude,
                            60.0f)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
        if(googleApiClient.isConnected()) {
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    busStationsList,
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        }
        //StartLocation
        /*busStationsList.add(new Geofence.Builder()
                .setRequestId("1")
                .setCircularRegion(startLocation.latitude,
                        startLocation.longitude,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Akher-Elshare3
        busStationsList.add(new Geofence.Builder()
                .setRequestId("2")
                .setCircularRegion(AkherElShare3.latitude,
                        AkherElShare3.longitude,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //El-Game3
        busStationsList.add(new Geofence.Builder()
                .setRequestId("3")
                .setCircularRegion(ElGame3.latitude,
                        ElGame3.longitude,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //El-Gate2
        busStationsList.add(new Geofence.Builder()
                .setRequestId("4")
                .setCircularRegion(ElGate2.latitude,
                        ElGate2.longitude,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //El-Gate1
        busStationsList.add(new Geofence.Builder()
                .setRequestId("5")
                .setCircularRegion(ElGate1.latitude,
                        ElGate1.longitude,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //El-Remaya
        busStationsList.add(new Geofence.Builder()
                .setRequestId("6")
                .setCircularRegion(elRemaya.latitude,
                        elRemaya.longitude,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());


        //Mashaal
        busStationsList.add(new Geofence.Builder()
                .setRequestId("7")
                .setCircularRegion(29.986782,
                        31.141370,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Mariotia
        busStationsList.add(new Geofence.Builder()
                .setRequestId("8")
                .setCircularRegion(29.989988,
                        31.150007,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Areish
        busStationsList.add(new Geofence.Builder()
                .setRequestId("9")
                .setCircularRegion(29.993816,
                        31.160403,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Cairo Mall
        busStationsList.add(new Geofence.Builder()
                .setRequestId("10")
                .setCircularRegion(29.998824,
                        31.173492,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Talbia
        busStationsList.add(new Geofence.Builder()
                .setRequestId("11")
                .setCircularRegion(30.001732,
                        31.181195,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Giza Square
        busStationsList.add(new Geofence.Builder()
                .setRequestId("12")
                .setCircularRegion(30.015417,
                        31.212062,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());*/


        /*busStationsList.add(new Geofence.Builder()
                .setRequestId("11")
                .setCircularRegion(29.992775, 31.157512,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        busStationsList.add(new Geofence.Builder()
                .setRequestId("12")
                .setCircularRegion(29.993853, 31.160677,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        busStationsList.add(new Geofence.Builder()
                .setRequestId("13")
                .setCircularRegion(29.995135, 31.163810,
                        60.0f)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());*/
    }

    private GeofencingRequest getGeofenceRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(busStationsList);
        return builder.build();
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

            if(geofenceListIndex < geofenceList.size()) {
                //setBusStationsList(geofenceList.get(geofenceListIndex).latitude, geofenceList.get(geofenceListIndex).longitude);



            }
        }
    }

    private void  showMarkersAndZoom(){
        Log.d("TEST", "ShowMarkers and zoom");
        mMap.clear();
        if(mLastLocation != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.shuttle)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16), 200, null);
        }
        for(int i = 0; i < geofenceList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(geofenceList.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop24)));
        }

        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(myLocationChangedList.get(myLocationChangeListIndex));
        builder.include(geofenceList.get(geofenceListIndex));


        LatLngBounds bounds = builder.build();
        int padding = 50;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);*/

    }

    private void startLocationUpdates(){
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(3000)
                .setInterval(3000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(mLastLocation != null)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16), 200, null);
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

    /*private boolean checkPermission() {
        Log.d("TEST", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {

        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                101
        );
    }*/

    public  boolean isPermissionsGranted(Context context, String[] grantPermissions) {
        Log.d("TEST", "checkPermission()");
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
        Log.d("TEST", "askPermission()");
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
        Log.d("TEST", "onLocationChanged");
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
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);*/
        /*location.setLatitude(myLocationChangedList.get(myLocationChangeListIndex).latitude);
        location.setLongitude(myLocationChangedList.get(myLocationChangeListIndex).longitude);*/
        //geofenceListIndex++;
        showMarkersAndZoom();
        myLocationChangeListIndex++;
        if(counter == 2){

            counter = 0;
        }else {
            counter++;
        }
        //startCountDownTimer();
    }

   /* private void startCountDownTimer(){
        CountDownTimer requestTimer = new CountDownTimer(3000, 1000L) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Log.d("Test", "CountDownTimer Finished");
                onLocationChanged(new Location(LocationManager.GPS_PROVIDER));
            }
        };
        requestTimer.start();
    }*/

    public  static Intent makeNotificationIntent(Context geofenceService, String msg)
    {
        Log.d("Test",msg);
        return new Intent(geofenceService,MainActivity.class);
    }

    @Override
    public void onGeoLocationEntered(int index) {

    }

    @Override
    public void onGeoLocationExited(int index) {

    }
}

package arab_open_university.com.bustracker;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, LocationListener {

    GoogleMap mMap;
    GoogleApiClient googleApiClient;

    boolean isMapReady = false;

    private final LatLng mashaalLatLng = new LatLng(29.986782, 31.141370);
    private final LatLng mariotiaLatLng = new LatLng(29.989988, 31.150007);
    private final LatLng areishLatLng = new LatLng(29.993816, 31.160403);
    private final LatLng carioMallLatLng = new LatLng(29.998824, 31.173492);
    private final LatLng talbiaLatLng = new LatLng(30.001732, 31.181195);
    private final LatLng gizaSquareLatLng = new LatLng(30.015417, 31.212062);
    private final LatLng startLocation = new LatLng(29.984625, 31.136037);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MapFragment mapFragment = new MapFragment();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction().replace(R.id.map_view, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, 0, this)
                .build();

        googleApiClient.connect();

        busStationsList = new ArrayList<>();
        geofenceList = new ArrayList<>();
        geofenceList.add(mashaalLatLng);
        geofenceList.add(mariotiaLatLng);
        geofenceList.add(areishLatLng);
        geofenceList.add(carioMallLatLng);
        geofenceList.add(talbiaLatLng);
        geofenceList.add(gizaSquareLatLng);

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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocationChangedList.get(myLocationChangeListIndex), 16), 200, null);

        showMarkersAndZoom();



        setBusStationsList();

        if(checkPermission()) {

            startLocationUpdates();
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofenceRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);

            startCountDownTimer();
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
    }

    private void  setBusStationsList(){
        Log.d("TEST", "SETBusStationsList");
        String id = UUID.randomUUID().toString();

        //Mashaal
        busStationsList.add(new Geofence.Builder()
                .setRequestId("1")
                .setCircularRegion(29.986782,
                        31.141370,
                        20)
                .setExpirationDuration(10000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Mariotia
        busStationsList.add(new Geofence.Builder()
                .setRequestId("2")
                .setCircularRegion(29.989988,
                        31.150007,
                        20)
                .setExpirationDuration(10000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Areish
        busStationsList.add(new Geofence.Builder()
                .setRequestId("3")
                .setCircularRegion(29.993816,
                        31.160403,
                        20)
                .setExpirationDuration(10000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Cairo Mall
        busStationsList.add(new Geofence.Builder()
                .setRequestId("4")
                .setCircularRegion(29.998824,
                        31.173492,
                        20)
                .setExpirationDuration(10000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Talbia
        busStationsList.add(new Geofence.Builder()
                .setRequestId("5")
                .setCircularRegion(30.001732,
                        31.181195,
                        20)
                .setExpirationDuration(10000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        //Giza Square
        busStationsList.add(new Geofence.Builder()
                .setRequestId("6")
                .setCircularRegion(30.015417,
                        31.212062,
                        20)
                .setExpirationDuration(10000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

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
        mMap.addMarker(new MarkerOptions().position(myLocationChangedList.get(myLocationChangeListIndex)).icon(BitmapDescriptorFactory.fromResource(R.drawable.shuttle)));
        mMap.addMarker(new MarkerOptions().position(geofenceList.get(geofenceListIndex)).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop24)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(myLocationChangedList.get(myLocationChangeListIndex));
        builder.include(geofenceList.get(geofenceListIndex));


        LatLngBounds bounds = builder.build();
        int padding = 50;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    private void startLocationUpdates(){
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(3000)
                .setInterval(3000);
    }

    private boolean checkPermission() {
        Log.d("TEST", "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d("TEST", "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                101
        );
    }


    @Override
    public void onLocationChanged(final Location location) {
        Log.d("TEST", "onLocationChanged");
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);*/
        /*location.setLatitude(myLocationChangedList.get(myLocationChangeListIndex).latitude);
        location.setLongitude(myLocationChangedList.get(myLocationChangeListIndex).longitude);*/
        showMarkersAndZoom();
        myLocationChangeListIndex++;
        if(counter == 2){
            geofenceListIndex++;
            counter = 0;
        }else {
            counter++;
        }
        startCountDownTimer();
    }

    private void startCountDownTimer(){
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
    }
}

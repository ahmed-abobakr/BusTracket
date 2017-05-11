package arab_open_university.com.bustracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    EditText editPlateNum, editPassword;
    Button btnLogin;

    //Firebase Credentials
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Inflate layout views
        editPlateNum = (EditText) findViewById(R.id.login_plate_num);
        editPassword = (EditText) findViewById(R.id.login_password);
        btnLogin = (Button) findViewById(R.id.login_btn);


        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    doLogin();
                    return true;
                }
                return false;
            }
        });

        //Intialize firebase
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doLogin();
            }
        });

    }

    private void doLogin(){
        /* this method is called when uer presses btn login
         * it checks if user enter the email and password
          * if not show message to ask user to enter them
          * if yes authenticate  firebase and if successful shows the map
          * if not show message that authentication fail */
        if(editPlateNum.getText().toString() == null || editPlateNum.getText().toString().isEmpty() ||
                editPassword.getText().toString() == null || editPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "please, enter email and password", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(editPlateNum.getText().toString(), editPassword.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void addBusTodatabase(){
        /* use this method to add bus info and bus stations locations to database
         * should be removed in presentation */
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                /*DatabaseReference busStationsReference = database.getReference("buses_Info");
                                BusStation busStation = new BusStation();
                                busStation.setNumOfBusStations(4);
                                List<String> busIDs = new ArrayList<String>();
                                busIDs.add(user.getUid());
                                busStation.setBusesIDs(busIDs);
                                List<String> busStationsNames = new ArrayList<String>();
                                busStationsNames.add("العريش");
                                busStationsNames.add("كايرو مول");
                                busStationsNames.add("الطالبية ");
                                busStationsNames.add("الجيزة");
                                busStation.setBusStationsNames(busStationsNames);
                                List<Double> busStationsLat = new ArrayList<Double>();
                                List<Double> buStationsLong = new ArrayList<Double>();
                                busStationsLat.add(29.993755);
                                buStationsLong.add(31.160263);
                                busStationsLat.add(29.998824);
                                buStationsLong.add(31.173492);
                                busStationsLat.add(30.001732);
                                buStationsLong.add(31.181195);
                                busStationsLat.add(30.015417);
                                buStationsLong.add(31.212062);
                                busStation.setBusStationsLat(busStationsLat);
                                busStation.setBusStationsLong(buStationsLong);
                                busStationsReference.child("300").setValue(busStation);
                                GeoFire geoFire = new GeoFire(busStationsReference);*/
                                    /*geoFire.setLocation("first_Station", );
                                    geoFire.setLocation("second_Station", );
                                    geoFire.setLocation("third_Station", );
                                    geoFire.setLocation("fourth_Station", );
                                    geoFire.setLocation("fifth_Station", );
                                    geoFire.setLocation("sixth_Station", );*/
    }
}

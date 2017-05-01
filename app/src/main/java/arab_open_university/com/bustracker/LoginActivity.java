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

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                doLogin();
            }
        });

    }

    private void doLogin(){
        if(editPlateNum.getText().toString() == null || editPlateNum.getText().toString().isEmpty() ||
                editPassword.getText().toString() == null || editPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "من فضلك تأكد من إدخال الإيميل وكلمة المرور", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(editPlateNum.getText().toString(), editPassword.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                /*FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference busStationsReference = database.getReference("buses_Info");
                                BusStation busStation = new BusStation();
                                busStation.setNumOfBusStations(6);
                                List<String> busIDs = new ArrayList<String>();
                                busIDs.add(user.getUid());
                                busStation.setBusesIDs(busIDs);
                                List<String> busStationsNames = new ArrayList<String>();
                                busStationsNames.add("الأولى");
                                busStationsNames.add("الثانية");
                                busStationsNames.add("الثالثة");
                                busStationsNames.add("الرابعة");
                                busStationsNames.add("الخامسة");
                                busStationsNames.add("السادسة");
                                busStation.setBusStationsNames(busStationsNames);
                                List<Double> busStationsLat = new ArrayList<Double>();
                                List<Double> buStationsLong = new ArrayList<Double>();
                                busStationsLat.add(29.959698);
                                buStationsLong.add(31.103493);
                                busStationsLat.add(29.958564);
                                buStationsLong.add(31.103622);
                                busStationsLat.add(29.958434);
                                buStationsLong.add(31.104781);
                                busStationsLat.add(29.958229);
                                buStationsLong.add(31.106519);
                                busStationsLat.add(29.959205);
                                buStationsLong.add(31.106959);
                                busStationsLat.add(29.959484);
                                buStationsLong.add(31.104963);
                                busStation.setBusStationsLat(busStationsLat);
                                busStation.setBusStationsLong(buStationsLong);
                                busStationsReference.child("122").setValue(busStation);
                                GeoFire geoFire = new GeoFire(busStationsReference);*/
                                    /*geoFire.setLocation("first_Station", );
                                    geoFire.setLocation("second_Station", );
                                    geoFire.setLocation("third_Station", );
                                    geoFire.setLocation("fourth_Station", );
                                    geoFire.setLocation("fifth_Station", );
                                    geoFire.setLocation("sixth_Station", );*/

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

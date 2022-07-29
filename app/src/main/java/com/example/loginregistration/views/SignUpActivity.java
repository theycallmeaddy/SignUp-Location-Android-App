package com.example.loginregistration.views;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loginregistration.R;
import com.example.loginregistration.database.UserDao;
import com.example.loginregistration.database.UserDatabase;
import com.example.loginregistration.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {


    private EditText name, email, password, phone;
    private Button signUp;
    public String cord;
    double lat, lon;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.nameET);
        email = (EditText) findViewById(R.id.emailET);
        phone = (EditText) findViewById(R.id.phoneET);
        password = (EditText) findViewById(R.id.passwordET);
        signUp = (Button) findViewById(R.id.signUpButton);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            getLocation();
        } 
        else 
        {
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            finish();

        }


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating user entity



                User user = new User();
                user.setEmail(email.getText().toString());
                user.setName(name.getText().toString());
                user.setPassword(password.getText().toString());
                user.setPhoneNo(phone.getText().toString());
                user.setLat(lat);
                user.setLon(lon);


                if (validateUser(user)) {
                    //Do insert Operations
                    UserDatabase userDatabase = UserDatabase.userDB(getApplicationContext());
                    UserDao userDao = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            //Register User on new thread
                            userDao.insert(user);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getLocation() {

        Log.d("Location Entered", "getLocation: ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Log.d("Inside here", "not null ");

                        Geocoder geocoder = new Geocoder(SignUpActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        lat = addresses.get(0).getLatitude();
                        lon = addresses.get(0).getLongitude();
                        Toast.makeText(SignUpActivity.this, "Location Registered", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        Log.d(e.toString(), "Location Not Retrieved ");
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //validate if any field is empty
    private boolean validateUser(User user)
    {
        if(user.getName().isEmpty()|| user.getEmail().isEmpty()||
                user.getPhoneNo().isEmpty()|| user.getPassword().isEmpty()){

            return false;
        }
        return true;
    }

    //when SignIn button is clicked
    public void finishActivity(View v){
        finish();
    }

}

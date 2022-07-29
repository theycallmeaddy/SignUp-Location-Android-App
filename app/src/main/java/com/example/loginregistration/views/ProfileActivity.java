package com.example.loginregistration.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ProfileActivity extends AppCompatActivity {

    SupportMapFragment smf;
    FusedLocationProviderClient client;

    TextView pname,pemail,pphone;
    ImageView img;
    Button seeAll;
    String cord;

    public static String name,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        pname = findViewById(R.id.profileName);
        pemail = findViewById(R.id.email);
        pphone = findViewById(R.id.phoneNumber);
        img = findViewById(R.id.profileImage);
        seeAll = findViewById(R.id.seeAll);


        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");

        pname.setText(name);
        pphone.setText(phone);
        pemail.setText(email);


        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,MyLocationActivity.class);
                startActivity(intent);
            }
        });


        /*location = findViewById(R.id.location);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,MyLocationActivity.class);
                startActivity(intent);
            }
        });*/


        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);

        //Runtime Permission using Dexter

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            buildAlertMessageNoGps();
                        }
                        else {
                            getMyLocation();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getMyLocation() {

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
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {
                    @Override

                    public void onMapReady(@NonNull GoogleMap googleMap) {


                        UserDatabase userDatabase = UserDatabase.userDB(getApplicationContext());
                        UserDao userDao = userDatabase.userDao();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d(name + email, "Detaaaa");

                                    User user = userDao.findLoc(name,email);
                                if(user == null){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ProfileActivity.this, "Error Getting Location", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            LatLng latLng = new LatLng(user.getLat(),user.getLon());

                                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here");

                                            googleMap.addMarker(markerOptions);
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                                        }
                                    });


                                }
                            }
                        }).start();








                    }
                });
            }
        });



    }
}
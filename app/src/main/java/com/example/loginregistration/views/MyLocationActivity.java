package com.example.loginregistration.views;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class MyLocationActivity extends AppCompatActivity {

    private GoogleMap mMap;
    public static SupportMapFragment supportMapFragment;

    public int id,count;
    private static ArrayList<LatLng>arrayList = new ArrayList<LatLng>();

    LatLng sydney = new LatLng(-34,151);
    LatLng dubbo = new LatLng(-32,148);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        UserDatabase userDatabase = UserDatabase.userDB(getApplicationContext());
        UserDao userDao = userDatabase.userDao();

        arrayList.add(sydney);
        arrayList.add(dubbo);




        new Thread(new Runnable() {
            @Override
            public void run() {


                count = userDao.getCount();
                for(int id=1; id<=count; id++){

                    User user = userDao.findUsrId(id);

                    Log.d(Integer.toString(count), "run: COUNT");
                    int finalId = id;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(String.valueOf(user.user_id)+"yes", "Insider");

                            LatLng latLng = new LatLng(user.getLat(),user.getLon());
                            arrayList.add(latLng);

                            Log.d(String.valueOf(user.user_id)+"yes", arrayList.get(finalId).toString());

                            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {

                                    for(int i=0;i< arrayList.size();i++){

                                        Log.d(arrayList.get(i).toString(), "run: ArrayList Insert ");
                                        MarkerOptions markerOptions=new MarkerOptions().position(arrayList.get(i)).title(user.name);

                                        googleMap.addMarker(markerOptions);
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(i),4));

                                    }

                                }
                            });


                        }
                    });
                }

            }
        }).start();




    }


}
package com.example.pets.beforeAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pets.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetLocation extends AppCompatActivity implements getLocationBS.getLocationListener{

    Button getLocationButton, getLocationButton2, next;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("LOCATION REQUIRED");
        new AlertDialog.Builder(GetLocation.this)
                .setTitle("Location required!")
                .setMessage("In order to find buyers around you, " +
                        "we need to know your city name and pincode. ")
                .setNeutralButton("OK", null).show();

        getLocationButton= findViewById(R.id.getLocationButton);
        getLocationButton2= findViewById(R.id.getLocationButton2);
        textView1= findViewById(R.id.textView1);
        textView2= findViewById(R.id.textView2);
        next= findViewById(R.id.next);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        next.setEnabled(false);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(GetLocation.this, register.class);
                intent.putExtra("city", textView1.getText().toString());
                intent.putExtra("pin", textView2.getText().toString());
                startActivity(intent);
            }
        });

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(GetLocation.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    nowGetLocation();
                } else {
                    ActivityCompat.requestPermissions(GetLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            44);
            }
        }

            @SuppressLint("MissingPermission")
            private void nowGetLocation() {


                final ProgressDialog dialog = ProgressDialog.show(GetLocation.this, "",
                        "Loading. Please wait...", true);

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location= task.getResult();
                        if (location!=null)
                        {
                            try {
                                Geocoder geocoder= new Geocoder(GetLocation.this,
                                        Locale.getDefault());

                                List<Address> addresses= geocoder.getFromLocation(location.getLatitude()
                                        , location.getLongitude(), 1);

                                dialog.dismiss();
                                textView1.setText(addresses.get(0).getLocality());
                                textView2.setText(addresses.get(0).getPostalCode());
                                next.setEnabled(true);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            dialog.dismiss();
                            new AlertDialog.Builder(GetLocation.this)
                                    .setTitle("Unexpected Error :(")
                                    .setMessage("Please Check if you have Internet Connection/" +
                                            "Please Check if your Location Service is enabled/" +
                                            "If problem persists, You can enter the information manually")
                                    .setNeutralButton("OK", null)
                                    .show();

                        }
                    }
                });
            }
            });

        getLocationButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationBS getLocationBS= new getLocationBS();
                getLocationBS.show(getSupportFragmentManager(), "getLocationBS");
                next.setEnabled(true);
            }
        });

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void applyTexts(String CITY, String PIN) {
        textView1.setText(CITY);
        textView2.setText(PIN);
    }
}
package com.home.locationbasedservices;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.home.locationbasedservices.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<Task> tasks = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng latLngCurrent;
    private Button buttonAddTask;
    private Button buttonSetSoundProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        firebaseDatabase = FirebaseDatabase.getInstance("https://androidlocationbasedservices-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("UserTasks");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Toast.makeText(MapActivity.this, "addValueEventListener", Toast.LENGTH_SHORT).show();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Task value = child.getValue(Task.class);
                    tasks.add(value);
                }

                mMap.clear();
                for (Task task : tasks) {
                    LatLng latLng = new LatLng(task.getLatitude(), task.getLongitude());
                    MarkerOptions marker = new MarkerOptions()
                            .position(latLng)
                            .title(task.getTitle())
                            .snippet(task.getDescription());
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                    mMap.addMarker(marker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonAddTask = findViewById(R.id.button_add_task);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MapActivity.this, TaskActivity.class), 1);
            }
        });

        buttonSetSoundProfile = findViewById(R.id.button_set_sound_profile);
        buttonSetSoundProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng toVisitPlace = new LatLng(30, 72);
        //mMap.addMarker(new MarkerOptions().position(toVisitPlace).title("Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(toVisitPlace));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCurrent));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrent, 12.0f));
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

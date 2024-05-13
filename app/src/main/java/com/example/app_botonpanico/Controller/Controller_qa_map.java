package com.example.app_botonpanico.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class Controller_qa_map extends AppCompatActivity implements OnMapReadyCallback {

    ImageButton mainMenuButtom;
    GoogleMap gOmap;
    FloatingActionsMenu floatingActionsMenu_group;
    FloatingActionButton fabButtonLayer, fabButtonFindMyLocation, fabButtonEmergencyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Map_activityQaMap);
        mapFragment.getMapAsync(this);
        mainMenuButtom=findViewById(R.id.Menu_activityQaMap);
        floatingActionsMenu_group = findViewById(R.id.GroupButton_FloatingButton_activityQaMap);
        fabButtonLayer = findViewById(R.id.Layer_FloatingButton_activityQaMap);
        fabButtonFindMyLocation = findViewById(R.id.FindMyLocation_FloatingButton_activityQaMap);
        fabButtonEmergencyButton = findViewById(R.id.ButtonEmergency_FloatingButton_activityQaMap);

        mainMenuButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Controller_qa_map.this, Controller_qa_main_menu.class);
                finish();
            }
        });
        fabButtonLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gOmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });
        fabButtonFindMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(Controller_qa_map.this);
                if (ActivityCompat.checkSelfPermission(Controller_qa_map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Controller_qa_map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Controller_qa_map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(Controller_qa_map.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    gOmap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Zoom de 15 (puedes ajustar este valor según tus necesidades)
                                } else {
                                    Toast.makeText(Controller_qa_map.this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                                }
                            }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gOmap=googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gOmap.setMyLocationEnabled(true);
        gOmap.getUiSettings().setMyLocationButtonEnabled(false);
        gOmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Controller_qa_map.this,R.raw.map_style_dark));
    }

}
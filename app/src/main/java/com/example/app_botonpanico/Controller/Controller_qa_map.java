package com.example.app_botonpanico.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Service.Send_Message_Service;
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

    RelativeLayout mainMenuButtom;
    GoogleMap gOmap;
    FloatingActionsMenu floatingActionsMenu_group;
    FloatingActionButton fabButtonLayer, fabButtonFindMyLocation, fabButtonEmergencyButton;
    String first_name_IntentUser, last_name_IntentUser, phone_number_IntentUser,email_IntentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_map);

        Intent intentToPanicButtom = getIntent();
        first_name_IntentUser = intentToPanicButtom.getStringExtra("first_name");
        last_name_IntentUser = intentToPanicButtom.getStringExtra("last_name");
        phone_number_IntentUser = intentToPanicButtom.getStringExtra("phone_number");
        email_IntentUser = intentToPanicButtom.getStringExtra("email");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Map_activityQaMap);
        mapFragment.getMapAsync(this);
        mainMenuButtom=findViewById(R.id.Menu_activityQaMap);
        floatingActionsMenu_group = findViewById(R.id.GroupButton_FloatingButton_activityQaMap);
        fabButtonLayer = findViewById(R.id.Layer_FloatingButton_activityQaMap);
        fabButtonFindMyLocation = findViewById(R.id.FindMyLocation_FloatingButton_activityQaMap);
        fabButtonEmergencyButton = findViewById(R.id.ButtonEmergency_FloatingButton_activityQaMap);


        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(Controller_qa_map.this);
        if (ActivityCompat.checkSelfPermission(Controller_qa_map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Controller_qa_map.this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Controller_qa_map.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(Controller_qa_map.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    gOmap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12)); // Zoom de 15 (puedes ajustar este valor según tus necesidades)
                } else {
                    Toast.makeText(Controller_qa_map.this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainMenuButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (ActivityCompat.checkSelfPermission(Controller_qa_map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Controller_qa_map.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Controller_qa_map.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
                fusedLocationClient.getLastLocation().addOnSuccessListener(Controller_qa_map.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            gOmap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18)); // Zoom de 15 (puedes ajustar este valor según tus necesidades)
                        } else {
                            Toast.makeText(Controller_qa_map.this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        fabButtonEmergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Controller_qa_map.this, android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Controller_qa_map.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    Intent send_message_service = new Intent(Controller_qa_map.this, Send_Message_Service.class);
                    send_message_service.putExtra("first_name", first_name_IntentUser);
                    send_message_service.putExtra("last_name", last_name_IntentUser);
                    send_message_service.putExtra("phone_number", phone_number_IntentUser);
                    send_message_service.putExtra("email", email_IntentUser);
                    startService(send_message_service);

                } else {
                    ActivityCompat.requestPermissions(Controller_qa_map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        gOmap.setMyLocationEnabled(true);
        gOmap.getUiSettings().setMyLocationButtonEnabled(false);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                gOmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark));
                break;
            default:
                gOmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_light));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent serviceIntent = new Intent(this, Send_Message_Service.class);
                serviceIntent.putExtra("first_name", first_name_IntentUser);
                serviceIntent.putExtra("last_name", last_name_IntentUser);
                serviceIntent.putExtra("phone_number", phone_number_IntentUser);
                serviceIntent.putExtra("email", email_IntentUser);
                startService(serviceIntent);
            } else {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
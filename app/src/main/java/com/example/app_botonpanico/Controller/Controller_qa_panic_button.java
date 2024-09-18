package com.example.app_botonpanico.Controller;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.airbnb.lottie.LottieAnimationView;
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Model.Model_contact_data;
import com.example.app_botonpanico.Dao.daoContact;
import com.example.app_botonpanico.Service.Send_Message_Service;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class Controller_qa_panic_button extends AppCompatActivity {

    daoContact daoContact;
    ArrayList<Model_contact_data> listContacts;
    RelativeLayout panicButton, mainMenuButtom;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    String first_name_IntentUser, last_name_IntentUser, phone_number_IntentUser, email_IntentUser;
    LottieAnimationView ButtonAnimationSOS;
    private Location currentLocation;
    private Handler handler;
    private Runnable runnable;


    private boolean isServiceRunning() {
        SharedPreferences prefs = getSharedPreferences("ServicePrefs", MODE_PRIVATE);
        return prefs.getBoolean("serviceIsRunning", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_panic_button);
        //datos
        Intent intentToPanicButtom = getIntent();
        first_name_IntentUser = intentToPanicButtom.getStringExtra("first_name");
        last_name_IntentUser = intentToPanicButtom.getStringExtra("last_name");
        phone_number_IntentUser = intentToPanicButtom.getStringExtra("phone_number");
        email_IntentUser = intentToPanicButtom.getStringExtra("email");
        //Varaibles
        mainMenuButtom = findViewById(R.id.Menu_activityQaPanicButton);
        panicButton = findViewById(R.id.imageButton2);
        ButtonAnimationSOS = findViewById(R.id.buttonAnimation_LottieAnimation_ActivityQaPanicButton);
        daoContact = new daoContact(this);
        listContacts = daoContact.getAllByEmail();
        handler = new Handler(Looper.getMainLooper());
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //BroadCastReceiver
        Intent send_message_service = new Intent(Controller_qa_panic_button.this, Send_Message_Service.class);
        IntentFilter filter = new IntentFilter("com.example.SERVICE_STATUS");
        registerReceiver(serviceStatusReceiver2, filter);

        if (ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Controller_qa_panic_button.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        String provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ? LocationManager.GPS_PROVIDER
                : LocationManager.NETWORK_PROVIDER;
        locationManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    currentLocation = location;
                    locationManager.removeUpdates(this);
                } else {
                    Toast.makeText(Controller_qa_panic_button.this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainMenuButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(Controller_qa_panic_button.this);
                if (ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Controller_qa_panic_button.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }else{
                    fusedLocationClient.getLastLocation().addOnSuccessListener(Controller_qa_panic_button.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location  != null) {
                                if (ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED &&
                                        ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    send_message_service.putExtra("first_name", first_name_IntentUser);
                                    send_message_service.putExtra("last_name", last_name_IntentUser);
                                    send_message_service.putExtra("phone_number", phone_number_IntentUser);
                                    send_message_service.putExtra("email", email_IntentUser);
                                    startService(send_message_service);
                                } else {
                                    ActivityCompat.requestPermissions(Controller_qa_panic_button.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                                }
                            } else{
                                if (currentLocation  != null) {
                                    if (ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                                                android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED &&
                                                ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                                                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            send_message_service.putExtra("first_name", first_name_IntentUser);
                                            send_message_service.putExtra("last_name", last_name_IntentUser);
                                            send_message_service.putExtra("phone_number", phone_number_IntentUser);
                                            send_message_service.putExtra("email", email_IntentUser);
                                            startService(send_message_service);
                                        } else {
                                            ActivityCompat.requestPermissions(Controller_qa_panic_button.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                                        }
                                } else {
                                    Toast.makeText(Controller_qa_panic_button.this, "Espere 2 segundos, y presione otra vez el botón", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    });
                }
            }
        });

        //Activar animación
        boolean serviceRunning = false;
        if (serviceRunning = isServiceRunning()) {
            ButtonAnimationSOS.setVisibility(View.VISIBLE);
            ButtonAnimationSOS.playAnimation();
        } else {
            ButtonAnimationSOS.cancelAnimation();
            ButtonAnimationSOS.setVisibility(View.GONE);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private BroadcastReceiver serviceStatusReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isRunning = intent.getBooleanExtra("isRunning", true);
            System.out.println(isRunning);
            if (isRunning==false) {
                ButtonAnimationSOS.cancelAnimation();
                ButtonAnimationSOS.setVisibility(View.GONE);
            }else {
                ButtonAnimationSOS.setVisibility(View.VISIBLE);
                ButtonAnimationSOS.playAnimation();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    //Pruebas

    private void enableLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Crear un diálogo que redirige a la configuración de ubicación
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Para utilizar esta aplicación, es necesario que enciendas la ubicación. ¿Deseas encenderla ahora?")
                    .setCancelable(false)
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }



        //
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceStatusReceiver2);
    }
}
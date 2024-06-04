package com.example.app_botonpanico.Controller;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    RelativeLayout panicButton, mainMenuButtom;;
    String first_name_IntentUser, last_name_IntentUser, phone_number_IntentUser,email_IntentUser;
    LottieAnimationView ButtonAnimationSOS;
    private Handler handler;
    private Runnable runnable;
    private boolean isSending = false; // Control variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_panic_button);

        Intent intentToPanicButtom = getIntent();
        first_name_IntentUser = intentToPanicButtom.getStringExtra("first_name");
        last_name_IntentUser = intentToPanicButtom.getStringExtra("last_name");
        phone_number_IntentUser = intentToPanicButtom.getStringExtra("phone_number");
        email_IntentUser = intentToPanicButtom.getStringExtra("email");

        mainMenuButtom=findViewById(R.id.Menu_activityQaPanicButton);
        panicButton =findViewById(R.id.imageButton2);
        ButtonAnimationSOS=findViewById(R.id.buttonAnimation_LottieAnimation_ActivityQaPanicButton);
        daoContact = new daoContact(this);
        listContacts=daoContact.getAllByEmail();
        handler = new Handler(Looper.getMainLooper());

        IntentFilter filter = new IntentFilter("com.example.SERVICE_STATUS");
        registerReceiver(serviceStatusReceiver2, filter);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    Toast.makeText(Controller_qa_panic_button.this, "Favor de activar el GPS para poder compartir la ubicacion en tiempo real", Toast.LENGTH_SHORT).show();
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
                if (ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Intent send_message_service = new Intent(Controller_qa_panic_button.this, Send_Message_Service.class);
                    send_message_service.putExtra("first_name", first_name_IntentUser);
                    send_message_service.putExtra("last_name", last_name_IntentUser);
                    send_message_service.putExtra("phone_number", phone_number_IntentUser);
                    send_message_service.putExtra("email", email_IntentUser);
                    startService(send_message_service);

                } else {
                    ActivityCompat.requestPermissions(Controller_qa_panic_button.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private BroadcastReceiver serviceStatusReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isRunning = intent.getBooleanExtra("isRunning", false);
            if (isRunning) {
                ButtonAnimationSOS.setVisibility(View.VISIBLE);
                ButtonAnimationSOS.playAnimation();
            }else {
                ButtonAnimationSOS.cancelAnimation();
                ButtonAnimationSOS.setVisibility(View.GONE);
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



    private void stopSendingMessages() {
        isSending = false;
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            Intent serviceIntent = new Intent(this, Send_Message_Service.class);
            stopService(serviceIntent);
            Toast.makeText(this, "Envio de mensajes", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceStatusReceiver2);
    }



}
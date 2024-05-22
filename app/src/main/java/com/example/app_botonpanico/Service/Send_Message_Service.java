package com.example.app_botonpanico.Service;

import static android.content.Intent.getIntent;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app_botonpanico.Controller.Controller_qa_panic_button;
import com.example.app_botonpanico.Dao.daoContact;
import com.example.app_botonpanico.Model.Model_Contact_data;
import com.example.app_botonpanico.Model.Model_send_message_coordinates;

import java.util.ArrayList;
import java.util.List;

public class Send_Message_Service extends Service {

    daoContact daoContact;
    private List<Model_Contact_data> listContacts;
    String first_name_IntentUser, last_name_IntentUser, phone_number_IntentUser,email_IntentUser;

    private Handler handler;
    private Runnable runnable;
    private long startTime;
    private static final long INTERVAL = 3000; //300000;  5 minutes in milliseconds
    private static final long DURATION = 3600000; // 1 hour in milliseconds
    private boolean isSending = false; // Control variable
    private boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isSending) {

            daoContact = new daoContact(this);
            listContacts=daoContact.getAllByEmail();

            String firstName = intent.getStringExtra("first_name");
            String lastName = intent.getStringExtra("last_name");
            String phoneNumber = intent.getStringExtra("phone_number");
            String email = intent.getStringExtra("email");

            System.out.println(first_name_IntentUser+ " " + firstName);
            startTime= System.currentTimeMillis();
            startSendingMessages(first_name_IntentUser);
            isSending = true;
        }else {
            stopSendingMessages();
            isSending = false;
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void startSendingMessages(String first_name_User ) {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - startTime < DURATION) {
                    for (Model_Contact_data contact : listContacts) {
                        String[] coordinates = getCoordinates();
                        if (coordinates != null) {
                            String latitudeStr = coordinates[0];
                            String longitudeStr = coordinates[1];
                            //SendMessage(contact.getFirst_name(), contact.getLast_name(), contact.getEmail(), first_name_IntentUser, last_name_IntentUser, email_IntentUser, phone_number_IntentUser, latitudeStr, longitudeStr);
                            System.out.println(first_name_User+" "+contact.getFirst_name()+" " +contact.getEmail() + " "+ latitudeStr+" "+longitudeStr);
                        } else {
                            Toast.makeText(Send_Message_Service.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                    // Vuelve a ejecutar el runnable después de 5 minutos si sigue activo el envío
                    if (isSending) {
                        handler.postDelayed(this, INTERVAL);
                    }
                }
            }
        };
        // Inicia la primera ejecución
        handler.post(runnable);
    }
    private void dataMessage(String firs_name_contact,String last_name_contact, String email_contact
            , String myfirst_name,String mylast_name, String myemail, String myphone_number, String latitude, String longitude){
        Model_send_message_coordinates modelSendMessageCoordinates = new Model_send_message_coordinates(this);
        try {
            String url = "https://www.google.com/maps/?q=" + latitude + "," + longitude;
            modelSendMessageCoordinates.sendCoordinate(firs_name_contact,last_name_contact,email_contact,myfirst_name,mylast_name,myemail,myphone_number,url);
        }catch (Exception e){
            System.out.println(e);
            Toast.makeText(this,"No se envió el mensaje",Toast.LENGTH_SHORT).show();
        }
    }
    private String[] getCoordinates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No se pueden solicitar permisos desde un servicio, por lo tanto, si no se tienen permisos, no se obtienen coordenadas
            Log.e("MessageService", "No location permissions granted");
            return null;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String latitudeStr = String.valueOf(latitude);
                    String longitudeStr = String.valueOf(longitude);
                    return new String[]{latitudeStr, longitudeStr};
                } else {
                    Log.e("MessageService", "No se pudo obtener la ubicación");
                    return null;
                }
            } else {
                Log.e("MessageService", "LocationManager is null");
                return null;
            }
        }
    }
    private void stopSendingMessages() {
        isSending = false;
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            Toast.makeText(this, "Envio de mensajes cancelado", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
        handler.removeCallbacks(runnable);
    }


}

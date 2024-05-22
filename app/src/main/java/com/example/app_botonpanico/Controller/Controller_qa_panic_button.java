package com.example.app_botonpanico.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Model.Model_Contact_data;
import com.example.app_botonpanico.Dao.daoContact;
import com.example.app_botonpanico.Model.Model_send_message_coordinates;
import com.example.app_botonpanico.Service.Send_Message_Service;

import java.util.ArrayList;

public class Controller_qa_panic_button extends AppCompatActivity {

    daoContact daoContact;
    ArrayList<Model_Contact_data> listContacts;
    ImageButton imageButton2;
    String first_name_IntentUser, last_name_IntentUser, phone_number_IntentUser,age_IntentUser,email_IntentUser;
    private Intent send_Menssage_Service;
    private boolean isServiceRunning = false;
    private Handler handler;
    private Runnable runnable;
    private long startTime;
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
        imageButton2=findViewById(R.id.imageButton2);
        daoContact = new daoContact(this);
        listContacts=daoContact.getAllByEmail();
        handler = new Handler(Looper.getMainLooper());
        send_Menssage_Service = new Intent(this, Send_Message_Service.class);



        System.out.println(first_name_IntentUser);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this, android.Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Controller_qa_panic_button.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent serviceIntent = new Intent(this, Send_Message_Service.class);
                startService(serviceIntent);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private String[] getCoordinates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 200);
            return null;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String latitudeStr = String.valueOf(latitude);
                String longitudeStr = String.valueOf(longitude);
                System.out.println(latitudeStr +""+longitudeStr);
                return new String[]{latitudeStr, longitudeStr};
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
    }
    private void SendMessage(String firs_name_contact,String last_name_contact, String email_contact
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

    private void stopSendingMessages() {
        isSending = false;
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            Toast.makeText(this, "Envio de mensajes cancelado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detén el handler cuando la actividad se destruya
        stopSendingMessages();
    }


}
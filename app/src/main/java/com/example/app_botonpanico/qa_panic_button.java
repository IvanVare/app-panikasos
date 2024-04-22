package com.example.app_botonpanico;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.contacts.Model_Contact_data;
import com.example.app_botonpanico.contacts.daoContact;

import java.security.Permission;
import java.util.ArrayList;

public class qa_panic_button extends AppCompatActivity {

    daoContact daoContact;
    ArrayList<Model_Contact_data> listContacts;
    ImageButton imageButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_panic_button);
        imageButton2=findViewById(R.id.imageButton2);

        daoContact = new daoContact(this);
        listContacts=daoContact.getAllFirstNameAndPhoneNumber();

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(qa_panic_button.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    for (Model_Contact_data contact : listContacts) {
                        SendMessage(contact.getPhone_number(),contact.getFirst_name());
                    }
                } else {
                    ActivityCompat.requestPermissions(qa_panic_button.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void SendMessage(String phoneNumber_contact,String firs_name_contact){
        try {
            //Enviar el SMS
            SmsManager smsManager = SmsManager.getDefault();
            String message_final ="Buenas noches "+ firs_name_contact+ " pipip"+phoneNumber_contact + ".";
            smsManager.sendTextMessage(phoneNumber_contact,null,message_final,null,null);
            Toast.makeText(this,"Mensaje enviado ",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,"No se envió el mensaje",Toast.LENGTH_SHORT).show();
        }
    }
     

}
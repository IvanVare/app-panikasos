package com.example.app_botonpanico;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.contacts.Controller_qa_contacts;
import com.example.app_botonpanico.gmaps.Controller_qa_map;
import com.example.app_botonpanico.gmaps.Controller_qa_panic_button;

public class qa_main_menu extends AppCompatActivity {

    Button ToMapButton, ToContactsButtom, ToPanicButtom;
    ImageButton LogOutButtom;

    TextView FullNameUser;
    String first_name_IntentUser, last_name_IntentUser, phone_number_IntentUser,age_IntentUser,email_IntentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_main_menu);

        Intent intentToMainMenu = getIntent();
        first_name_IntentUser = intentToMainMenu.getStringExtra("first_name");
        last_name_IntentUser = intentToMainMenu.getStringExtra("last_name");
        phone_number_IntentUser = intentToMainMenu.getStringExtra("phone_number");
        age_IntentUser = intentToMainMenu.getStringExtra("age");
        email_IntentUser = intentToMainMenu.getStringExtra("email");

        ToMapButton=findViewById(R.id.ToMap_button_activityqa_MainMenu);
        ToContactsButtom=findViewById(R.id.ToContacts_button_activityQaMainMenu);
        ToPanicButtom=findViewById(R.id.ToPanicButtom_button_activityQaMainMenu);
        LogOutButtom=findViewById(R.id.imageButton_LogOut_ActivityQaMainMenu);

        FullNameUser=findViewById(R.id.FullNameUser_TextView_activityQaMainMenu);



        FullNameUser.setText("Hola "+first_name_IntentUser+" "+last_name_IntentUser);
        ToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(qa_main_menu.this, Controller_qa_map.class);
                startActivity(i);
            }
        });

        ToContactsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(qa_main_menu.this, Controller_qa_contacts.class);
                startActivity(i);
            }
        });


        ToPanicButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToPanicButtom = new Intent(qa_main_menu.this, Controller_qa_panic_button.class);
                intentToPanicButtom.putExtra("first_name",first_name_IntentUser);
                intentToPanicButtom.putExtra("last_name", last_name_IntentUser);
                intentToPanicButtom.putExtra("phone_number", phone_number_IntentUser);
                intentToPanicButtom.putExtra("age", age_IntentUser);
                intentToPanicButtom.putExtra("email", email_IntentUser);
                startActivity(intentToPanicButtom);
            }
        });

        LogOutButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Intent intent =new Intent(qa_main_menu.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
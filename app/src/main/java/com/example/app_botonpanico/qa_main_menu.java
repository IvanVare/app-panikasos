package com.example.app_botonpanico;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class qa_main_menu extends AppCompatActivity {

    Button ToMapButton, ToContactsButtom, ToPanicButtom;
    ImageButton LogOutButtom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_main_menu);
        ToMapButton=findViewById(R.id.ToMap_button_activityqa_MainMenu);
        ToContactsButtom=findViewById(R.id.ToContacts_button_activityQaMainMenu);
        ToPanicButtom=findViewById(R.id.ToPanicButtom_button_activityQaMainMenu);
        LogOutButtom=findViewById(R.id.imageButton_LogOut_ActivityQaMainMenu);
        ToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(qa_main_menu.this, qa_map.class);
                startActivity(i);
            }
        });

        ToContactsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(qa_main_menu.this,qa_contacts.class);
                startActivity(i);
            }
        });


        ToPanicButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(qa_main_menu.this, qa_panic_button.class);
                startActivity(i);
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
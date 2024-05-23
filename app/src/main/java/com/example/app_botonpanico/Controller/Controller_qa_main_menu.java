package com.example.app_botonpanico.Controller;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.app_botonpanico.Notification_panicbtn_active;
import com.example.app_botonpanico.R;

public class Controller_qa_main_menu extends AppCompatActivity {

    Button ToMapButton, ToContactsButtom, ToPanicButtom;
    ImageButton LogOutButtom;
    private static final String CHANNEL_ID = "Channel Id";

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission( Controller_qa_main_menu.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( Controller_qa_main_menu.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }

        FullNameUser.setText("Hola "+first_name_IntentUser+" "+last_name_IntentUser);
        ToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildNotificationCustomStyle();
                //Intent i = new Intent(Controller_qa_main_menu.this, Controller_qa_map.class);
                //startActivity(i);
            }
        });

        ToContactsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Controller_qa_main_menu.this, Controller_qa_contacts.class);
                startActivity(i);
            }
        });


        ToPanicButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentToPanicButtom = new Intent(Controller_qa_main_menu.this, Controller_qa_panic_button.class);
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
                Intent intent =new Intent(Controller_qa_main_menu.this, Controller_MainActivity.class);
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



    public void buildNotificationCustomStyle() {
        String channelId = "CHANNEL_ID_NOTIFICATION";
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_panicbtn_active);

        NotificationCompat.Builder notificationCustomStyle = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.icn_btnpanic_light)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intentNotification = new Intent(getApplicationContext(), Notification_panicbtn_active.class);
        intentNotification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentNotification.putExtra("data", "rellenar");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intentNotification, PendingIntent.FLAG_MUTABLE);
        notificationCustomStyle.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);

        if (notificationChannel == null) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            notificationChannel = new NotificationChannel(channelId, "Notification Channel", importance);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, notificationCustomStyle.build());
    }


}
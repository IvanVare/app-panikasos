package com.example.app_botonpanico.Controller;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.app_botonpanico.R;

public class Controller_qa_main_menu extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 2;
    RelativeLayout ButtonLayoutSOS, ToMapButton, ToContactsButtom, LogOutButtom;
    TextView FullNameUser;
    SwitchCompat SwitchCompatUbication;
    LottieAnimationView ButtonAnimationSOS;
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
        LogOutButtom=findViewById(R.id.imageButton_LogOut_ActivityQaMainMenu);
        FullNameUser=findViewById(R.id.FullNameUser_TextView_activityQaMainMenu);
        ButtonLayoutSOS=findViewById(R.id.buttomPanika_RelativeLayout_ActivityQaMainMenu);
        ButtonAnimationSOS=findViewById(R.id.buttonAnimation_LottieAnimation_ActivityQaMainMenu);
        SwitchCompatUbication=findViewById(R.id.switch_ActivityQaMainMenu);

        IntentFilter filter = new IntentFilter("com.example.SERVICE_STATUS");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        SwitchCompatUbication.setChecked(isGpsEnabled);
        registerReceiver(serviceStatusReceiver, filter, Context.RECEIVER_EXPORTED);

        FullNameUser.setText("Hola, "+first_name_IntentUser+" "+last_name_IntentUser);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.POST_NOTIFICATIONS
                    }, REQUEST_LOCATION_PERMISSION);
        }



        SwitchCompatUbication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkLocationPermission();
                } else {
                    disableLocation();
                }
            }
        });

        ButtonLayoutSOS.setOnClickListener(new View.OnClickListener() {
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

        ToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToMap = new Intent(Controller_qa_main_menu.this, Controller_qa_map.class);
                intentToMap.putExtra("first_name",first_name_IntentUser);
                intentToMap.putExtra("last_name", last_name_IntentUser);
                intentToMap.putExtra("phone_number", phone_number_IntentUser);
                intentToMap.putExtra("age", age_IntentUser);
                intentToMap.putExtra("email", email_IntentUser);
                startActivity(intentToMap);
            }
        });

        ToContactsButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Controller_qa_main_menu.this, Controller_qa_contacts.class);
                startActivity(i);
            }
        });
        LogOutButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Intent intent =new Intent(Controller_qa_main_menu.this, Controller_mainActivity.class);
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

    private BroadcastReceiver serviceStatusReceiver = new BroadcastReceiver() {
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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

        } else {
            enableLocation();

        }
    }
    private void enableLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void disableLocation() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "Encender ubicación para usar la aplicación", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) ||
                            permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        enableLocation();
                    } else if (permissions[i].equals(Manifest.permission.POST_NOTIFICATIONS)) {
                    }
                } else {
                    Toast.makeText(this, "Permisos necesarios: " + permissions[i], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceStatusReceiver);
    }

}
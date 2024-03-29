package com.example.app_botonpanico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.data_register.data_register;

public class sign_in_user extends AppCompatActivity {

    Button ForgotPasswordButton, SignInButtonToMenu, LogInButtonToLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_user);
        ForgotPasswordButton=findViewById(R.id.ForgotPassword_button_activitySign_in);
        SignInButtonToMenu=findViewById(R.id.SignIn_button_activitySign_in);
        LogInButtonToLogIn=findViewById(R.id.LogIn_button_activitySign_in);

        ForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(sign_in_user.this, reset_password.class);
                startActivity(i);
            }
        });

        LogInButtonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(sign_in_user.this, data_register.class);
                startActivity(i);
            }
        });

        SignInButtonToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(sign_in_user.this, main_menu.class);
                startActivity(i);
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
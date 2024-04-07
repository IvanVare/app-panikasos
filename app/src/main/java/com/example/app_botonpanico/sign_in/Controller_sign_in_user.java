package com.example.app_botonpanico.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.R;
import com.example.app_botonpanico.data_register.Controller_data_register;
import com.example.app_botonpanico.reset_password;

public class Controller_sign_in_user extends AppCompatActivity {

    EditText InputPhoneNumber, InputPassword;
    Button ForgotPasswordButton, SignInButtonToMenu, RegisterButtonToLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_user);
        ForgotPasswordButton=findViewById(R.id.ForgotPassword_button_activitySign_in);
        SignInButtonToMenu=findViewById(R.id.SignIn_button_activitySign_in);
        RegisterButtonToLogIn=findViewById(R.id.Register_button_activitySign_in);
        InputPhoneNumber=findViewById(R.id.InputPhoneNumber_activitySign_in);
        InputPassword=findViewById(R.id.InputPassword_activitySign_in);
        Model_sign_in modelSignIn = new Model_sign_in(this);

        ForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Controller_sign_in_user.this, reset_password.class);
                startActivity(i);
            }
        });

        RegisterButtonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Controller_sign_in_user.this, Controller_data_register.class);
                startActivity(i);
            }
        });

        SignInButtonToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelSignIn.validateUser(InputPhoneNumber.getText().toString(), InputPassword.getText().toString());
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}
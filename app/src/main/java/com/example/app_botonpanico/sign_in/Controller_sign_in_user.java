package com.example.app_botonpanico.sign_in;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.R;
import com.example.app_botonpanico.data_register.Controller_data_register;
import com.example.app_botonpanico.qa_main_menu;
import com.example.app_botonpanico.reset_password.Controller_reset_password;
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_sign_in_user extends AppCompatActivity implements CheckData {

    EditText InputPhoneNumber, InputPassword;
    Button ForgotPasswordButton, SignInButtonToMenu, RegisterButtonToLogIn;
    String phonenumberString, passwordString;

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
        returnpreferences();
        ForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Controller_sign_in_user.this, Controller_reset_password.class);
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
                phonenumberString=InputPhoneNumber.getText().toString();
                passwordString=InputPassword.getText().toString();
                if (!phonenumberString.isEmpty() && !passwordString.isEmpty()){
                    //saveSesion();
                    Login(v);
                }else {
                    Toast.makeText(Controller_sign_in_user.this,"Numero telefonico o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void Login(View view) {
        phonenumberString=InputPhoneNumber.getText().toString();
        passwordString=InputPassword.getText().toString();

        if (!phonenumberString.isEmpty() && !passwordString.isEmpty()){
            //saveSesion();
            Model_sign_in modelsignin = new Model_sign_in(phonenumberString, passwordString,this,this);
            modelsignin.validateUser();
        }else {
            Toast.makeText(Controller_sign_in_user.this,"Numero telefonico o contraseña incorrectos",Toast.LENGTH_SHORT).show();
        }
    }
    private void saveSesion(){
        SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone_number_user",phonenumberString);
        editor.putString("password_user",passwordString);
        editor.putBoolean("sesion",true);
        editor.commit();
    }
    private void returnpreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        InputPhoneNumber.setText(sharedPreferences.getString("phone_number_user",""));
        InputPassword.setText(sharedPreferences.getString("password_user",""));
    }
    @Override
    public void OnSuccess(String data){
        EncryptAndDesencrypt encryptAndDesencrypt = new EncryptAndDesencrypt();
        try {
            if (passwordString.equals(encryptAndDesencrypt.decrypt(data))){
                Intent IntentToMainMenu = new Intent(this, qa_main_menu.class);
                startActivity(IntentToMainMenu);
            } else {
                Toast.makeText(this, "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
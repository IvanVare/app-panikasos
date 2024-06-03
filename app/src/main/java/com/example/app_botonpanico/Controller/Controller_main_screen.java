package com.example.app_botonpanico.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.Interface.SigninCallback;
import com.example.app_botonpanico.Model.Model_sign_in;
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_main_screen extends AppCompatActivity implements SigninCallback {

    String phone_number_user, password_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                phone_number_user=sharedPreferences.getString("phone_number_user","");
                password_user=sharedPreferences.getString("password_user","");
                boolean sesion=sharedPreferences.getBoolean("sesion", false);
                if(sesion && !phone_number_user.isEmpty() && !password_user.isEmpty()){
                    Model_sign_in modelsignin = new Model_sign_in(phone_number_user, password_user, getApplicationContext(), Controller_main_screen.this);
                    modelsignin.validateUser();
                }else {
                    Intent intentToMainActivity = new Intent(getApplicationContext(), Controller_mainActivity.class);
                    startActivity(intentToMainActivity);
                    finish();
                }
            }
        },3000);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void OnSuccess(String[] data){
        EncryptAndDesencrypt encryptAndDesencrypt = new EncryptAndDesencrypt();

        try {
            if (data[0].equalsIgnoreCase("Datos vacios")){
                Intent intentToSignIn = new Intent(this, Controller_sign_in_user.class);
                startActivity(intentToSignIn);
                finish();
                Toast.makeText(this, "Cuenta no registrada", Toast.LENGTH_SHORT).show();
            }else {
                if (password_user.equals(encryptAndDesencrypt.decrypt(data[4]))){
                    Intent intentToMainMenu = new Intent(this, Controller_qa_main_menu.class);
                    intentToMainMenu.putExtra("first_name",data[0]);
                    intentToMainMenu.putExtra("last_name", data[1]);
                    intentToMainMenu.putExtra("phone_number", data[2]);
                    intentToMainMenu.putExtra("age", data[3]);
                    intentToMainMenu.putExtra("email", data[5]);
                    startActivity(intentToMainMenu);
                    finish();
                } else {
                    Intent intentToSignIn = new Intent(this, Controller_sign_in_user.class);
                    startActivity(intentToSignIn);
                    finish();
                    Toast.makeText(this, "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void OnFailure(String error) {
        try {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            Intent intentToMainActivity = new Intent(getApplicationContext(), Controller_mainActivity.class);
            startActivity(intentToMainActivity);
            finish();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
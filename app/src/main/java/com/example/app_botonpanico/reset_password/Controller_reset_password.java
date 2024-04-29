package com.example.app_botonpanico.reset_password;

import android.content.Intent;
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
import com.example.app_botonpanico.insert_new_password.Controller_insert_new_password;
import com.example.app_botonpanico.qa_main_menu;
import com.example.app_botonpanico.sign_in.Controller_sign_in_user;
import com.example.app_botonpanico.sign_in.Model_sign_in;
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_reset_password extends AppCompatActivity implements CheckDataRP{

    EditText InputPhoneNumber;
    Button SendCodeButton;
    String phonenumberString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        InputPhoneNumber=findViewById(R.id.InputPhoneNumber_activityResetPassword);
        SendCodeButton=findViewById(R.id.SendCode_button_activityResetpassword);
        SendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhoneNumber();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void checkPhoneNumber(){
        phonenumberString=InputPhoneNumber.getText().toString();
        if (!phonenumberString.isEmpty()){
            Model_reset_password modelResetPassword = new Model_reset_password(phonenumberString,this,this);
            modelResetPassword.validatePhoneNumber();
        }else {
            Toast.makeText(this,"Ingrese número telefonico",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void CheckPhoneNumber(String phoneNumber){
        try {
            if (phoneNumber.equals(phonenumberString)){
                Intent IntentToInsertNewPassword = new Intent(this, Controller_insert_new_password.class);
                startActivity(IntentToInsertNewPassword);
            } else {
                Toast.makeText(this, "No está registrado ese numero", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
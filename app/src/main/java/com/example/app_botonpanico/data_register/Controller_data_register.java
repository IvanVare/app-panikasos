package com.example.app_botonpanico.data_register;

import android.annotation.SuppressLint;
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
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_data_register extends AppCompatActivity {

    EditText InputFirstName,InputLastName,InputPhoneNumber,InputAge, InputPassword, InputConfirmPassword;
    Button DataRegisterButtom;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_register);
        InputFirstName=findViewById(R.id.InputFirstName_activityData_register);
        InputLastName=findViewById(R.id.InputLastName_activityData_register);
        InputPhoneNumber=findViewById(R.id.InputPhoneNumber_activityData_register);
        InputAge=findViewById(R.id.InputAge_activityData_register);
        InputPassword=findViewById(R.id.InputPassword_activityData_register);
        InputConfirmPassword=findViewById(R.id.InputConfirmPassword_activityData_register);
        DataRegisterButtom=findViewById(R.id.Register_button_activityData_register);
        Model_data_register modelDataRegister = new Model_data_register(this);
        EncryptAndDesencrypt encryptAndDesencrypt= new EncryptAndDesencrypt();
        DataRegisterButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encryptPassword = "";
                encryptPassword = encryptAndDesencrypt.encrypt(InputPassword.getText().toString());
                modelDataRegister.registerUser(
                        InputFirstName.getText().toString()
                        ,InputLastName.getText().toString()
                        ,InputPhoneNumber.getText().toString()
                        ,InputAge.getText().toString()
                        ,encryptPassword);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
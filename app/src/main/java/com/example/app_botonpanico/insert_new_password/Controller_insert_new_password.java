package com.example.app_botonpanico.insert_new_password;

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

public class Controller_insert_new_password extends AppCompatActivity {


    Button ResetPassword;
    EditText InputNewPassword, ConfirmInsertNewPassword;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_new_password);
        InputNewPassword=findViewById(R.id.InputNewPassword_activityInsertNewPassword);
        ConfirmInsertNewPassword=findViewById(R.id.ConfirmNewPassword_activityInsertNewPassword);
        ResetPassword= findViewById(R.id.ResetPassword_button_activityInsertNewPassword);
        Model_insert_new_password modelInsertNewPassword = new Model_insert_new_password(this);
        EncryptAndDesencrypt encryptAndDesencrypt= new EncryptAndDesencrypt();

        email = getIntent().getStringExtra("email");
        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InputNewPassword.getText().toString().equals(ConfirmInsertNewPassword.getText().toString())){
                    String encryptPassword = "";
                    encryptPassword = encryptAndDesencrypt.encrypt(InputNewPassword.getText().toString());
                    modelInsertNewPassword.resetPassword(email,encryptPassword);
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
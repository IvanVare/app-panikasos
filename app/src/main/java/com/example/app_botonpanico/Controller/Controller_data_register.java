package com.example.app_botonpanico.Controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.PatternsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.Custom_dialog_loading;
import com.example.app_botonpanico.Interface.DataRegisterCallback;
import com.example.app_botonpanico.Model.Model_reset_password;
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Model.Model_data_register;
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_data_register extends AppCompatActivity implements DataRegisterCallback {

    EditText InputFirstName,InputLastName,InputEmail,InputPhoneNumber,InputAge, InputPassword, InputConfirmPassword;
    Button DataRegisterButtom;

    String FirstName,LastName,Email,PhoneNumber,Age,Password;
    Model_data_register modelDataRegister = new Model_data_register(this);
    EncryptAndDesencrypt encryptAndDesencrypt= new EncryptAndDesencrypt();
    Custom_dialog_loading customDialogLoading = new Custom_dialog_loading(Controller_data_register.this);
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_register);
        InputEmail = findViewById(R.id.InputEmail_activityData_register);
        InputPhoneNumber=findViewById(R.id.InputPhoneNumber_activityData_register);
        InputPassword=findViewById(R.id.InputPassword_activityData_register);
        InputConfirmPassword=findViewById(R.id.InputConfirmPassword_activityData_register);
        DataRegisterButtom=findViewById(R.id.Register_button_activityData_register);
        Model_data_register modelDataRegister = new Model_data_register(this);
        EncryptAndDesencrypt encryptAndDesencrypt= new EncryptAndDesencrypt();
        Custom_dialog_loading customDialogLoading = new Custom_dialog_loading(Controller_data_register.this);

        Intent intentToDataRegister = getIntent();
        FirstName = intentToDataRegister.getStringExtra("first_name");
        LastName = intentToDataRegister.getStringExtra("last_name");
        Age = intentToDataRegister.getStringExtra("age");


        DataRegisterButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!InputEmail.getText().toString().isEmpty() &&  !InputPhoneNumber.getText().toString().isEmpty()
                        && !InputPassword.getText().toString().isEmpty() && !InputConfirmPassword.getText().toString().isEmpty()){
                    checkEmailAndPhoneNumberExist();
                }else {
                    Toast.makeText(Controller_data_register.this,"Campos vacíos",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private boolean validateData() {
        boolean[] result = {validateEmail(), validatePhoneNumber(),validatePassword(),validatePasswordConfirm()};
        for (boolean isValid : result) {
            if (!isValid) {
                return false;
            }
        }
        return true;
    }



    private boolean validateEmail() {
        String email = InputEmail.getText().toString();
        if (email.isEmpty()) {
            InputEmail.setError("Campo vacío");
            return false;
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            InputEmail.setError("Ingrese una dirección de correo valida");
            return false;
        } else {
            InputEmail.setError(null);
            return true;
        }
    }


    private boolean validatePhoneNumber() {
        String phoneNumber = InputPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty()) {
            InputPhoneNumber.setError("Campo vacío");
            return false;
        } else if (phoneNumber.length() < 6 || !phoneNumber.matches("[0-9]+")) {
            InputPhoneNumber.setError("Ingrese un número teléfonico valido");
            return false;
        } else {
            InputPhoneNumber.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        String password = InputPassword.getText().toString();
        if (password.isEmpty()) {
            InputPassword.setError("Campo vacío");
            return false;
        } else if (password.length() < 6 ) {
            InputPassword.setError("Ingrese una contraseña más segura");
            return false;
        } else {
            InputPassword.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        String password = InputPassword.getText().toString();
        String passwordConfirm = InputConfirmPassword.getText().toString();
        if (passwordConfirm.isEmpty()) {
            InputConfirmPassword.setError("Campo vacío");
            return false;
        } else if (!password.equals(passwordConfirm)) {
            InputPassword.setError("La contraseña no coincide");
            return false;
        } else {
            InputConfirmPassword.setError(null);
            return true;
        }
    }



    public void checkEmailAndPhoneNumberExist(){
        String emailString=InputEmail.getText().toString().trim();
        String phoneNumberString= InputPhoneNumber.getText().toString().trim();
        if (validateEmail()){
            Model_data_register modelDataRegister = new Model_data_register(emailString,phoneNumberString,this,this);
            modelDataRegister.validateEmail();
        }
    }


    @Override
    public void OnSuccess() {
        customDialogLoading.startLoadingDialog();
        if (validateData()){
            String encryptPassword = "";
            encryptPassword = encryptAndDesencrypt.encrypt(InputPassword.getText().toString());
            modelDataRegister.registerUser(
                    FirstName
                    ,LastName
                    ,InputEmail.getText().toString().trim()
                    ,InputPhoneNumber.getText().toString().trim()
                    ,Age
                    ,encryptPassword);
            customDialogLoading.dismissDialog();
        }else {
            Toast.makeText(Controller_data_register.this,"Cumplir parámetros",Toast.LENGTH_SHORT).show();
            customDialogLoading.dismissDialog();

        }

    }

    @Override
    public void OnFailure(String error) {

    }
}
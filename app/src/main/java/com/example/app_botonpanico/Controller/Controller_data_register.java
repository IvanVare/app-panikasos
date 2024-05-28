package com.example.app_botonpanico.Controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Model.Model_data_register;
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_data_register extends AppCompatActivity {

    EditText InputFirstName,InputLastName,InputEmail,InputPhoneNumber,InputAge, InputPassword, InputConfirmPassword;
    Button DataRegisterButtom;

    String FirstName,LastName,Email,PhoneNumber,Age,Password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_register);
        InputFirstName=findViewById(R.id.InputFirstName_activityData_register);
        InputLastName=findViewById(R.id.InputLastName_activityData_register);
        InputEmail = findViewById(R.id.InputEmail_activityData_register);
        InputPhoneNumber=findViewById(R.id.InputPhoneNumber_activityData_register);
        InputAge=findViewById(R.id.InputAge_activityData_register);
        InputPassword=findViewById(R.id.InputPassword_activityData_register);
        InputConfirmPassword=findViewById(R.id.InputConfirmPassword_activityData_register);
        DataRegisterButtom=findViewById(R.id.Register_button_activityData_register);
        Model_data_register modelDataRegister = new Model_data_register(this);
        EncryptAndDesencrypt encryptAndDesencrypt= new EncryptAndDesencrypt();
        Custom_dialog_loading customDialogLoading = new Custom_dialog_loading(Controller_data_register.this);

        DataRegisterButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!InputFirstName.getText().toString().isEmpty() &&  !InputLastName.getText().toString().isEmpty()
                        && !InputEmail.getText().toString().isEmpty() &&  !InputPhoneNumber.getText().toString().isEmpty()
                        && !InputAge.getText().toString().isEmpty() && !InputPassword.getText().toString().isEmpty()
                        && !InputConfirmPassword.getText().toString().isEmpty()){

                    customDialogLoading.startLoadingDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (validateData()){
                                String encryptPassword = "";
                                encryptPassword = encryptAndDesencrypt.encrypt(InputPassword.getText().toString());
                                modelDataRegister.registerUser(
                                        InputFirstName.getText().toString().trim()
                                        ,InputLastName.getText().toString().trim()
                                        ,InputEmail.getText().toString().trim()
                                        ,InputPhoneNumber.getText().toString().trim()
                                        ,InputAge.getText().toString().trim().trim()
                                        ,encryptPassword);
                            }else {
                                Toast.makeText(Controller_data_register.this,"Cumplir parámetros",Toast.LENGTH_SHORT).show();

                            }
                            customDialogLoading.dismissDialog();
                        }
                    },3000);


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
        boolean[] result = {validateFirstName(),validateLastName(),validateEmail(), validatePhoneNumber()
                ,validateAge(), validatePassword(),validatePasswordConfirm()};
        for (boolean isValid : result) {
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

    private boolean validateFirstName() {
        String firstName = InputFirstName.getText().toString();
        if (firstName.isEmpty()) {
            InputFirstName.setError("Campo vacío");
            return false;
        } else if (firstName.length() < 2 || !firstName.matches("[\\p{L} ]+")) {
            InputFirstName.setError("Ingrese un nombre(s) valido(s)");
            return false;
        } else {
            InputFirstName.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        String lastname = InputLastName.getText().toString();
        if (lastname.isEmpty()) {
            InputLastName.setError("Campo vacío");
            return false;
        } else if (lastname.length() < 3 || !lastname.matches("[\\p{L} ]+")) {
            InputLastName.setError("Ingrese un apellido(s) valido(s)");
            return false;
        } else {
            InputLastName.setError(null);
            return true;
        }
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

    private boolean validateAge() {
        String Age = InputAge.getText().toString();
        if (Age.isEmpty()) {
            InputAge.setError("Campo vacío");
            return false;
        } else if (!Age.matches("[0-9]+")) {
            InputAge.setError("Ingrese una edad valida");
            return false;
        } else {
            InputAge.setError(null);
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



}
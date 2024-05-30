package com.example.app_botonpanico.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.Custom_dialog_loading;
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Model.Model_sign_in;
import com.example.app_botonpanico.Interface.SigninCallback;
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_sign_in_user extends AppCompatActivity implements SigninCallback {

    EditText InputPhoneNumber, InputPassword;

    RelativeLayout ForgotPasswordButton,SignInButtonToMenu,RegisterButtonToLogIn;
    String phonenumberString, passwordString;
    Custom_dialog_loading customDialogLoading = new Custom_dialog_loading(Controller_sign_in_user.this);

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
        returnpreferences();
        ForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToResetPassword = new Intent(Controller_sign_in_user.this, Controller_reset_password.class);
                startActivity(intentToResetPassword);
            }
        });

        RegisterButtonToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToDataRegister = new Intent(Controller_sign_in_user.this, Controller_data_register_personaldata.class);
                startActivity(intentToDataRegister);
            }
        });

        SignInButtonToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogLoading.startLoadingDialog();
                Handler handler = new Handler();

                if (validateDataSignin()){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Signin(v);
                        }
                    },3000);

                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Signin(View view) {
        phonenumberString=InputPhoneNumber.getText().toString();
        passwordString=InputPassword.getText().toString();
        if (!phonenumberString.isEmpty() && !passwordString.isEmpty()){
            Model_sign_in modelsignin = new Model_sign_in(phonenumberString, passwordString,this,this);
            modelsignin.validateUser();
            saveSesion();
        }else {
            Toast.makeText(Controller_sign_in_user.this,"Numero telefonico o contraseña incorrectos",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void OnSuccess(String[] data){
        EncryptAndDesencrypt encryptAndDesencrypt = new EncryptAndDesencrypt();
        try {
            if (data[0].equalsIgnoreCase("Datos vacios")){
                customDialogLoading.dismissDialog();
                Toast.makeText(this, "Cuenta no registrada", Toast.LENGTH_SHORT).show();
            }else {
                if (passwordString.equals(encryptAndDesencrypt.decrypt(data[4]))){
                    Intent intentToMainMenu = new Intent(this, Controller_qa_main_menu.class);
                    intentToMainMenu.putExtra("first_name",data[0]);
                    intentToMainMenu.putExtra("last_name", data[1]);
                    intentToMainMenu.putExtra("phone_number", data[2]);
                    intentToMainMenu.putExtra("age", data[3]);
                    intentToMainMenu.putExtra("email", data[5]);
                    startActivity(intentToMainMenu);
                    customDialogLoading.dismissDialog();
                } else {
                    customDialogLoading.dismissDialog();
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
            System.out.println(error);
        } catch (Exception e) {
            System.out.println(e);
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
    private boolean validateDataSignin() {
        boolean[] result = { validatePhoneNumber(), validatePassword()};
        for (boolean isValid : result) {
            if (!isValid) {
                return false;
            }
        }
        return true;
    }
    private boolean validatePhoneNumber() {
        String phoneNumber = InputPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty()) {
            InputPhoneNumber.setError("Campo vacío");
            return false;
        } else if (!phoneNumber.matches("[0-9]+")) {
            InputPhoneNumber.setError("Ingresa un número teléfonico valido");
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
        } else {
            InputPassword.setError(null);
            return true;
        }
    }
}
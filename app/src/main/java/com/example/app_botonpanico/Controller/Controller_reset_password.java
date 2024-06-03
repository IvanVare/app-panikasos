package com.example.app_botonpanico.Controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.PatternsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.utils.Custom_dialog_loading;
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Model.Model_reset_password;
import com.example.app_botonpanico.Interface.ResetPasswordCallback;

public class Controller_reset_password extends AppCompatActivity implements ResetPasswordCallback {

    EditText InputEmail;
    RelativeLayout SendCodeButton;
    String emailString;
    Custom_dialog_loading customDialogLoading = new Custom_dialog_loading(Controller_reset_password.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        InputEmail=findViewById(R.id.InputEmail_activityReset_Password);
        SendCodeButton=findViewById(R.id.SendCode_button_activityResetpassword);
        SendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEmail()){
                    customDialogLoading.startLoadingDialog();
                    checkEmail();
                }

            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void checkEmail(){
        emailString=InputEmail.getText().toString().trim();
        Model_reset_password modelResetPassword = new Model_reset_password(emailString,this,this);
        modelResetPassword.validateEmail();

    }

    public void OnSuccess(String email){
        try {
            if (email.equals(emailString)){
                String codeVerification = generateVerificationCode();
                Model_reset_password modelResetPassword = new Model_reset_password(email,codeVerification,this,this);
                modelResetPassword.sendEmailCode();
                customDialogLoading.dismissDialog();
                //Actividty para Confirmar codigo recibido
                Dialog dialog= new Dialog(Controller_reset_password.this);
                dialog.setTitle("Nuevo registro");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.activity_reset_password_code);
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                final EditText editTextNumber_01=dialog.findViewById(R.id.editTextNumber_01_activityResetpasswordCode);
                final EditText editTextNumber_02=dialog.findViewById(R.id.editTextNumber_02_activityResetpasswordCode);
                final EditText editTextNumber_03=dialog.findViewById(R.id.editTextNumber_03_activityResetpasswordCode);
                final EditText editTextNumber_04=dialog.findViewById(R.id.editTextNumber_04_activityResetpasswordCode);
                final EditText editTextNumber_05=dialog.findViewById(R.id.editTextNumber_05_activityResetpasswordCode);
                final EditText editTextNumber_06=dialog.findViewById(R.id.editTextNumber_06_activityResetpasswordCode);
                RelativeLayout verifyCodeButton=dialog.findViewById(R.id.VerifyCode_button_activityResetpasswordCode);

                editTextNumber_01.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_02.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_02.requestFocus();
                        }
                    }
                });
                editTextNumber_02.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_03.requestFocus();
                        }else {
                            editTextNumber_01.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_03.requestFocus();
                        }else {
                            editTextNumber_01.requestFocus();
                        }
                    }
                });
                editTextNumber_03.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_04.requestFocus();
                        }else {
                            editTextNumber_02.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_04.requestFocus();
                        }else {
                            editTextNumber_02.requestFocus();
                        }
                    }
                });
                editTextNumber_04.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_05.requestFocus();
                        }else {
                            editTextNumber_03.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_05.requestFocus();
                        }else {
                            editTextNumber_03.requestFocus();
                        }
                    }
                });
                editTextNumber_05.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_06.requestFocus();
                        }else {
                            editTextNumber_04.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().trim().isEmpty()){
                            editTextNumber_06.requestFocus();
                        }else {
                            editTextNumber_04.requestFocus();
                        }
                    }
                });
                editTextNumber_06.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().trim().isEmpty()){
                            editTextNumber_05.requestFocus();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().trim().isEmpty()){
                            editTextNumber_05.requestFocus();
                        }
                    }
                });
                verifyCodeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editTextNumber_01.getText().toString().trim().isEmpty()
                                || editTextNumber_02.getText().toString().trim().isEmpty()
                                || editTextNumber_03.getText().toString().trim().isEmpty()
                                || editTextNumber_04.getText().toString().trim().isEmpty()
                                || editTextNumber_05.getText().toString().trim().isEmpty()
                                || editTextNumber_06.getText().toString().trim().isEmpty()){
                            Toast.makeText(Controller_reset_password.this,"Insertar codigo completo",Toast.LENGTH_SHORT).show();

                        }else {
                            String inputCode = editTextNumber_01.getText().toString().trim()
                                    + editTextNumber_02.getText().toString().trim()
                                    + editTextNumber_03.getText().toString().trim()
                                    + editTextNumber_04.getText().toString().trim()
                                    + editTextNumber_05.getText().toString().trim()
                                    + editTextNumber_06.getText().toString().trim();

                            if (inputCode.equals(codeVerification)){
                                Intent IntentToInsertNewPassword = new Intent(Controller_reset_password.this, Controller_insert_new_password.class);
                                IntentToInsertNewPassword.putExtra("email",email);
                                startActivity(IntentToInsertNewPassword);
                                finish();
                            }else {
                                Toast.makeText(Controller_reset_password.this,"Codigo incorrecto",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });

            } else {
                Toast.makeText(this, "Correo no registrado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public void OnFailure(String error) {
        try {
            customDialogLoading.dismissDialog();
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
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

}
package com.example.app_botonpanico.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.utils.Custom_dialog_loading;
import com.example.app_botonpanico.Interface.InsertNewPasswordCallback;
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.Model.Model_insert_new_password;
import com.example.app_botonpanico.utils.EncryptAndDesencrypt;

public class Controller_insert_new_password extends AppCompatActivity implements InsertNewPasswordCallback {


    RelativeLayout ResetPassword;
    EditText InputNewPassword, ConfirmInsertNewPassword;
    String email;

    Model_insert_new_password modelInsertNewPassword = new Model_insert_new_password(this,this );
    EncryptAndDesencrypt encryptAndDesencrypt= new EncryptAndDesencrypt();
    Custom_dialog_loading customDialogLoading = new Custom_dialog_loading(Controller_insert_new_password.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_new_password);
        InputNewPassword=findViewById(R.id.InputNewPassword_activityInsertNewPassword);
        ConfirmInsertNewPassword=findViewById(R.id.ConfirmNewPassword_activityInsertNewPassword);
        ResetPassword= findViewById(R.id.ResetPassword_button_activityInsertNewPassword);


        email = getIntent().getStringExtra("email");
        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateDataPassword()) {
                    customDialogLoading.startLoadingDialog();
                    String encryptPassword = "";
                    encryptPassword = encryptAndDesencrypt.encrypt(InputNewPassword.getText().toString());
                    modelInsertNewPassword.resetPassword(email, encryptPassword);
                }else {
                    Toast.makeText(Controller_insert_new_password.this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private boolean validateDataPassword() {
        boolean[] result = {validatePassword(),validatePasswordConfirm()};
        for (boolean isValid : result) {
            if (!isValid) {
                return false;
            }
        }
        return true;
    }


    private boolean validatePassword() {
        String newPassword = InputNewPassword.getText().toString();
        if (newPassword.isEmpty()) {
            InputNewPassword.setError("Campo vacío");
            return false;
        } else if (newPassword.length() < 6 ) {
            InputNewPassword.setError("Ingrese una contraseña más segura");
            return false;
        } else {
            InputNewPassword.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        String newPassword = InputNewPassword.getText().toString();
        String newPasswordConfirm = ConfirmInsertNewPassword.getText().toString();
        if (newPasswordConfirm.isEmpty()) {
            ConfirmInsertNewPassword.setError("Campo vacío");
            return false;
        } else if (!newPassword.equals(newPasswordConfirm)) {
            ConfirmInsertNewPassword.setError("La contraseña no coincide");
            return false;
        } else {
            ConfirmInsertNewPassword.setError(null);
            return true;
        }
    }

    @Override
    public void OnSuccess(String res) {
        finish();
        customDialogLoading.dismissDialog();
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
}
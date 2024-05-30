package com.example.app_botonpanico.Controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.R;

public class Controller_reset_password_code extends AppCompatActivity {

    EditText editTextNumber_01, editTextNumber_02, editTextNumber_03, editTextNumber_04, editTextNumber_05, editTextNumber_06;
    RelativeLayout verifyCodeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password_code);
        verifyCodeButton=findViewById(R.id.VerifyCode_button_activityResetpasswordCode);

        //TextView textViewMobile= findViewById(R.id.TextView_activityResetpasswordCode);
        editTextNumber_01=findViewById(R.id.editTextNumber_01_activityResetpasswordCode);
        editTextNumber_02=findViewById(R.id.editTextNumber_02_activityResetpasswordCode);
        editTextNumber_03=findViewById(R.id.editTextNumber_03_activityResetpasswordCode);
        editTextNumber_04=findViewById(R.id.editTextNumber_04_activityResetpasswordCode);
        editTextNumber_05=findViewById(R.id.editTextNumber_05_activityResetpasswordCode);
        editTextNumber_06=findViewById(R.id.editTextNumber_06_activityResetpasswordCode);

        /*textViewMobile.setText(String.format("correo",getIntent().getStringExtra("email")
        ));*/

        setupOtpInputs();

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAnyFieldEmpty()){
                    Toast.makeText(Controller_reset_password_code.this,"Insertar codigo completo",Toast.LENGTH_SHORT).show();

                }else {
                    String code = getCodeFromFields();
                    Toast.makeText(Controller_reset_password_code.this,code,Toast.LENGTH_SHORT).show();

                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupOtpInputs(){
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

            }
        });

    }

    private boolean isAnyFieldEmpty() {
        return editTextNumber_01.getText().toString().trim().isEmpty()
                || editTextNumber_02.getText().toString().trim().isEmpty()
                || editTextNumber_03.getText().toString().trim().isEmpty()
                || editTextNumber_04.getText().toString().trim().isEmpty()
                || editTextNumber_05.getText().toString().trim().isEmpty()
                || editTextNumber_06.getText().toString().trim().isEmpty();
    }

    private String getCodeFromFields() {
        return editTextNumber_01.getText().toString().trim()
                + editTextNumber_02.getText().toString().trim()
                + editTextNumber_03.getText().toString().trim()
                + editTextNumber_04.getText().toString().trim()
                + editTextNumber_05.getText().toString().trim()
                + editTextNumber_06.getText().toString().trim();
    }
}
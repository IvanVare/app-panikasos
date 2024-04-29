package com.example.app_botonpanico.reset_password;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.R;

public class reset_password_code extends AppCompatActivity {

    EditText editTextNumber_01, editTextNumber_02, editTextNumber_03, editTextNumber_04, editTextNumber_05, editTextNumber_06;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password_code);

        TextView textViewMobile= findViewById(R.id.TextView_activityResetpasswordCode);
        editTextNumber_01=findViewById(R.id.editTextNumber_01_activityResetpasswordCode);
        editTextNumber_02=findViewById(R.id.editTextNumber_02_activityResetpasswordCode);
        editTextNumber_03=findViewById(R.id.editTextNumber_03_activityResetpasswordCode);
        editTextNumber_04=findViewById(R.id.editTextNumber_04_activityResetpasswordCode);
        editTextNumber_05=findViewById(R.id.editTextNumber_05_activityResetpasswordCode);
        editTextNumber_06=findViewById(R.id.editTextNumber_06_activityResetpasswordCode);

        textViewMobile.setText(String.format(
                "+51-%s",getIntent().getStringExtra("mobile")
        ));

        setupOtpInputs();
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
}
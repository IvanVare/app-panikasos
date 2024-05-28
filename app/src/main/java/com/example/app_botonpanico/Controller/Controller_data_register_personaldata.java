package com.example.app_botonpanico.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class Controller_data_register_personaldata extends AppCompatActivity {

    Button NextDataRegisterPD;
    EditText InputFirstName,InputLastName,InputEmail,InputPhoneNumber,InputAge, InputPassword, InputConfirmPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_register_personaldata);
        InputFirstName=findViewById(R.id.InputFirstName_activityData_register_personaldata);
        InputLastName=findViewById(R.id.InputLastName_activityData_register_personaldata);
        InputAge=findViewById(R.id.InputAge_activityData_register_personaldata);
        NextDataRegisterPD=findViewById(R.id.next_button_activityData_register_personaldata);


        NextDataRegisterPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!InputFirstName.getText().toString().isEmpty() &&  !InputLastName.getText().toString().isEmpty()
                        && !InputAge.getText().toString().isEmpty()){
                    if (validateData()){
                        Intent intentToDataRegister = new Intent(Controller_data_register_personaldata.this, Controller_data_register.class);
                        intentToDataRegister.putExtra("first_name", InputFirstName.getText().toString());
                        intentToDataRegister.putExtra("last_name", InputLastName.getText().toString());
                        intentToDataRegister.putExtra("age", InputAge.getText().toString());
                        startActivity(intentToDataRegister);
                    }

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
        boolean[] result = {validateFirstName(),validateLastName(),validateAge()};
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
}
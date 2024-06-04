package com.example.app_botonpanico.Controller;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.PatternsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.Model.Model_Contact_data;
import com.example.app_botonpanico.R;
import com.example.app_botonpanico.AdapterContact;
import com.example.app_botonpanico.Dao.daoContact;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Controller_qa_contacts extends AppCompatActivity {

    RelativeLayout mainMenuButtom;
    com.example.app_botonpanico.Dao.daoContact daoContact;
    AdapterContact adapterContact;
    Model_Contact_data contactData;
    ArrayList<Model_Contact_data> list;
    FloatingActionButton floatingActionsMenu_buttom;

    String firstNameContact,lastNameContact,nickNameContact,emailContact,phoneNumberContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_contacts);

        //Establecer lista de contactos
        daoContact = new daoContact(this);
        list=daoContact.getAll();
        adapterContact= new AdapterContact(this,list,daoContact);
        ListView listViewContacts= findViewById(R.id.ListView_activityQaContacts);
        listViewContacts.setAdapter(adapterContact);

        //botones
        mainMenuButtom=findViewById(R.id.Menu_activityQaContacts);
        floatingActionsMenu_buttom= findViewById(R.id.GroupButton_FloatingButton_activityQaContacts);

        mainMenuButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        floatingActionsMenu_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(Controller_qa_contacts.this);
                dialog.setTitle("Nuevo registro");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.activity_qa_contact);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                final TextView actionText = dialog.findViewById(R.id.ActionText_TextView_activityQaContact);
                final EditText firstName = dialog.findViewById(R.id.firstName_activityQaContact);
                final EditText lastName = dialog.findViewById(R.id.lastName_activityQaContact);
                final EditText nickName = dialog.findViewById(R.id.nickName_activityQaContact);
                final EditText email = dialog.findViewById(R.id.email_activityQaContact);
                final EditText phoneNumber = dialog.findViewById(R.id.phoneNumber_activityQaContact);
                Button saveButtom = dialog.findViewById(R.id.Save_Buttom_activityQaContact);
                Button cancelButtom = dialog.findViewById(R.id.Cancel_Buttom_activityQaContact);


                actionText.setText("Nuevo registro");
                saveButtom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            firstNameContact=firstName.getText().toString().trim();
                            lastNameContact=lastName.getText().toString().trim();
                            nickNameContact=nickName.getText().toString().trim();
                            emailContact=email.getText().toString().trim();
                            phoneNumberContact=phoneNumber.getText().toString().trim();


                            if (validateFirstName(firstNameContact,firstName) && validateLastName( lastNameContact, lastName)
                                    && validateEmail( emailContact, email) && validatePhoneNumber(phoneNumberContact, phoneNumber)){

                                contactData = new Model_Contact_data(firstNameContact
                                        ,lastNameContact,nickNameContact,emailContact,phoneNumberContact);
                                daoContact.insert(contactData);
                                list=daoContact.getAll();
                                adapterContact.notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        }catch (Exception e){
                            Toast.makeText(getApplication(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancelButtom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateFirstName(String firstNameContact, EditText firstName) {
        if (firstNameContact.isEmpty()) {
            firstName.setError("Campo vacío");
            return false;
        } else if (firstNameContact.length() < 3 || !firstNameContact.matches("[\\p{L} ]+")) {
            firstName.setError("Ingrese un nombre(s) valido(s)");
            return false;
        } else {
            firstName.setError(null);
            return true;
        }
    }
    private boolean validateLastName(String lastNameContact, EditText lastName) {
        if (lastNameContact.isEmpty()) {
            lastName.setError("Campo vacío");
            return false;
        } else if (lastNameContact.length() < 3 || !lastNameContact.matches("[\\p{L} ]+")) {
            lastName.setError("Ingrese un apellido(s) valido(s)");
            return false;
        } else {
            lastName.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String emailContact, EditText email) {
        if (emailContact.isEmpty()) {
            email.setError("Campo vacío");
            return false;
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(emailContact).matches()) {
            email.setError("Ingrese una dirección de correo valida");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }


    private boolean validatePhoneNumber(String phoneNumberContact,EditText phoneNumber) {
        if (phoneNumberContact.isEmpty()) {
            phoneNumber.setError("Campo vacío");
            return false;
        } else if (phoneNumberContact.length() < 6 || !phoneNumberContact.matches("[0-9]+")) {
            phoneNumber.setError("Ingrese un número teléfonico valido");
            return false;
        } else {
            phoneNumber.setError(null);
            return true;
        }
    }









}
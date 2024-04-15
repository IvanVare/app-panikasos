package com.example.app_botonpanico;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_botonpanico.contacts.AdapterContact;
import com.example.app_botonpanico.contacts.Contact_data;
import com.example.app_botonpanico.contacts.daoContact;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class qa_contacts extends AppCompatActivity {
    daoContact daoContact;
    AdapterContact adapterContact;
    Contact_data contactData;
    ArrayList<Contact_data> list;
    Button floatingActionsMenu_buttom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qa_contacts);
        daoContact = new daoContact(this);
        list=daoContact.getAll();
        adapterContact= new AdapterContact(this,list,daoContact);
        ListView listViewContacts= findViewById(R.id.ListView_activityQaContacts);
        listViewContacts.setAdapter(adapterContact);
        floatingActionsMenu_buttom= findViewById(R.id.GroupButton_FloatingButton_activityQaContacts);
        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        floatingActionsMenu_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(qa_contacts.this);
                dialog.setTitle("Nuevo registro");
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.activity_qa_contact);

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

                final EditText firstName = dialog.findViewById(R.id.firstName_activityQaContact);
                final EditText lastName = dialog.findViewById(R.id.lastName_activityQaContact);
                final EditText nickName = dialog.findViewById(R.id.nickName_activityQaContact);
                final EditText phoneNumber = dialog.findViewById(R.id.phoneNumber_activityQaContact);
                Button saveButtom = dialog.findViewById(R.id.Save_Buttom_activityQaContact);
                Button cancelButtom = dialog.findViewById(R.id.Cancel_Buttom_activityQaContact);

                saveButtom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            contactData = new Contact_data(firstName.getText().toString()
                                    ,lastName.getText().toString()
                                    ,nickName.getText().toString()
                                    ,phoneNumber.getText().toString());
                            daoContact.insert(contactData);
                            list=daoContact.getAll();
                            adapterContact.notifyDataSetChanged();
                            dialog.dismiss();
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
}
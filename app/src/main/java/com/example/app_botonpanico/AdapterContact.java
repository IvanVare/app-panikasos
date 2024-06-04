package com.example.app_botonpanico;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.PatternsCompat;

import com.example.app_botonpanico.Model.Model_contact_data;
import com.example.app_botonpanico.Dao.daoContact;

import java.util.ArrayList;

public class AdapterContact extends BaseAdapter {
    ArrayList<Model_contact_data> list;
    com.example.app_botonpanico.Dao.daoContact daoContact;
    Model_contact_data contactData;
    Activity activity;
    String firstNameContact,lastNameContact,nickNameContact,emailContact,phoneNumberContact;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id=0;

    public AdapterContact(Activity activity, ArrayList<Model_contact_data> list, daoContact daoContact) {
        this.list = list;
        this.activity = activity;
        this.daoContact = daoContact;
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        contactData=list.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        contactData=list.get(i);
        return contactData.getId();
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {
        View view= convertView;
        if (view==null){
            LayoutInflater layoutInflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(R.layout.item_contact,null);
        }
        contactData=list.get(posicion);
        TextView firstName=(TextView)view.findViewById(R.id.firstName_itemContact);
        TextView lastName=(TextView)view.findViewById(R.id.lastName_itemContact);
        TextView nickName=(TextView)view.findViewById(R.id.nickName_itemContact);
        TextView email=(TextView)view.findViewById(R.id.email_itemContact);
        TextView phoneNumber=(TextView)view.findViewById(R.id.phoneNumber_itemContact);
        RelativeLayout editButtom=(RelativeLayout) view.findViewById(R.id.editButtom_itemContact);
        RelativeLayout deleteButtom=(RelativeLayout) view.findViewById(R.id.deleteButtom_itemContact);
        firstName.setText(contactData.getFirst_name());
        lastName.setText(contactData.getLast_name());
        nickName.setText(contactData.getNickname());
        email.setText(contactData.getEmail());
        phoneNumber.setText(contactData.getPhone_number());
        editButtom.setTag(posicion);
        deleteButtom.setTag(posicion);

        editButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position= Integer.parseInt(v.getTag().toString());
                Dialog dialog= new Dialog(activity);dialog.setTitle("Editar registro");dialog.setCancelable(true);
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
                contactData=list.get(position);
                setId(contactData.getId());
                actionText.setText("Editar registro");
                firstName.setText(contactData.getFirst_name());
                lastName.setText(contactData.getLast_name());
                nickName.setText(contactData.getNickname());
                email.setText(contactData.getEmail());
                phoneNumber.setText(contactData.getPhone_number());
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
                                    contactData = new Model_contact_data(getId()
                                            ,firstNameContact
                                            ,lastNameContact
                                            ,nickNameContact
                                            ,emailContact
                                            ,phoneNumberContact);
                                    daoContact.update(contactData);
                                    list=daoContact.getAll();
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            }catch (Exception e){
                                Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show();
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
        deleteButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position= Integer.parseInt(v.getTag().toString());
                contactData=list.get(position);
                setId(contactData.getId());
                AlertDialog.Builder deleteAlert = getDeleteAlert();
                deleteAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                deleteAlert.show();
            }
        });

        return view;
    }

    @NonNull
    private AlertDialog.Builder getDeleteAlert() {
        AlertDialog.Builder deleteAlert= new AlertDialog.Builder(activity);
        deleteAlert.setTitle("Seguro que deseas borrar el contacto");
        deleteAlert.setCancelable(false);
        deleteAlert.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                daoContact.delete(getId());
                list=daoContact.getAll();
                notifyDataSetChanged();
            }
        });
        return deleteAlert;
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

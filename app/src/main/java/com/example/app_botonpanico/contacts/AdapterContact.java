package com.example.app_botonpanico.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.app_botonpanico.R;
import com.example.app_botonpanico.qa_contacts;

import java.util.ArrayList;

public class AdapterContact extends BaseAdapter {
    ArrayList<Contact_data> list;
    daoContact daoContact;
    Contact_data contactData;
    Activity activity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id=0;

    public AdapterContact(Activity activity,ArrayList<Contact_data> list, daoContact daoContact) {
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
        TextView phoneNumber=(TextView)view.findViewById(R.id.phoneNumber_itemContact);
        ImageButton editButtom=(ImageButton) view.findViewById(R.id.editButtom_itemContact);
        ImageButton deleteButtom=(ImageButton) view.findViewById(R.id.deleteButtom_itemContact);
        firstName.setText(contactData.getFirst_name());
        lastName.setText(contactData.getLast_name());
        nickName.setText(contactData.getNickname());
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
                final EditText firstName = dialog.findViewById(R.id.firstName_activityQaContact);
                final EditText lastName = dialog.findViewById(R.id.lastName_activityQaContact);
                final EditText nickName = dialog.findViewById(R.id.nickName_activityQaContact);
                final EditText phoneNumber = dialog.findViewById(R.id.phoneNumber_activityQaContact);
                Button saveButtom = dialog.findViewById(R.id.Save_Buttom_activityQaContact);
                Button cancelButtom = dialog.findViewById(R.id.Cancel_Buttom_activityQaContact);
                contactData=list.get(position);
                setId(contactData.getId());
                firstName.setText(contactData.getFirst_name());
                lastName.setText(contactData.getLast_name());
                nickName.setText(contactData.getNickname());
                phoneNumber.setText(contactData.getPhone_number());
                    saveButtom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                contactData = new Contact_data(getId(),firstName.getText().toString()
                                        ,lastName.getText().toString()
                                        ,nickName.getText().toString()
                                        ,phoneNumber.getText().toString());
                                daoContact.update(contactData);
                                list=daoContact.getAll();
                                notifyDataSetChanged();
                                dialog.dismiss();
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
}

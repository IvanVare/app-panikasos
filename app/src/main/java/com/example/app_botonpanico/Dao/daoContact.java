package com.example.app_botonpanico.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_botonpanico.Model.Model_Contact_data;

import java.util.ArrayList;

public class daoContact {
    SQLiteDatabase db;
    ArrayList<Model_Contact_data> list=new ArrayList<Model_Contact_data>();
    Model_Contact_data contactData;
    Context context;
    String DataBaseName = "DBContacts";
    //Creación de base de datos
    String table = "create table if not exists contact(id integer primary key autoincrement, " +
            "first_name text, last_name text, nickname text, email text, phone_number text)";

    public daoContact(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase(DataBaseName, Context.MODE_PRIVATE, null);
        db.execSQL(table);
    }
    public boolean insert(Model_Contact_data contactData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", contactData.getFirst_name());
        contentValues.put("last_name", contactData.getLast_name());
        contentValues.put("nickname", contactData.getNickname());
        contentValues.put("email", contactData.getEmail());
        contentValues.put("phone_number", contactData.getPhone_number());
        return (db.insert("contact",null,contentValues))>0;
    }

    public boolean delete(int id) {
        return (db.delete("contact","id="+id,null))>0;

    }

    public boolean update(Model_Contact_data contactData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", contactData.getFirst_name());
        contentValues.put("last_name", contactData.getLast_name());
        contentValues.put("nickname", contactData.getNickname());
        contentValues.put("email", contactData.getEmail());
        contentValues.put("phone_number", contactData.getPhone_number());
        return (db.update("contact",contentValues,"id="+contactData.getId(),null))>0;
    }


    public ArrayList<Model_Contact_data> getAllFirstNameAndPhoneNumber() {
        list.clear();
        Cursor cursor = db.rawQuery("SELECT first_name, phone_number FROM contact", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(new Model_Contact_data(cursor.getInt(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public ArrayList<Model_Contact_data> getAllByEmail() {
        list.clear();
        Cursor cursor = db.rawQuery("SELECT first_name,last_name, email, phone_number FROM contact", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(new Model_Contact_data(
                        cursor.getString(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }


    public ArrayList<Model_Contact_data> getAll(){
        list.clear();
        Cursor cursor= db.rawQuery("select * from contact", null);

        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                list.add(new Model_Contact_data(cursor.getInt(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getString(4)
                        ,cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public Model_Contact_data findOne(int position){
        Cursor cursor= db.rawQuery("select * from contact", null);
        cursor.moveToPosition(position);
        contactData= new Model_Contact_data(cursor.getInt(0)
                ,cursor.getString(1)
                ,cursor.getString(2)
                ,cursor.getString(3)
                ,cursor.getString(4)
                ,cursor.getString(5));

        return contactData;
    }

}

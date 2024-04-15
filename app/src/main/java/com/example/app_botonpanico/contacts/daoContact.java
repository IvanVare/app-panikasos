package com.example.app_botonpanico.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class daoContact {
    SQLiteDatabase db;
    ArrayList<Contact_data> list=new ArrayList<Contact_data>();
    Contact_data contactData;
    Context context;
    String DataBaseName = "DBContacts";
    String table = "create table if not exists contact(id integer primary key autoincrement, " +
            "first_name text, last_name text, nickname text, phone_number text)";

    public daoContact(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase(DataBaseName, Context.MODE_PRIVATE, null);
        db.execSQL(table);

    }

    public boolean insert(Contact_data contactData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", contactData.getFirst_name());
        contentValues.put("last_name", contactData.getLast_name());
        contentValues.put("nickname", contactData.getNickname());
        contentValues.put("phone_number", contactData.getPhone_number());
        return (db.insert("contact",null,contentValues))>0;
    }

    public boolean delete(int id) {
        return (db.delete("contact","id="+id,null))>0;

    }

    public boolean update(Contact_data contactData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", contactData.getFirst_name());
        contentValues.put("last_name", contactData.getLast_name());
        contentValues.put("nickname", contactData.getNickname());
        contentValues.put("phone_number", contactData.getPhone_number());
        return (db.update("contact",contentValues,"id="+contactData.getId(),null))>0;
    }

    public ArrayList<Contact_data> getAll(){
        list.clear();
        Cursor cursor= db.rawQuery("select * from contact", null);

        if (cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                list.add(new Contact_data(cursor.getInt(0)
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,cursor.getString(4)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public Contact_data findOne(int position){
        Cursor cursor= db.rawQuery("select * from contact", null);
        cursor.moveToPosition(position);
        contactData= new Contact_data(cursor.getInt(0)
                ,cursor.getString(1)
                ,cursor.getString(2)
                ,cursor.getString(3)
                ,cursor.getString(4));

        return contactData;
    }

}

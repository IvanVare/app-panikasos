package com.example.app_botonpanico;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class Custom_dialog_loading {
    private Activity activity;
    private AlertDialog alertDialog;


    public Custom_dialog_loading(Activity myactivity){
        activity=myactivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading,null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void  dismissDialog(){
        alertDialog.dismiss();
    }
}

package com.example.app_botonpanico.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.app_botonpanico.Controller.Controller_qa_main_menu;

public class ServiceStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = intent.getBooleanExtra("isRunning", false);
        if (isRunning) {
            // Lógica cuando el servicio está corriendo.
            System.out.println("Service is running");
        } else {
            // Lógica cuando el servicio no está corriendo.
            System.out.println("Service is not running");        }
    }
}

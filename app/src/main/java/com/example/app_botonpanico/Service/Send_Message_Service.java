package com.example.app_botonpanico.Service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.app_botonpanico.Controller.Controller_qa_panic_button;
import com.example.app_botonpanico.Dao.daoContact;
import com.example.app_botonpanico.Model.Model_contact_data;
import com.example.app_botonpanico.Model.Model_send_message_coordinates;
import com.example.app_botonpanico.R;

import java.util.List;

public class Send_Message_Service extends Service {

    daoContact daoContact;
    private List<Model_contact_data> listContacts;
    String first_name_IntentUser, last_name_IntentUser, phone_number_IntentUser,email_IntentUser;
    private Handler handler;
    private Runnable runnable;
    private long startTime;
    private static final long INTERVAL = 60000; //300000;  5 minutes in milliseconds
    private static final long DURATION = 3600000; // 1 hour in milliseconds
    private boolean isSending = false; // Control variable
    private boolean isServiceRunning = false;
    private static final String CHANNEL_ID = "CHANNEL_ID_NOTIFICATION";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isSending) {
            daoContact = new daoContact(this);
            listContacts=daoContact.getAllByEmail();
            first_name_IntentUser = intent.getStringExtra("first_name");
            last_name_IntentUser = intent.getStringExtra("last_name");
            phone_number_IntentUser = intent.getStringExtra("phone_number");
            email_IntentUser = intent.getStringExtra("email");
            startTime= System.currentTimeMillis();
            startSendingMessages(first_name_IntentUser,last_name_IntentUser,phone_number_IntentUser,email_IntentUser);
            isSending = true;
            sendServiceStatus(true);

            showNotification();
        }else {
            stopSendingMessages();
            isSending = false;
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void startSendingMessages(String first_name_User,String last_name_User, String phone_number_User, String email_User ) {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - startTime < DURATION) {
                    for (Model_contact_data contact : listContacts) {
                        String[] coordinates = getCoordinates();
                        if (coordinates != null) {
                            String latitudeStr = coordinates[0];
                            String longitudeStr = coordinates[1];
                            dataMessage(contact.getFirst_name(), contact.getLast_name(), contact.getEmail(), first_name_User, last_name_User, email_User, phone_number_User, latitudeStr, longitudeStr);
                        } else {
                            Toast.makeText(Send_Message_Service.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (isSending) {
                        handler.postDelayed(this, INTERVAL);
                    }
                }else {
                    stopSelf();
                }
            }
        };
        handler.post(runnable);
    }
    private void dataMessage(String firs_name_contact,String last_name_contact, String email_contact
            , String myfirst_name,String mylast_name, String myemail, String myphone_number, String latitude, String longitude){
        Model_send_message_coordinates modelSendMessageCoordinates = new Model_send_message_coordinates(this);
        try {
            String url = "https://www.google.com/maps/?q=" + latitude + "," + longitude;
            modelSendMessageCoordinates.sendCoordinate(firs_name_contact,last_name_contact,email_contact,myfirst_name,mylast_name,myemail,myphone_number,url);
        }catch (Exception e){
            System.out.println(e);
            Toast.makeText(this,"No pudo enviar el correo",Toast.LENGTH_SHORT).show();
        }
    }
    private String[] getCoordinates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MessageService", "No location permissions granted");
            return null;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String latitudeStr = String.valueOf(latitude);
                    String longitudeStr = String.valueOf(longitude);
                    return new String[]{latitudeStr, longitudeStr};
                } else {
                    Log.e("MessageService", "No se pudo obtener la ubicación");
                    return null;
                }
            } else {
                Log.e("MessageService", "LocationManager is null");
                return null;
            }
        }
    }
    private void stopSendingMessages() {
        isSending = false;

        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            stopForeground(true);
            stopSelf();
            Toast.makeText(this, "Panika SOS Desactivado", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendServiceStatus(boolean isRunning) {
        Intent intent = new Intent("com.example.SERVICE_STATUS");
        intent.putExtra("isRunning", isRunning);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendServiceStatus(false);
        isServiceRunning = false;
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        stopForeground(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
        stopSelf();
        System.out.println("Notification-destroy");
    }




    private void createNotificationChannel() {
        // Crear el NotificationChannel, pero solo en API 26+ porque
        // el NotificationChannel es una nueva clase que no está en las bibliotecas de soporte
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for basic notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            // Registrar el canal con el sistema; no se puede cambiar la importancia o
            // otras características después de esto
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotification() {
        // Crear un intent para abrir la actividad cuando se toque la notificación
        Intent intent = new Intent(this, Controller_qa_panic_button.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_buttone) // Asegúrate de tener un icono en drawable
                .setContentTitle("Activado...")
                .setContentText("Panoka SOS está envíando su ubicación a tus contactos de emergencia")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(false);

        // Mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        System.out.println("Notification-activada");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

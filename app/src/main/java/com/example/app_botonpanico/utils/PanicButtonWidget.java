package com.example.app_botonpanico.utils;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.app_botonpanico.Controller.Controller_main_screen;
import com.example.app_botonpanico.R;

public class PanicButtonWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, Controller_main_screen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);
            views.setOnClickPendingIntent(R.id.buttomPanika_RelativeLayout_ActivityWidget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Aquí puedes manejar acciones cuando se agrega el primer widget a la pantalla
    }

    @Override
    public void onDisabled(Context context) {
        // Aquí puedes manejar acciones cuando se elimina el último widget de la pantalla
    }
}

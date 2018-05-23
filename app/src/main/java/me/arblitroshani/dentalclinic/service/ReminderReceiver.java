package me.arblitroshani.dentalclinic.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import me.arblitroshani.dentalclinic.activity.AppointmentsActivity;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, AppointmentsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AppointmentsActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        MyNotification notification = new MyNotification(pendingIntent, "You have an appointment in 1 hour!", ":)", context);
        notification.send(MyNotification.NOTIFICATION_REMIND);
    }
}
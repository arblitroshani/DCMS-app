package me.arblitroshani.dentalclinic.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import me.arblitroshani.dentalclinic.activity.AppointmentsActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
//        MyNotification notification = new MyNotification(
//                message.getNotification().getTitle(),
//                message.getNotification().getBody(),
//        this,
//                AppointmentsActivity.class);
//        notification.send(MyNotification.NOTIFICATION_CONFIRM);
        setReminder();
    }

    private void setReminder() {
        Intent notificationIntent = new Intent(this, ReminderReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
}
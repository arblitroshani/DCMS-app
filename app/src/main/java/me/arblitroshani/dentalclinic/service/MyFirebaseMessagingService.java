package me.arblitroshani.dentalclinic.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import me.arblitroshani.dentalclinic.activity.AppointmentsActivity;
import me.arblitroshani.dentalclinic.activity.SettingsActivity;
import me.arblitroshani.dentalclinic.extra.Config;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        if (message.getData().size() > 0) {
            MyNotification notification = new MyNotification(
                    message.getData().get("title"),
                    message.getData().get("body"),
                    this,
                    AppointmentsActivity.class);
            notification.send(MyNotification.NOTIFICATION_CONFIRM);
            setReminder(message.getData().get("time"));
        }
    }

    private void setReminder(String time) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isActiveReceiveReminder = prefs.getBoolean("notification_reminder", true);

        if (isActiveReceiveReminder) {
            int minutesBefore = Integer.parseInt(prefs.getString("reminder_before", "60"));
            long notificationTime = Long.parseLong(time);
            long reminderTime = notificationTime - (minutesBefore * 60 * 1000);

            long actualTime = Calendar.getInstance().getTimeInMillis();
            if (reminderTime > actualTime) {
                Intent notificationIntent = new Intent(this, ReminderReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, broadcast);
            }
        }
    }
}
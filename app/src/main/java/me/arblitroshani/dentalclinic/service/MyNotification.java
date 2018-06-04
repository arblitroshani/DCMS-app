package me.arblitroshani.dentalclinic.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import me.arblitroshani.dentalclinic.R;

public class MyNotification {

    public static final int NOTIFICATION_CONFIRM = 0;
    public static final int NOTIFICATION_REMIND = 1;

    private Context packageContext;
    private String channelId;
    private NotificationCompat.Builder notificationBuilder;

    public MyNotification(PendingIntent pendingIntent, String contentTitle, String contentText,
                          Context packageContext) {
        this.packageContext = packageContext;
        showNotification(pendingIntent, contentTitle, contentText);
    }

    public MyNotification(String contentTitle, String contentText,
            Context packageContext, Class destinationClass) {
        this.packageContext = packageContext;
        Intent intent = new Intent(packageContext, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(packageContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        showNotification(pendingIntent, contentTitle, contentText);
    }

    private void showNotification(PendingIntent pendingIntent, String contentTitle, String contentText) {
        channelId = packageContext.getString(R.string.default_notification_channel_id);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(packageContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_1)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);
    }

    public void send(int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) packageContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}

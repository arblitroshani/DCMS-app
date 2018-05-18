package me.arblitroshani.androidtest.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import me.arblitroshani.androidtest.R;

public class MyNotification {

    private Context packageContext;
    private String channelId;
    private NotificationCompat.Builder notificationBuilder;

    public MyNotification(String contentTitle, String contentText,
            Context packageContext, Class destinationClass) {
        this.packageContext = packageContext;
        Intent intent = new Intent(packageContext, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(packageContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        channelId = packageContext.getString(R.string.default_notification_channel_id);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder = new NotificationCompat.Builder(packageContext, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);
    }

    public void send() {
        NotificationManager notificationManager =
                (NotificationManager) packageContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}

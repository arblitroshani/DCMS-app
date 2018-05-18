package me.arblitroshani.androidtest.service;

import android.os.Bundle;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.arblitroshani.androidtest.activity.AppointmentsActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        MyNotification notification = new MyNotification(
                //"Your appointment is confirmed!",
                message.getNotification().getTitle(),
                message.getNotification().getBody(),
                this,
                AppointmentsActivity.class);
        notification.send();
        setReminder();
    }

    private void setReminder() {

    }
}
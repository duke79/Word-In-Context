package com.baliyaan.android.wordincontext;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Pulkit Singh on 2/6/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(getClass().getName(), "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(getClass().getName(), "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            /*Retrive message details*/
            String notifTitle = remoteMessage.getNotification().getTitle();
            String notifBody = remoteMessage.getNotification().getBody();

            Log.d(getClass().getName(), "Message Notification Body: " + notifTitle + " : " + notifBody);
            try {
                /*Build notification*/
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setContentTitle(notifTitle);
                builder.setContentText(notifBody);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                Notification notif = builder.build();

                /*Notify*/
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(0, notif);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}

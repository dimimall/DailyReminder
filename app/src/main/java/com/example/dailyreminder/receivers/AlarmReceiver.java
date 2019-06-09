package com.example.dailyreminder.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dailyreminder.activities.MainActivity;
import com.example.dailyreminder.adapters.MyAdapter;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    // Notification ID.
    private static int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    /**
     * Called when the BroadcastReceiver receives an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");

        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Deliver the notification.
        deliverNotification(context,title);
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(Context context,String title) {
        // Create the content intent for the notification, which launches
        // this activity

        Intent contentIntent = new Intent(context, MainActivity.class);

        //contentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NOTIFICATION_ID = (int) SystemClock.elapsedRealtime();

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent
                        .FLAG_UPDATE_CURRENT);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(title)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
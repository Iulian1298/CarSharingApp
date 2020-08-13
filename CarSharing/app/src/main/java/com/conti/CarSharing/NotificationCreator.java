package com.conti.CarSharing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static com.conti.CarSharing.NotificationReceiverActivity.ACTION_SNOOZE;
import static com.conti.CarSharing.NotificationReceiverActivity.EXTRA_NOTIFICATION_ID;


public class NotificationCreator {

    public static String TAG = "MainActivity";
    private static final String CHANNEL_ID = "CHANNEL_ID";

    // variable to hold context
    private Context ctx;

    //save the context received via constructor in a local variable
    public NotificationCreator(Context ctx) {
        this.ctx = ctx;
        createNotificationChannel();
//        Button buttonShowNotification = findViewById(R.id.show);
//
//        buttonShowNotification.setOnClickListener(v ->  {
//            sendNotification("test", 100);
//        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "channel";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            //channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String title, int notificationId) {

        // Create an explicit intent for an Activity in your app
        /* Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0); */

        Intent snoozeIntent = new Intent(ctx, NotificationReceiverActivity.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);

        Log.e(TAG, snoozeIntent.getExtras().toString());

        Log.e(TAG, "snoozeIntent id: " + snoozeIntent.getIntExtra(EXTRA_NOTIFICATION_ID, -1));

        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(ctx, notificationId, snoozeIntent, 0);


        //Intent notificationIntent =new Intent(ctx, NotificationReceiverActivity.class);
        //PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(ctx, 0,
        //notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                //.setLargeIcon(BitmapFactory.decodeResource(R.drawable.xyz))
                .setContentTitle(String.format("%s (id %d)", title, notificationId))
                .setContentText("Much longer text that cannot fit one line...")
                //.setVibrate(new long[]{0,300,300,300})
                // .setContentIntent(fullScreenPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                //.setPriority(NotificationCompat.PRIORITY_MAX)
                //.setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                //.setFullScreenIntent(fullScreenPendingIntent, true)
                //.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Add the action button
                .addAction(R.drawable.ic_launcher_foreground, "snooze",
                        snoozePendingIntent);


        //Notification buildNotification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

}


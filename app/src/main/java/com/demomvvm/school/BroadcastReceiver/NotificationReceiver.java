package com.demomvvm.school.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent notificationIntent = new Intent(context, BookingDetailsFragment.class);
//
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(TestActivity.class);
//        stackBuilder.addNextIntent(notificationIntent);
//
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//
//        Notification notification = builder.setContentTitle("Demo App Notification")
//                .setContentText("New Notification From Demo App..")
//                .setTicker("New Message Alert!")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent).build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notification);
    }
}
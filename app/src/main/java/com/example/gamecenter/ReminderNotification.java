package com.example.gamecenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("DEBUG DB", "onReceive: notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"AlarmManager");
        builder.setContentTitle("Game Center");
        builder.setContentText("Hey, It's been a long time since you played the game!");
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(2,builder.build());
    }
}

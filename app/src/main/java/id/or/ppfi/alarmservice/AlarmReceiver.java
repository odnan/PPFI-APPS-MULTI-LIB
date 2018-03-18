package id.or.ppfi.alarmservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import id.or.ppfi.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    MediaPlayer mp;
    private PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        mp=MediaPlayer.create(context, R.raw.sunny);
        mp.start();
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();

        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, AlarmMainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setTicker("IAM PRIMA")
                .setSmallIcon(R.drawable.icon_depan)
                .setContentTitle("Wellness Notification")
                .setContentText("Anda Belum Create Wellness!\nSilakan dibuat sekarang!")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
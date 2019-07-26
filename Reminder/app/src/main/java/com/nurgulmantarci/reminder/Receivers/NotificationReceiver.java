package com.nurgulmantarci.reminder.Receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.nurgulmantarci.reminder.MainActivity;
import com.nurgulmantarci.reminder.R;


@SuppressWarnings("deprecation")
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String Title= intent.getStringExtra(context.getString(R.string.alert_title));
        String content=intent.getStringExtra(context.getString(R.string.alert_content));

        Intent detayEkrani=new Intent(context, MainActivity.class);
        PendingIntent detayEkraniIntent= PendingIntent.getActivity(context,0,detayEkrani,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(Title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/crystal"))
                .setContentIntent(detayEkraniIntent);

        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify("bildirim",0,builder.build());

    }
}

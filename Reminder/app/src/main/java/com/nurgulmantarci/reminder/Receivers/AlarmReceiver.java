package com.nurgulmantarci.reminder.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nurgulmantarci.reminder.Alert;
import com.nurgulmantarci.reminder.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String _title=intent.getStringExtra(context.getString(R.string.alert_title));
        String _content=intent.getStringExtra(context.getString(R.string.alert_content));
        Intent intent2=new Intent(context, Alert.class);
        intent2.putExtra(context.getString(R.string.alert_title),_title);
        intent2.putExtra(context.getString(R.string.alert_content),_content);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent2);
    }
}

package com.nurgulmantarci.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nurgulmantarci.reminder.Entities.Music;
import com.nurgulmantarci.reminder.Receivers.AlarmReceiver;

import java.io.IOException;


public class Alert extends Activity implements View.OnClickListener {

    MediaPlayer mediaPlayer;
    TextView txtSmallText;
    String alarmTitle;
    String alarmContent;
    Context context = this;
    Button btnOk, btnDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        alarmTitle = getIntent().getExtras().getString(getString(R.string.alert_title));
        alarmContent = getIntent().getExtras().getString(getString(R.string.alert_content));
        txtSmallText = findViewById(R.id.txtSmallText);
        txtSmallText.setText(alarmTitle + "\n" + alarmContent);

        btnOk = findViewById(R.id.btnOk);
        btnDelay = findViewById(R.id.btnDelay);

        btnOk.setOnClickListener(this);
        btnDelay.setOnClickListener(this);

        if(Music.selectedId==5000){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.best);
        }else{
            Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,Music.selectedId);
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(context,contentUri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


            mediaPlayer.start();
            mediaPlayer.setLooping(true);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                Alert.this.finish();
                break;
            case R.id.btnDelay:
                reLoadAlarm();
                Toast.makeText(context, getString(R.string.delayMessage), Toast.LENGTH_SHORT).show();
                Alert.this.finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    private void reLoadAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(getString(R.string.alert_title), alarmTitle);
        intent.putExtra(getString(R.string.alert_content), alarmContent);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //Birinci Yöntem
//        long afterFiveMinutes = System.currentTimeMillis() + (5*60*1000);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, afterFiveMinutes, pendingIntent);

        //İkinci Yöntem
        long delayTime=SystemClock.elapsedRealtime()+5*60*1000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,delayTime,pendingIntent);

    }

}

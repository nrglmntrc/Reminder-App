package com.nurgulmantarci.reminder;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.nurgulmantarci.reminder.Adapters.CustomSpinnerAdapter;
import com.nurgulmantarci.reminder.Entities.Music;

import java.io.IOException;

public class SetMusic extends Activity {

    Spinner spinner;
    Context context=this;
    Button btnSave;
    MediaPlayer mediaPlayer=new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_music);

        btnSave=findViewById(R.id.btnSaveMusic);
        spinner=findViewById(R.id.spinnerMusic);

        ContentResolver contentResolver=getContentResolver();
        Uri uri= MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor cursor=contentResolver.query(uri,null,null,null,null);
        if(cursor==null){
            Toast.makeText(context, "Cursor hatası", Toast.LENGTH_SHORT).show();
        }else if(!cursor.moveToFirst()){
            Toast.makeText(context, "No media on the device", Toast.LENGTH_SHORT).show();
        }else{
            int lenght=cursor.getCount();
            Music[] musicList=new Music[lenght];
                do{
                    musicList[cursor.getPosition()]=new Music();
                    musicList[cursor.getPosition()].setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    musicList[cursor.getPosition()].setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                }while (cursor.moveToNext());

            final CustomSpinnerAdapter adapter=new CustomSpinnerAdapter(context,android.R.layout.simple_spinner_item,musicList);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    Music music=adapter.getItem(position); // ***
                    Uri contentUri= ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,music.getId());
                    play(context,contentUri);

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    private void play(Context _context, Uri contentUri) {
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(_context,contentUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

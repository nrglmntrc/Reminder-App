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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class SetMusic extends Activity {

    Spinner spinner;
    Context context=this;
    private ArrayAdapter<String> dataAdapterForMucis;
    private ArrayList<String> titles;
    Button btnSave;
    MediaPlayer mediaPlayer=new MediaPlayer();
    long thisId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_music);

        btnSave=findViewById(R.id.btnSaveMusic);

        titles=new ArrayList<>();

        ContentResolver contentResolver=getContentResolver();
        Uri uri= MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor cursor=contentResolver.query(uri,null,null,null,null);
        if(cursor==null){
            Toast.makeText(context, "Cursor hatası", Toast.LENGTH_SHORT).show();
        }else if(!cursor.moveToFirst()){
            Toast.makeText(context, "No media on the device", Toast.LENGTH_SHORT).show();
        }else{

                int titleColumn=cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int idColumn=cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                do{
                    thisId=cursor.getLong(idColumn);
                    String thisString=cursor.getString(titleColumn);
                    titles.add(thisString);
                }while (cursor.moveToNext());

        }


        if(titles!=null){
            spinner=findViewById(R.id.spinnerMusic);
            dataAdapterForMucis=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,titles);
            dataAdapterForMucis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapterForMucis);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, position+" Seçildi.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}

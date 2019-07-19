package com.nurgulmantarci.reminder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper myDbHelper;
    ListView listView;
    FloatingActionButton fab;
    Context context=this;

    //TODO TASARIM: ListView Tasarımı daha profesyonel yapabilirsin, tarihi geçen işlerin üzeri çizilmeli.

    //TODO MEDİAPLAYER: alarm sesi tekrarlı çalmalı, alarm arka planda çıkan activity olmamalı,(YAPILDI)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        listView=findViewById(R.id.commentlist);
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CreateNote.class);
                startActivity(intent);
            }
        });

        myDbHelper=new DbHelper(context);
        db=myDbHelper.getWritableDatabase();

        final String[] column={myDbHelper.C_ID,myDbHelper.TITLE,myDbHelper.DETAIL,myDbHelper.TYPE,myDbHelper.TIME,myDbHelper.DATE};
        final Cursor cursor=db.query(myDbHelper.TABLE_NAME,column,null,null,null,null,null);

        String[] from={myDbHelper.TITLE,myDbHelper.DETAIL,myDbHelper.TYPE,myDbHelper.TIME,myDbHelper.DATE};
        int[] to ={R.id.title, R.id.Detail, R.id.type, R.id.time, R.id.date};
        final SimpleCursorAdapter cursorAdapter=new SimpleCursorAdapter(context, R.layout.list_entry,cursor,from,to, 0);

        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, ViewNote.class);
                intent.putExtra(getString(R.string.row_id),id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_new:
                Intent intent=new Intent(context, CreateNote.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
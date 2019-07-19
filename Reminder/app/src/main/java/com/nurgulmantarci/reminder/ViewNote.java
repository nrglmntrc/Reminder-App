package com.nurgulmantarci.reminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewNote extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));

        final long row_id= getIntent().getExtras().getLong(getString(R.string.row_id));

        dbHelper=new DbHelper(context);
        db=dbHelper.getWritableDatabase();

        String sql="SELECT * FROM " + dbHelper.TABLE_NAME + " WHERE " + dbHelper.C_ID+"="+row_id;

        TextView txtTitle=findViewById(R.id.title);
        TextView txtDetail=findViewById(R.id.detail);
        TextView txtType=findViewById(R.id.note_type_ans);
        TextView txtTime=findViewById(R.id.alertvalue);
        TextView txtDate=findViewById(R.id.datevalue);

        Cursor cursor= db.rawQuery(sql,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                txtTitle.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                txtDetail.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DETAIL)));
                txtType.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)));
                txtTime.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TIME)));
                txtDate.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DATE)));
            }
            cursor.close();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_note,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long note_id=getIntent().getExtras().getLong(getString(R.string.row_id));

        switch (item.getItemId()){
            case R.id.action_back:
                Intent intent= new Intent(context, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_edit:
                Intent intent2=new Intent(context, EditNote.class);
                intent2.putExtra(getString(R.string.row_id),note_id);
                startActivity(intent2);
                return true;
            case R.id.action_discard:
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder
                        .setTitle(getString(R.string.delete_title))
                        .setMessage(getString(R.string.delete_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.delete(dbHelper.TABLE_NAME, DbHelper.C_ID+"="+note_id,null);  //??????
                                db.close();
                                Intent intent3=new Intent(context, MainActivity.class);
                                startActivity(intent3);
                            }
                        })
                        .setNegativeButton(getString(R.string.no),null)
                        .show();
                   return true;
               default:
                   return super.onOptionsItemSelected(item);
        }
    }
}

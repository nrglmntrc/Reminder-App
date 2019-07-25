package com.nurgulmantarci.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nurgulmantarci.reminder.DataAccesLayer.DbHelper;
import com.nurgulmantarci.reminder.Receivers.AlarmReceiver;
import com.nurgulmantarci.reminder.Receivers.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNote extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper myDbHelper;
    EditText etTitle, etDescription;
    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView txtTime, txtDate;
    RadioButton radioAlarm, radioNotify,radioNothing;
    RadioGroup radioGroup;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_create_note));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));

        init();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioNothing.isChecked()){
                    pickerDate.setVisibility(View.INVISIBLE);
                    pickerTime.setVisibility(View.INVISIBLE);
                    txtTime.setVisibility(View.INVISIBLE);
                    txtDate.setVisibility(View.INVISIBLE);
                }else{
                    pickerDate.setVisibility(View.VISIBLE);
                    pickerTime.setVisibility(View.VISIBLE);
                    txtTime.setVisibility(View.VISIBLE);
                    txtDate.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void init() {
        myDbHelper=new DbHelper(context);
        db=myDbHelper.getWritableDatabase();

        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        pickerDate=findViewById(R.id.datePicker);
        pickerTime=findViewById(R.id.timePicker);
        txtTime=findViewById(R.id.txtTime);
        txtDate=findViewById(R.id.txtDate);
        radioGroup=findViewById(R.id.radioGroup);
        radioAlarm=findViewById(R.id.radioAlarm);
        radioNotify=findViewById(R.id.radioNotify);
        radioNothing=findViewById(R.id.radioNothing);

        pickerDate.setVisibility(View.INVISIBLE);
        pickerTime.setVisibility(View.INVISIBLE);
        txtTime.setVisibility(View.INVISIBLE);
        txtDate.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onBackPressed() {
        Intent intent= new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.action_save:
               String title=etTitle.getText().toString();
               String detail=etDescription.getText().toString();

               ContentValues contentValues=new ContentValues();
               contentValues.put(myDbHelper.TITLE,title);
               contentValues.put(myDbHelper.DETAIL,detail);

               if(radioAlarm.isChecked() || radioNotify.isChecked()){
                   Calendar calendar=Calendar.getInstance();
                   calendar.clear();
                   calendar.set(Calendar.MONTH,pickerDate.getMonth());
                   calendar.set(Calendar.DAY_OF_MONTH,pickerDate.getDayOfMonth());
                   calendar.set(Calendar.YEAR,pickerDate.getYear());
                   calendar.set(Calendar.HOUR,pickerTime.getCurrentHour());
                   calendar.set(Calendar.MINUTE,pickerTime.getCurrentMinute());
                   calendar.set(Calendar.SECOND,00);

                   SimpleDateFormat formatter=new SimpleDateFormat(getString(R.string.hour_minutes));
                   String timeString=formatter.format(new Date(calendar.getTimeInMillis()));
                   SimpleDateFormat dateFormatter=new SimpleDateFormat(getString(R.string.dateformate));
                   String dateString=dateFormatter.format(new Date(calendar.getTimeInMillis()));
                   SimpleDateFormat sdf=new SimpleDateFormat(getString(R.string.dateformate)+" "+getString(R.string.hour_minutes));
                   String datetimeString=sdf.format(new Date(calendar.getTimeInMillis()));

                   AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                   Intent intent=null;

                   String alertTitle=etTitle.getText().toString();
                   String alertContent=etDescription.getText().toString();

                   if(radioAlarm.isChecked()){
                       contentValues.put(myDbHelper.TYPE,getString(R.string.alarm));
                       intent= new Intent(context, AlarmReceiver.class);
                       intent.putExtra(getString(R.string.alert_title),alertTitle);
                       intent.putExtra(getString(R.string.alert_content),alertContent);

                   }else if(radioNotify.isChecked()){
                       contentValues.put(myDbHelper.TYPE,getString(R.string.notify));
                       intent=new Intent(context, NotificationReceiver.class);

                       intent.putExtra(getString(R.string.alert_title),alertTitle);
                       intent.putExtra(getString(R.string.alert_content),alertContent);
                   }

                   PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,0);

                   alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

                   contentValues.put(myDbHelper.TIME,timeString);
                   contentValues.put(myDbHelper.DATE,dateString);
                   contentValues.put(myDbHelper.DATETIME,datetimeString);
               }
               else {
                   contentValues.put(myDbHelper.TYPE,getString(R.string.nothing));
                   contentValues.put(myDbHelper.TIME,getString(R.string.Not_Set));
                   contentValues.putNull(myDbHelper.DATE);
                   contentValues.putNull(myDbHelper.DATETIME);
               }

               db.insert(myDbHelper.TABLE_NAME,null,contentValues);

               Intent intent_openMainScreen=new Intent(context, MainActivity.class);
               intent_openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent_openMainScreen);
               return true;


           case R.id.action_back:
               Intent intent_mainAkc=new Intent(context, MainActivity.class);
               startActivity(intent_mainAkc);
               return true;


           default:
               return  super.onOptionsItemSelected(item);
       }
    }
}

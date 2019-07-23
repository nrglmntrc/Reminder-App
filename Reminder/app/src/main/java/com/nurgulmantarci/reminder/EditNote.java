package com.nurgulmantarci.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class EditNote extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    EditText etTitle, etDescription;
    DatePicker pickerDate;
    TimePicker pickerTime;
    TextView txtTime, txtDate;
    RadioGroup radioGroup;
    RadioButton radioAlarm, radioNotify,radioNothing;
    Context context = this;
    Long note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        init();

        note_id = getIntent().getExtras().getLong(getString(R.string.row_id));

        Cursor cursor = db.rawQuery("SELECT * FROM " + dbHelper.TABLE_NAME + " where " + dbHelper.C_ID + "=" + note_id, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                etTitle.setText(cursor.getString(cursor.getColumnIndex(dbHelper.TITLE)));
                etDescription.setText(cursor.getString(cursor.getColumnIndex(dbHelper.DETAIL)));
                if (cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)).equals(getString(R.string.nothing))) {
                    pickerDate.setVisibility(View.INVISIBLE);
                    pickerTime.setVisibility(View.INVISIBLE);
                    txtTime.setVisibility(View.INVISIBLE);
                    txtDate.setVisibility(View.INVISIBLE);
                    radioNothing.setChecked(true);
                } else if (cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)).equals(getString(R.string.alarm))) {
                    radioAlarm.setChecked(true);
                    dateTimeSet(cursor);

                } else if (cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)).equals(getString(R.string.notify))) {
                    radioNotify.setChecked(true);
                    dateTimeSet(cursor);
                }

            }
            cursor.close();
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioNothing:
                            pickerDate.setVisibility(View.INVISIBLE);
                            pickerTime.setVisibility(View.INVISIBLE);
                            txtTime.setVisibility(View.INVISIBLE);
                            txtDate.setVisibility(View.INVISIBLE);
                            break;
                    case R.id.radioAlarm:
                        pickerDate.setVisibility(View.VISIBLE);
                        pickerTime.setVisibility(View.VISIBLE);
                        txtTime.setVisibility(View.VISIBLE);
                        txtDate.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioNotify:
                        pickerDate.setVisibility(View.VISIBLE);
                        pickerTime.setVisibility(View.VISIBLE);
                        txtTime.setVisibility(View.VISIBLE);
                        txtDate.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


    }


    private void dateTimeSet(Cursor cursor) {
        Calendar calendar=Calendar.getInstance();
        calendar.clear();

        String time=cursor.getString(cursor.getColumnIndex(dbHelper.TIME));
        String date=cursor.getString(cursor.getColumnIndex(dbHelper.DATE));

        String[] dates=date.split("/");
        int day= Integer.parseInt(dates[0]);
        int month=Integer.parseInt(dates[1])-1;
        int year=Integer.parseInt(dates[2]);
        pickerDate.updateDate(year,month,day);

        String[] times=time.split(":");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pickerTime.setMinute(Integer.parseInt(times[1]));
            pickerTime.setHour(Integer.parseInt(times[0]));
        }


    }



    private void init() {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();

        etTitle = findViewById(R.id.txttitle);
        etDescription = findViewById(R.id.description);
        pickerDate = findViewById(R.id.datePicker2);
        pickerTime = findViewById(R.id.timePicker2);
        txtTime = findViewById(R.id.txt_selecttime);
        txtDate = findViewById(R.id.txt_selectdate);

        radioGroup=findViewById(R.id.radioGroup);
        radioAlarm=findViewById(R.id.radioAlarm);
        radioNotify=findViewById(R.id.radioNotify);
        radioNothing=findViewById(R.id.radioNothing);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent openMainActivity = new Intent(context, MainActivity.class);
                startActivity(openMainActivity);
                return true;
            case R.id.action_save:
                String title = etTitle.getText().toString();
                String detail = etDescription.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(dbHelper.TITLE, title);
                cv.put(dbHelper.DETAIL, detail);

                if(radioAlarm.isChecked() || radioNotify.isChecked()){
                    Calendar calender = Calendar.getInstance();
                    calender.clear();
                    calender.set(Calendar.MONTH, pickerDate.getMonth());
                    calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
                    calender.set(Calendar.YEAR, pickerDate.getYear());
                    calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
                    calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
                    calender.set(Calendar.SECOND, 00);

                    SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
                    String timeString = formatter.format(new Date(calender.getTimeInMillis()));
                    SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
                    String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));
                    SimpleDateFormat sdf=new SimpleDateFormat(getString(R.string.dateformate)+" "+getString(R.string.hour_minutes));
                    String datetimeString=sdf.format(new Date(calender.getTimeInMillis()));

                    Intent intent=null;
                    String alertTitle = etTitle.getText().toString();
                    String alertContent=etDescription.getText().toString();

                    if (radioAlarm.isChecked()) {
                        cv.put(dbHelper.TYPE, getString(R.string.alarm));

                        intent = new Intent(context, AlarmReceiver.class);
                        intent.putExtra(getString(R.string.alert_title), alertTitle);
                        intent.putExtra(getString(R.string.alert_content),alertContent);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

                    }  if (radioNotify.isChecked()) {
                        cv.put(dbHelper.TYPE, getString(R.string.notify));

                        intent = new Intent(context, NotificationReceiver.class);
                        intent.putExtra(getString(R.string.alert_title), alertTitle);
                        intent.putExtra(getString(R.string.alert_content), alertContent);
                    }
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                    cv.put(dbHelper.TIME, timeString);
                    cv.put(dbHelper.DATE, dateString);
                    cv.put(dbHelper.DATETIME,datetimeString);

                }else{
                    cv.put(dbHelper.TYPE, getString(R.string.nothing));
                    cv.put(dbHelper.TIME,getString(R.string.Not_Set));
                    cv.putNull(dbHelper.DATE);
                    cv.putNull(dbHelper.DATETIME);
                }

                db.update(dbHelper.TABLE_NAME, cv, dbHelper.C_ID + "=" + note_id, null);

                Intent openMainScreen = new Intent(context, MainActivity.class);
                openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openMainScreen);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


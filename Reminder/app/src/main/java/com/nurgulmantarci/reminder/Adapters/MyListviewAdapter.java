package com.nurgulmantarci.reminder.Adapters;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nurgulmantarci.reminder.DataAccesLayer.DbHelper;
import com.nurgulmantarci.reminder.Entities.Task;
import com.nurgulmantarci.reminder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyListviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> taskList;
    DbHelper myDbHelper;
    SQLiteDatabase db;
    TextView txtTitle,txtDetail,txtType,txtDate,txtTime,txtNotId;
    ImageView image;


    public MyListviewAdapter(Context context) {
        this.context=context;

        myDbHelper=new DbHelper(this.context);
        db=myDbHelper.getWritableDatabase();

        final String[] column={myDbHelper.C_ID,myDbHelper.TITLE,myDbHelper.DETAIL,myDbHelper.TYPE,myDbHelper.TIME,myDbHelper.DATE,myDbHelper.DATETIME};
        final Cursor cursor=db.query(myDbHelper.TABLE_NAME,column,null,null,null,null,null);

        taskList=new ArrayList<>();
        if(cursor!=null && cursor.getCount()!=0){
            if(cursor.moveToFirst()){
                do{
                    Task task= new Task();
                    task.set_id(cursor.getString(cursor.getColumnIndex(myDbHelper.C_ID)));
                    task.setTitle(cursor.getString(cursor.getColumnIndex(myDbHelper.TITLE)));
                    task.setDescription(cursor.getString(cursor.getColumnIndex(myDbHelper.DETAIL)));
                    task.setType(cursor.getString(cursor.getColumnIndex(myDbHelper.TYPE)));
                    task.setTime(cursor.getString(cursor.getColumnIndex(myDbHelper.TIME)));
                    task.setDate(cursor.getString(cursor.getColumnIndex(myDbHelper.DATE)));
                    task.setDatetime(cursor.getString(cursor.getColumnIndex(myDbHelper.DATETIME)));
                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        }

    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view= LayoutInflater.from(context).inflate(R.layout.listview_layout,parent,false);

        txtTitle=view.findViewById(R.id.title);
        txtDetail=view.findViewById(R.id.Detail);
        txtType=view.findViewById(R.id.type);
        txtDate=view.findViewById(R.id.time);
        txtTime=view.findViewById(R.id.date);
        image=view.findViewById(R.id.alarmImage);
        txtNotId=view.findViewById(R.id.notId);


        txtTitle.setText(taskList.get(position).getTitle());
        txtDetail.setText(taskList.get(position).getDescription());
        txtType.setText(taskList.get(position).getType());
        txtTime.setText(taskList.get(position).getTime());
        txtDate.setText(taskList.get(position).getDate());
        txtNotId.setText(taskList.get(position).get_id());

        if(taskList.get(position).getType().equals(context.getString(R.string.alarm))){
            image.setImageResource(R.drawable.alarm);
            drawOn(position);

        }else if(taskList.get(position).getType().equals(context.getString(R.string.notify))){
           image.setImageResource(R.drawable.notification);
           drawOn(position);
        }else{
            image.setImageResource(R.drawable.noalert);
        }

        return view;
    }

    private void drawOn(int position) {

        try {
            SimpleDateFormat sdf=new SimpleDateFormat(context.getString(R.string.dateformate)+" " +context.getString(R.string.hour_minutes));
            Date dateAlert=sdf.parse(taskList.get(position).getDatetime());
            Date now=new Date();
            if(now.getTime()>dateAlert.getTime()){
                txtTitle.setPaintFlags(txtTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ERROR",e.getLocalizedMessage());
        }

    }
}

package com.nurgulmantarci.reminder;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> taskList;
    DbHelper myDbHelper;
    SQLiteDatabase db;


    public MyListviewAdapter(Context context) {
        this.context=context;

        myDbHelper=new DbHelper(this.context);
        db=myDbHelper.getWritableDatabase();

        final String[] column={myDbHelper.C_ID,myDbHelper.TITLE,myDbHelper.DETAIL,myDbHelper.TYPE,myDbHelper.TIME,myDbHelper.DATE};
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
        TextView txtTitle,txtDetail,txtType,txtDate,txtTime;
        ImageView image;
        txtTitle=view.findViewById(R.id.title);
        txtDetail=view.findViewById(R.id.Detail);
        txtType=view.findViewById(R.id.type);
        txtDate=view.findViewById(R.id.time);
        txtTime=view.findViewById(R.id.date);
        image=view.findViewById(R.id.alarmImage);

        txtTitle.setText(taskList.get(position).getTitle());
        txtDetail.setText(taskList.get(position).getDescription());
        txtType.setText(taskList.get(position).getType());
        txtTime.setText(taskList.get(position).getTime());
        txtDate.setText(taskList.get(position).getDate());

        if(taskList.get(position).getType().equals(context.getString(R.string.alarm))){
            image.setImageResource(R.drawable.ic_action_alarms);

        }else if(taskList.get(position).getType().equals(context.getString(R.string.notify))){
           image.setImageResource(R.drawable.notification);
        }else{
            image.setImageResource(R.drawable.noalert);
        }

        return view;
    }
}

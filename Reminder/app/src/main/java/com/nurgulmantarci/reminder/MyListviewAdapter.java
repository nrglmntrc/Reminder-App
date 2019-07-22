package com.nurgulmantarci.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListviewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Task> taskList;

    public MyListviewAdapter(Context context, ArrayList<Task> taskList) {
        this.context=context;
        this.taskList=taskList;
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
       View view= LayoutInflater.from(context).inflate(R.layout.list_entry,parent,false);
        TextView txtTitle=view.findViewById(R.id.title);
        txtTitle.setText(taskList.get(position).getTitle());
        return view;
    }
}

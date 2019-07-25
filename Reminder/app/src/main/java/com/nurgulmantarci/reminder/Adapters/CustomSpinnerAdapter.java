package com.nurgulmantarci.reminder.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nurgulmantarci.reminder.Entities.Music;

public class CustomSpinnerAdapter extends ArrayAdapter<Music> {

    private Context context;
    private Music[] values;

    public CustomSpinnerAdapter(Context context, int textViewRecourceId, Music[] values) {
        super(context, textViewRecourceId,values);
        this.context=context;
        this.values=values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Music getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        TextView label= (TextView) super.getView(position,convertView,parent);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label= (TextView) super.getDropDownView(position,convertView,parent);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getName());
        return label;
    }
    //aklıma şimdi geldi..acaba spinner içinde 200 veri büyük mü geliyor?
    //ama mesela ilçe bilgisi alıncada baya ilce verisi gelir dimi?
    //yok abla senin verilerinden biri boş geliyor
}

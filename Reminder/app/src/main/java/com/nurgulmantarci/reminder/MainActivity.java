package com.nurgulmantarci.reminder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nurgulmantarci.reminder.Adapters.MyListviewAdapter;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton fab;
    Context context=this;

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

        MyListviewAdapter adapter= new MyListviewAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView txtnotId=view.findViewById(R.id.notId);
                id=Long.parseLong(txtnotId.getText().toString());

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
            case R.id.action_settings:
                Intent intent1=new Intent(context, SetMusic.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

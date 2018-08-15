package com.example.vedantgote.simplenotepad2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText ed;
    ListView lv;
    Mythread td;
    long fg;
    DbAdapter dba;
    SimpleCursorAdapter sca;

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        long i = menuInfo.id;
        switch (item.getItemId())
        {
            case R.id.del:
                Toast.makeText(MainActivity.this,menuInfo.toString(),Toast.LENGTH_LONG).show();
                dba.delrow(i);
                 return  true;
            case R.id.sh:
                Toast.makeText(this,"Share functionality not added till now",Toast.LENGTH_LONG).show();
                return true;

        default:
        return super.onContextItemSelected(item);
    }}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv =(ListView)findViewById(R.id.lv);
        ed=(EditText)findViewById(R.id.ed);
        openDb();
        Thread th = new Thread(new Mythread());
        th.start();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
              fg=1;
              String f = dba.sel(id);
              f.trim();
              Intent o = new Intent(MainActivity.this,Notes.class);
              o.putExtra("filename",f); // id deni padti he na be
              o.putExtra("flag",fg);
              o.putExtra("pos",id);
              startActivity(o);

            }

        });
        lv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.fullmenu, menu);

            }


        });



        ed.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MainActivity.this.sca.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void openDb()
    {
        dba = new DbAdapter(this);
        dba.open();
    }
    public void onNew(View view)
    {
        Intent i = new Intent(MainActivity.this,Notes.class);
        startActivity(i);
    }



    public void populate()
    {
        Cursor c = dba.getallrows();
        String dbd[]={dba.NAME,dba.TIME};
        int itm[]={R.id.tvn,R.id.tvt};
        sca = new SimpleCursorAdapter(this,R.layout.item,c,dbd,itm,0);
        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(sca);
    }
    public class Mythread implements Runnable
    {
        @Override
        public void run()
        {
            populate();
        }
    }
}

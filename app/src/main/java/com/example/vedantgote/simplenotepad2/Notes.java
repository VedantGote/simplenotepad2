package com.example.vedantgote.simplenotepad2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Notes extends AppCompatActivity {
    EditText t,bd;
    String fl=null;
    String tl;
    DbAdapter dba;
    long f=0;
    long ps=0;


    //kai mast chalu ahe sagla
    Time td = new Time(Time.getCurrentTimezone());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        dba = new DbAdapter(this);
        t=(EditText)findViewById(R.id.tit);
        bd=(EditText)findViewById(R.id.bd);
        tl=t.getText().toString();
        f=0;

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
        fl = b.getString("filename");
        fl.trim();
        f=b.getLong("flag");
        ps=b.getLong("pos");
        if(fl!=null)
        {
            t.setText(fl);
            bd.setText(open(fl+".txt"));
        }}


    }

    public void onback(View view)
    {
        Thread tr = new Thread(new Mthread());
        tr.start();
        update();
    }

     public void update()
     {
         td.setToNow();
         String tm = td.format("%H:%M:%S %d/%m/%Y");
         tl=t.getText().toString();
         if(f==0) {
             dba.inserdata(tl, tm);
         }//nl.add(tl);
         else
         {
             dba.replace(ps,tl,tm);
         }
         t.setText("");
         bd.setText("");
         Intent ni = new Intent(Notes.this,MainActivity.class);

         startActivity(ni);

     }


        //MainActivity ob = new MainActivity();
        //ob.l.add(t.getText().toString());
        //ob.ad.notifyDataSetChanged();


    public void save(String fn)
    {
        try
        {
            OutputStreamWriter os = new OutputStreamWriter(openFileOutput(fn,0));
            os.write(bd.getText().toString());
            os.close();
            Toast.makeText(Notes.this,"Notes Saved",Toast.LENGTH_LONG).show();
        }
        catch(Throwable t)
        {
            Toast.makeText(Notes.this,t.toString(),Toast.LENGTH_LONG).show();
        }
    }
    public boolean filexist(String filenm)
    {
        File file = getBaseContext().getFileStreamPath(filenm);
        return file.exists();
    }

    public String open(String filenm)
    {
        String nts ="not";
        if(filexist(filenm))
        {
            nts="file hae to sahi";
            try{
                InputStream i = openFileInput(filenm);//read in bytes
                if(i!=null)
                {
                    InputStreamReader ip = new InputStreamReader(i); // read in characters briget between byte an char
                    BufferedReader bf = new BufferedReader(ip); // goodpratise to wrap around inputstramreader as it is inefficnet
                    StringBuilder sb = new StringBuilder();
                    String st;
                    while((st = bf.readLine())!=null)
                    {
                        sb.append(st + '\n');
                    }i.close();
                    nts=sb.toString();
                }
            }
            catch(java.io.FileNotFoundException e)
            {}

            catch(Throwable t)
            {
                Toast.makeText(Notes.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        }
        return nts;
    }
    public class Mthread implements Runnable{
        public void run()
        {
            save(tl+".txt");

        }
    }

}

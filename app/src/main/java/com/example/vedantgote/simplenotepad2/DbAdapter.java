package com.example.vedantgote.simplenotepad2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vedant gote on 29-03-2018.
 */

public class DbAdapter {
    Myhelper mh;
    public static final String DATABASE_NAME="files";
    public static final String TABLE_NAME="filetable";
    public static final int VERSION=2;
    public static final String ID="_id";
    public static final String NAME="Name";
    public static final String TIME="Time";
    public SQLiteDatabase db;
    public static final String CREATE_TABLE="CREATE TABLE " + TABLE_NAME
            + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
            + " VARCHAR(255), " + TIME + " VARCHAR(255));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
    public DbAdapter(Context context) {
        mh = new Myhelper(context);
    }
    public DbAdapter open()
    {
        db = mh.getWritableDatabase();
        return this;
    }

    public long inserdata(String s,String s1)
    {
        SQLiteDatabase db = mh.getWritableDatabase();
        ContentValues ct = new ContentValues();
        ct.put(NAME,s);
        ct.put(TIME,s1);
        return db.insert(TABLE_NAME,null,ct);
    }

    public Cursor getallrows()
    {
        SQLiteDatabase db = mh.getWritableDatabase();
        String all[]={ID,NAME,TIME};
        String where = null;
        Cursor c = db.query(true,TABLE_NAME,all,where,null,null,null,null,null);
        if(c!=null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public long delrow(long id)
    {
        String where = ID + "=" +id;
        return db.delete(TABLE_NAME,where,null);
    }

    public void replace(long p,String a,String b)
    {
        SQLiteDatabase db = mh.getWritableDatabase();

        String rep = "REPLACE INTO "+TABLE_NAME+" ("+ ID + "," + NAME +"," + TIME + ") " +
                "VALUES "+"(" + p + "," + a +"," + b + ")";
        db.execSQL(rep);
    }

    public String sel(long id)
    {
        SQLiteDatabase db = mh.getReadableDatabase();
        String sl = "SELECT "+NAME + ","+TIME+" FROM "+ TABLE_NAME+ " WHERE " + ID +"=" +id;
        Cursor c = db.rawQuery(sl,null);
        c.moveToFirst();
        return c.getString(0);
    }

    public class Myhelper extends SQLiteOpenHelper{


        public Context context;
        public Myhelper(Context context)
        {
            super(context,DATABASE_NAME,null,VERSION);
            this.context = context;

        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}

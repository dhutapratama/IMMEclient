package com.imme.immeclient;

/**
 * Created by sysadm@ilccourse.com on 2/5/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SecurityData  extends SQLiteOpenHelper {
    public static final String TABLE_LOGIN_DATA = "login_data";
    public static final String SESSION_KEY = "session_key";
    public static final String FIRST_TIME = "fresh_instal";
    public static final String DB_NAME = "IMME.db";
    public static final int DB_VER = 2;

    public SecurityData(Context context)
    {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String login_data = "CREATE TABLE " + TABLE_LOGIN_DATA + " ( " + SESSION_KEY + " TEXT, " + FIRST_TIME + " INTEGER)";
        db.execSQL(login_data);

        ContentValues values = new ContentValues();
        values.put(SESSION_KEY, "");
        values.put(FIRST_TIME, 1);
        db.insert( TABLE_LOGIN_DATA, SESSION_KEY, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_DATA);
        onCreate(db);
    }


}


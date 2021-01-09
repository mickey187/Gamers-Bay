package com.progamer.gamersbay.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.progamer.gamersbay.notification.NotificationModel;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "gamers_bey.db";
    public static final String TABLE_NAME = "notification";
    public static final String NOTIFICATION_ID = "id";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_TIME = "time";
    public static final String NOTIFICATION_BODY = "body";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
      }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ TABLE_NAME +" ( id integer primary key autoincrement, title text,time text, body text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public boolean insertData(String title, String time, String body){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
                values.put(NOTIFICATION_TIME, time);
                values.put(NOTIFICATION_TITLE, title);
                values.put(NOTIFICATION_BODY, body);
                double x = db.insert(TABLE_NAME,null,values);
                    if (x == -1){
                        return false;
                    }else
                        return true;
    }
    public ArrayList<NotificationModel> getAllData(){
        ArrayList<NotificationModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ TABLE_NAME, null);
        while (cursor.moveToNext()) {
            list.add(new NotificationModel(cursor.getString(1),cursor.getString(3), cursor.getString(2)));
        }
        return list;
    }
}

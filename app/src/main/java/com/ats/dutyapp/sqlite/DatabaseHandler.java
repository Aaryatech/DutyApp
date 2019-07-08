package com.ats.dutyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ats.dutyapp.model.NotificationTemp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "CommunicationApp";

    private static final String TABLE_NOTIFICATION = "notification";

    private static final String N_ID = "nId";
    private static final String N_TITLE = "nTitle";
    private static final String N_DESC = "nDesc";
    private static final String N_DATE = "nDate";


    String CREATE_TABLE_NOTIFICATION = "CREATE TABLE "
            + TABLE_NOTIFICATION + "("
            + N_ID + " INTEGER PRIMARY KEY, "
            + N_TITLE + " TEXT, "
            + N_DESC + " TEXT, "
            + N_DATE + " TEXT) ";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);
    }


    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, null, null);
        db.close();
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NOTIFICATION);
        db.close();
    }


    //----------------------------------NOTIFICATION------------------------------------


    public void addNotification(String title, String desc, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(N_TITLE, title);
        values.put(N_DESC, desc);
        values.put(N_DATE, date);

        db.insert(TABLE_NOTIFICATION, null, values);
        db.close();
    }

    public void insertNotification(ArrayList<NotificationTemp> notificationModels ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for(int i=0;i<notificationModels.size();i++){
            values.put(N_TITLE, notificationModels.get(i).getTitle());
            values.put(N_DESC,  notificationModels.get(i).getMessage());
            values.put(N_DATE, notificationModels.get(i).getDate());
            db.insert(TABLE_NOTIFICATION, null, values); //Insert each time for loop count
        }
      //  db.insert(TABLE_NOTIFICATION, null, values); //Insert each time for loop count
        values.clear();
        db.close();
    }
    public ArrayList<NotificationTemp> getAllNotification() {
        ArrayList<NotificationTemp> messageList = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String todaysDate = sdf.format(date.getTime());
        //Log.e("TODAYS DATE : ", "-------------" + todaysDate);

        //String query = "SELECT * FROM " + TABLE_MESSAGE + " WHERE " + M_TO_DATE + "<'" + todaysDate + "'";
        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " ORDER BY " + N_ID + " DESC";

        //Log.e("QUERY : ", "-------------" + query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                NotificationTemp message = new NotificationTemp();
                message.setId(cursor.getInt(0));
                message.setTitle(cursor.getString(1));
                message.setMessage(cursor.getString(2));
                message.setDate(cursor.getString(3));
                messageList.add(message);
            } while (cursor.moveToNext());
        }
        return messageList;
    }
}

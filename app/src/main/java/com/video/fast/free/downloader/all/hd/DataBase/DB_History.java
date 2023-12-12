package com.video.fast.free.downloader.all.hd.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.video.fast.free.downloader.all.hd.Model.HistoryData;

import java.util.ArrayList;

public class DB_History extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SearchHis.db";
    private static final String TABLE_NAME = "myHistory";
    private static final int DB_VERSION = 1;

    private static final String ID_His = "id";
    private static final String DATE_His = "date";
    private static final String LINK_His = "link";
    boolean boolean_isdate;


    public DB_History(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_His + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_His + " TEXT,"
                + LINK_His + " TEXT)";


        db.execSQL(query);
    }

    public void insertHis(String date, String link) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(DATE_His, date);
        values.put(LINK_His, link);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public void deleteHis(HistoryData historyData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_His + " = ?",
                new String[]{String.valueOf(historyData.getmID())});
        db.close();
    }

    public ArrayList<HistoryData> getAllHistory() {
        ArrayList<HistoryData> historyDataArrayList = new ArrayList<HistoryData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + DATE_His + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // Adding contact to list

                int i2 = 0;
                while (true) {
                    if (i2 >= historyDataArrayList.size()) {
                        break;
                    } else if ((historyDataArrayList.get(i2)).getmDate().equals(cursor.getString(1))) {
                        boolean_isdate = true;
                        break;
                    } else {
                        boolean_isdate = false;
                        i2++;
                    }
                }

                if (!boolean_isdate) {
                    HistoryData historyData = new HistoryData();
                    historyData.setmID(cursor.getString(0));
                    historyData.setmDate(cursor.getString(1));
                    historyData.setmLink(cursor.getString(2));
                    historyData.setNxtDay(true);
                    historyDataArrayList.add(historyData);

                }

                HistoryData historyData = new HistoryData();
                historyData.setmID(cursor.getString(0));
                historyData.setmDate(cursor.getString(1));
                historyData.setmLink(cursor.getString(2));
                historyData.setNxtDay(false);
                historyDataArrayList.add(historyData);

            } while (cursor.moveToNext());
        }

        // return contact list
        return historyDataArrayList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}

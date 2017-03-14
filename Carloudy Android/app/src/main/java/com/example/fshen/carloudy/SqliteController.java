package com.example.fshen.carloudy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

/**
 * Created by fshen on 17/2/25.
 */
public class SqliteController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    public String table_name = "messages";
    public String _ID = "_id";
    public String col1 = "title";
    public String col2 = "content";
    public String col3 = "time";
    public String col4 = "lati";
    public String col5 = "longi";
    String[] myStringArray = new String[2];
    String val;


    public SqliteController(Context applicationcontext) {
        super(applicationcontext, "message.db", null, 4);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE "+table_name +" (" + _ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " + col1 + " TEXT NOT NULL, " + col2 + " TEXT NOT NULL, "+ col3 + " TEXT NOT NULL, "+ col4 + " TEXT NOT NULL, " + col5 + " TEXT NOT NULL);";
        database.execSQL(query);
        Log.d(LOGCAT, "Table Created");
    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS "+table_name;
        database.execSQL(query);
        onCreate(database);
    }

    public void deletemsg(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(table_name, "title = ?", new String[]{title});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public long insertmsg(String title, String content, String time, String lati, String longi) {

        long status;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("time", time);
        values.put("lati", lati);
        values.put("longi", longi);

        status = database.insert(table_name, null, values);
        return status;


    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("select _id , title, content, time, lati, longi from messages", null);
    }

    public String check_existing_msg(String title)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM messages WHERE " + col1 + "=?", new String[]{title});
        if (c.getCount() > 0)
        {
            return null;
        }
        else {return "success"; }
    }

    public Cursor get_each_msgs(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM messages WHERE " + col1 + "=?", new String[]{title});
        return c;

    }

    public void cleardb() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "delete from messages";
        database.execSQL(query);
    }

}
package com.example.swmc.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.swmc.Models.User;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SWAMCOV2.db";

    //User Table
    private static final String TABLE_USER = "Current_App_user";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME= "user_name";
    private static final String KEY_USER_TYPE= "user_type";
    private static final String TAG = "TAG";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_USER_NAME+ " TEXT, " +
                KEY_USER_TYPE + " INTEGER, " +
                TAG +" TEXT)";
        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
    }

    //Add User
    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, 0);
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_USER_TYPE, user.getUserType());
        values.put(TAG,0);

        // Inserting Row
        long result = db.insert(TABLE_USER, null, values);
        if (result == -1){
            return false;
        }
        else {
            db.close();
            return true;
        }

    }

    public User getAppUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_ID,
                        KEY_USER_NAME, KEY_USER_TYPE}, TAG + "=?",
                new String[] { String.valueOf(0) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = new User();

        user.setUserName(cursor.getString(1));
        user.setUserType(cursor.getInt(2));
        return user;
    }


    public boolean deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_USER,TAG + " = ?", new String[] { String.valueOf(0) });
        if (result == -1){
            return false;
        }
        else {
            db.close();
            return true;
        }
    }
}

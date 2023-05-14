package com.example.womensafety.db;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.womensafety.model.Contact;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactInfo.db";
    public static String TABLE_APPINFO = "ContactInfo";
    public static String TABLE_SELECTED_APPINFO = "Selected_ContactInfo";
    public static String KEY_ID = "_id";
    public static String KEY_PH_NUMBER = "phoneNumber";
    public static String KEY_NAME = "name";
    public static String KEY_BITMAP = "bitmap";
    public static String KEY_IS_CHECKED = "is_checked";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_APPINFO + "("
                + KEY_ID + " integer primary key, "
                + KEY_NAME + " text, "
                + KEY_BITMAP + " text, "
                + KEY_PH_NUMBER + " text, "
                + KEY_IS_CHECKED + " integer "
                + ")"
        );

        db.execSQL("create table " + TABLE_SELECTED_APPINFO + "("
                + KEY_ID + " integer primary key, "
                + KEY_NAME + " text, "
                + KEY_BITMAP + " text, "
                + KEY_PH_NUMBER + " text, "
                + KEY_IS_CHECKED + " integer "
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_APPINFO);
        db.execSQL("drop table if exists " + TABLE_SELECTED_APPINFO);
        onCreate(db);
    }

    public void saveAppList(ArrayList<Contact> arrayAppList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (Contact appInfo : arrayAppList) {
            contentValues.put(KEY_NAME, appInfo.getName());
//            contentValues.put(KEY_BITMAP, appInfo.getBitmap());
            contentValues.put(KEY_PH_NUMBER, appInfo.getPh_no());
            contentValues.put(KEY_IS_CHECKED, appInfo.isSelected());
            db.insert(TABLE_APPINFO, null, contentValues);
        }
        db.close();
    }

    public ArrayList<Contact> getAppList() {
        ArrayList<Contact> serverList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_APPINFO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                serverList.add(parseServer(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d("TAG", "0 rows");
        }
        cursor.close();
        db.close();
        return serverList;
    }


    public void saveSelectedAppList(Contact appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, appInfo.getName());
//        contentValues.put(KEY_BITMAP, appInfo.getBitmap());
        contentValues.put(KEY_PH_NUMBER, appInfo.getPh_no());
        contentValues.put(KEY_IS_CHECKED, appInfo.isSelected());
        db.insert(TABLE_SELECTED_APPINFO, null, contentValues);
        db.close();
    }

    public void saveSelectedAppList(ArrayList<Contact> appInfos) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Contact appInfo : appInfos) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, appInfo.getName());
//            contentValues.put(KEY_BITMAP, appInfo.getBitmap());
            contentValues.put(KEY_PH_NUMBER, appInfo.getPh_no());
            contentValues.put(KEY_IS_CHECKED, appInfo.isSelected());
            db.insert(TABLE_SELECTED_APPINFO, null, contentValues);
        }
        db.close();
    }

    public ArrayList<Contact> getSelectedAppList() {
        ArrayList<Contact> serverList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SELECTED_APPINFO, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                serverList.add(parseServer(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.d("TAG", "0 rows");
        }
        cursor.close();

        db.close();
        return serverList;
    }

    public void delFullAppList() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_APPINFO);
        db.close();

    }

    public void delFullSelectedAppList() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_SELECTED_APPINFO);
        db.close();

    }


    public void delAppList(Contact appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APPINFO, KEY_PH_NUMBER + " = ?", new String[]{appInfo.getPh_no()});
        db.close();
    }

    public void delSelectedAppList(Contact appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELECTED_APPINFO, KEY_PH_NUMBER + " = ?", new String[]{appInfo.getPh_no()});
        db.close();
    }

    public void delSelectedAppList(int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Contact> appInfos = getSelectedAppList();
            db.delete(TABLE_SELECTED_APPINFO, KEY_PH_NUMBER + " = ?", new String[]{appInfos.get(position).getPh_no()});
        db.close();
    }


    public boolean checkPackage(Contact server) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SELECTED_APPINFO,
                null,
                KEY_PH_NUMBER + "=?",
                new String[]{server.getPh_no()},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            result = true;
        } else {
            Log.d(TAG, "0 rows");
        }
        cursor.close();
        db.close();
        return result;
    }

    @SuppressLint("Range")
    private Contact parseServer(Cursor cursor) {
        return new Contact(
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PH_NUMBER))
        );
    }
}

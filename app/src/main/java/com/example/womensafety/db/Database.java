package com.example.womensafety.db;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.womensafety.model.Contact;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactInfo.db";
    public static String TABLE_CONTACT = "ContactInfo";
    public static String KEY_ID = "_id";
    public static String KEY_PH_NUMBER = "phoneNumber";
    public static String KEY_NAME = "name";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACT + "("
                + KEY_ID + " integer primary key, "
                + KEY_NAME + " text, "
                + KEY_PH_NUMBER + " text "
                + ")"
        );
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACT);
        onCreate(db);
    }

    public ArrayList<Contact> getContactList() {
        ArrayList<Contact> serverList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONTACT, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                serverList.add(parseServer(cursor));
            } while (cursor.moveToNext());
        } else {
            Log.e("TAG", "0 rows");
        }
        cursor.close();
        db.close();
        return serverList;
    }


    public void saveContact(@NonNull Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, contact.getName());
        contentValues.put(KEY_PH_NUMBER, contact.getPh_no());
        db.insert(TABLE_CONTACT, null, contentValues);
        db.close();
    }

    public void delFullAppList() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTACT);
        db.close();

    }

    public void delAppList(@NonNull Contact appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, KEY_PH_NUMBER + " = ?", new String[]{appInfo.getPh_no()});
        db.close();
    }

    public void delSelectedAppList(@NonNull Contact appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT, KEY_PH_NUMBER + " = ?", new String[]{appInfo.getPh_no()});
        db.close();
    }

    @NonNull
    @SuppressLint("Range")
    private Contact parseServer(@NonNull Cursor cursor) {
        return new Contact(
                cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PH_NUMBER))
        );
    }
}

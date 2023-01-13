package com.example.contactmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactmanager.db.Entity.Contact;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contact.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);
        onCreate(db);
    }

    public long insertContact(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, name);
        values.put(Contact.COLUMN_EMAIL, email);
        long ID = db.insert(Contact.TABLE_NAME, null, values);
        db.close();
        return ID;
    }

    public Contact getContact(long ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contact contact;
        try (Cursor cursor = db.query(Contact.TABLE_NAME,
                new String[]{Contact.COLUMN_ID, Contact.COLUMN_NAME, Contact.COLUMN_EMAIL},
                Contact.COLUMN_ID + "=?",
                new String[]{String.valueOf(ID)},
                null,
                null,
                null,
                null)) {

            contact = null;
            if (cursor != null) {
                cursor.moveToFirst();
                contact = new Contact(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL))
                );
            }
        }
        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Contact.TABLE_NAME + " ORDER BY " + Contact.COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact(
                            cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL))
                    );
                    contactArrayList.add(contact);
                } while (cursor.moveToNext());
            }
        }
        return contactArrayList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_EMAIL, contact.getEmail());
        return db.update(Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ? ", new String[]{String.valueOf(contact.getID())});
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contact.TABLE_NAME, Contact.COLUMN_ID + " = ?", new String[]{String.valueOf(contact.getID())});
        db.close();
    }
}

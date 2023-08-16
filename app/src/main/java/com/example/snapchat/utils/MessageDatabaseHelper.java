package com.example.snapchat.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class MessageDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "message.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "messages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_RECIPIENT_ID = "recipient_id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_MESSAGE_TYPE = "message_type";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IS_READ = "is_read";
    public static final String COLUMN_IS_RECEIVED = "is_received";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_SENDER_ID + " INTEGER,"
            + COLUMN_RECIPIENT_ID + " INTEGER,"
            + COLUMN_CONTENT + " TEXT,"
            + COLUMN_MESSAGE_TYPE + " INTEGER,"
            + COLUMN_TIMESTAMP + " TEXT,"
            + COLUMN_IS_READ + " INTEGER,"
            + COLUMN_IS_RECEIVED + " INTEGER"
            + ")";

    public MessageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
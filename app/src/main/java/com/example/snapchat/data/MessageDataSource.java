package com.example.snapchat.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.snapchat.data.model.Message;
import com.example.snapchat.utils.DataManager;
import com.example.snapchat.utils.MessageDatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDataSource {

    private SQLiteDatabase database;
    private MessageDatabaseHelper dbHelper;

    public MessageDataSource(Context context) {
        dbHelper = new MessageDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @SuppressLint("Range")
    private Map<Integer, MutableLiveData<List<Message>>> loadMessagesFromDatabase() {
        Map<Integer, MutableLiveData<List<Message>>> messagesWithSender = new HashMap<>();

        // Query the database and populate messagesWithSender Map
        Cursor cursor = database.query(MessageDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Message message = new Message();
                message.id = cursor.getInt(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_ID));
                message.senderId = cursor.getInt(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_SENDER_ID));
                message.recipientId = cursor.getInt(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_RECIPIENT_ID));
                message.content = cursor.getString(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_CONTENT));
                message.timestamp = cursor.getString(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_TIMESTAMP));
                // Convert the messageType value to the corresponding enum
                int messageTypeValue = cursor.getInt(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_MESSAGE_TYPE));
                message.messageType = Message.MessageType.values()[messageTypeValue];
                // Convert isRead and isReceived values to boolean
                message.isRead = cursor.getInt(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_IS_READ)) == 1;
                message.isReceived = cursor.getInt(cursor.getColumnIndex(MessageDatabaseHelper.COLUMN_IS_RECEIVED)) == 1;

                MutableLiveData<List<Message>> recipientMessages;
                if(message.senderId == DataManager.getInstance().getUserLiveData().getValue().id){
                    recipientMessages = messagesWithSender.get(message.recipientId);
                }
                else{
                    recipientMessages = messagesWithSender.get(message.senderId);
                }

                if (recipientMessages == null) {
                    recipientMessages = new MutableLiveData<>(new ArrayList<>());
                    if(message.senderId == DataManager.getInstance().getUserLiveData().getValue().id)
                        messagesWithSender.put(message.recipientId, recipientMessages);
                    else
                        messagesWithSender.put(message.senderId, recipientMessages);
                }
                recipientMessages.getValue().add(message);
            }
            cursor.close();
        }

        return messagesWithSender;
    }

    public Map<Integer, MutableLiveData<List<Message>>> getMessagesWithSender() {
        return loadMessagesFromDatabase();
    }

    public long insertMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(MessageDatabaseHelper.COLUMN_SENDER_ID, message.senderId);
        values.put(MessageDatabaseHelper.COLUMN_RECIPIENT_ID, message.recipientId);
        values.put(MessageDatabaseHelper.COLUMN_CONTENT, message.content);
        values.put(MessageDatabaseHelper.COLUMN_MESSAGE_TYPE, message.messageType.ordinal()); // Store the enum ordinal
        values.put(MessageDatabaseHelper.COLUMN_TIMESTAMP, message.timestamp);
        values.put(MessageDatabaseHelper.COLUMN_IS_READ, message.isRead ? 1 : 0);
        values.put(MessageDatabaseHelper.COLUMN_IS_RECEIVED, message.isReceived ? 1 : 0);

        return database.insert(MessageDatabaseHelper.TABLE_NAME, null, values);
    }
}
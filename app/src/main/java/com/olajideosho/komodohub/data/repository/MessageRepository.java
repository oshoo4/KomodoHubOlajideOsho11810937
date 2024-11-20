package com.olajideosho.komodohub.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.olajideosho.komodohub.data.local.DBHelper;
import com.olajideosho.komodohub.data.model.Message;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class MessageRepository {
    private DBHelper dbHelper;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MessageRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sender_id", message.getSenderId());

        if (message.getReceiverId() != null) {
            values.put("receiver_id", message.getReceiverId());
        }

        if (message.getClassroomId() != null) {
            values.put("classroom_id", message.getClassroomId());
        }

        values.put("content", message.getContent());
        values.put("timestamp", formatter.format(message.getTimestamp()));
        long messageId = db.insert("Message", null, values);
        db.close();
        return messageId;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"message_id", "sender_id", "receiver_id", "classroom_id", "content", "timestamp"};
        Cursor cursor = db.query("Message", columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int classroomIdIndex = cursor.getColumnIndex("classroom_id");
                Integer classroomId = (classroomIdIndex != -1 && !cursor.isNull(classroomIdIndex)) ?
                        cursor.getInt(classroomIdIndex) : null;

                Message message = new Message(
                        cursor.getInt(cursor.getColumnIndexOrThrow("message_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("sender_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("receiver_id")),
                        classroomId,
                        cursor.getString(cursor.getColumnIndexOrThrow("content")),
                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("timestamp")), formatter)
                );
                messages.add(message);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return messages;
    }

    public List<Message> getMessagesForClassroomFromSender(int classroomId, int senderId) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"message_id", "sender_id", "receiver_id", "classroom_id", "content", "timestamp"};
        String selection = "classroom_id = ? AND sender_id = ?";
        String[] selectionArgs = {String.valueOf(classroomId), String.valueOf(senderId)};
        Cursor cursor = db.query("Message", columns, selection, selectionArgs, null, null, "timestamp ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int classroomIdIndex = cursor.getColumnIndex("classroom_id");
                Integer classroomIdValue = (classroomIdIndex != -1 && !cursor.isNull(classroomIdIndex)) ?
                        cursor.getInt(classroomIdIndex) : null;

                Message message = new Message(
                        cursor.getInt(cursor.getColumnIndexOrThrow("message_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("sender_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("receiver_id")),
                        classroomIdValue,
                        cursor.getString(cursor.getColumnIndexOrThrow("content")),
                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("timestamp")), formatter)
                );
                messages.add(message);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return messages;
    }

    public List<Message> getMessagesForClassroomFromSenders(int classroomId, List<Integer> senderIds) {
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"message_id", "sender_id", "receiver_id", "classroom_id", "content", "timestamp"};

        StringBuilder selection = new StringBuilder("classroom_id = ? AND (");
        String[] selectionArgs = new String[senderIds.size() + 1];
        selectionArgs[0] = String.valueOf(classroomId);
        for (int i = 0; i < senderIds.size(); i++) {
            selection.append("sender_id = ?");
            if (i < senderIds.size() - 1) {
                selection.append(" OR ");
            }
            selectionArgs[i + 1] = String.valueOf(senderIds.get(i));
        }
        selection.append(")");

        Cursor cursor = db.query("Message", columns, selection.toString(), selectionArgs, null, null, "timestamp ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int classroomIdIndex = cursor.getColumnIndex("classroom_id");
                Integer classroomIdValue = (classroomIdIndex != -1 && !cursor.isNull(classroomIdIndex)) ?
                        cursor.getInt(classroomIdIndex) : null;

                Message message = new Message(
                        cursor.getInt(cursor.getColumnIndexOrThrow("message_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("sender_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("receiver_id")),
                        classroomIdValue,
                        cursor.getString(cursor.getColumnIndexOrThrow("content")),
                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("timestamp")), formatter)
                );
                messages.add(message);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return messages;
    }
}
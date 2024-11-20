package com.olajideosho.komodohub.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.olajideosho.komodohub.data.local.DBHelper;
import com.olajideosho.komodohub.data.model.Classroom;
import java.util.ArrayList;
import java.util.List;

public class ClassroomRepository {
    private DBHelper dbHelper;

    public ClassroomRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertClassroom(Classroom classroom) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("classroom_name", classroom.getClassroomName());
        values.put("teacher_id", classroom.getTeacherId());
        long classroomId = db.insert("Classroom", null, values);
        db.close();
        return classroomId;
    }

    public Classroom getClassroomById(int classroomId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"classroom_id", "classroom_name", "teacher_id"};
        String selection = "classroom_id = ?";
        String[] selectionArgs = {String.valueOf(classroomId)};
        Cursor cursor = db.query("Classroom", columns, selection, selectionArgs, null, null, null);
        Classroom classroom = null;
        if (cursor != null && cursor.moveToFirst()) {
            classroom = new Classroom(
                    cursor.getInt(cursor.getColumnIndexOrThrow("classroom_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("classroom_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
            );
            cursor.close();
        }
        db.close();
        return classroom;
    }

    public Classroom getClassroomByName(String classroomName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"classroom_id", "classroom_name", "teacher_id"};
        String selection = "classroom_name = ?";
        String[] selectionArgs = {classroomName};
        Cursor cursor = db.query("Classroom", columns, selection, selectionArgs, null, null, null);
        Classroom classroom = null;
        if (cursor != null && cursor.moveToFirst()) {
            classroom = new Classroom(
                    cursor.getInt(cursor.getColumnIndexOrThrow("classroom_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("classroom_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
            );
            cursor.close();
        }
        db.close();
        return classroom;
    }

    public List<Classroom> getAllClassrooms() {
        List<Classroom> classrooms = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"classroom_id", "classroom_name", "teacher_id"};
        Cursor cursor = db.query("Classroom", columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Classroom classroom = new Classroom(
                        cursor.getInt(cursor.getColumnIndexOrThrow("classroom_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("classroom_name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
                );
                classrooms.add(classroom);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return classrooms;
    }

    public Classroom getClassroomByTeacherId(int teacherId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"classroom_id", "classroom_name", "teacher_id"};
        String selection = "teacher_id = ?";
        String[] selectionArgs = {String.valueOf(teacherId)};
        Cursor cursor = db.query("Classroom", columns, selection, selectionArgs, null, null, null);
        Classroom classroom = null;
        if (cursor != null && cursor.moveToFirst()) {
            classroom = new Classroom(
                    cursor.getInt(cursor.getColumnIndexOrThrow("classroom_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("classroom_name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
            );
            cursor.close();
        }
        db.close();
        return classroom;
    }
}

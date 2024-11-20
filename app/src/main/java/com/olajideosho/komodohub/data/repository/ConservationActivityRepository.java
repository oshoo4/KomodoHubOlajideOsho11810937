package com.olajideosho.komodohub.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.olajideosho.komodohub.data.local.DBHelper;
import com.olajideosho.komodohub.data.model.ConservationActivity;
import java.util.ArrayList;
import java.util.List;

public class ConservationActivityRepository {
    private DBHelper dbHelper;

    public ConservationActivityRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertConservationActivity(ConservationActivity activity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("activity_name", activity.getActivityName());
        values.put("description", activity.getDescription());
        values.put("teacher_id", activity.getTeacherId());
        long activityId = db.insert("ConservationActivity", null, values);
        db.close();
        return activityId;
    }

    public ConservationActivity getConservationActivityById(int activityId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"activity_id", "activity_name", "description", "teacher_id"};
        String selection = "activity_id = ?";
        String[] selectionArgs = {String.valueOf(activityId)};
        Cursor cursor = db.query("ConservationActivity", columns, selection, selectionArgs, null, null, null);
        ConservationActivity activity = null;
        if (cursor != null && cursor.moveToFirst()) {
            activity = new ConservationActivity(
                    cursor.getInt(cursor.getColumnIndexOrThrow("activity_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("activity_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
            );
            cursor.close();
        }
        db.close();
        return activity;
    }

    public List<ConservationActivity> getAllConservationActivities() {
        List<ConservationActivity> activities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"activity_id", "activity_name", "description", "teacher_id"};
        Cursor cursor = db.query("ConservationActivity", columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ConservationActivity activity = new ConservationActivity(
                        cursor.getInt(cursor.getColumnIndexOrThrow("activity_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("activity_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
                );
                activities.add(activity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return activities;
    }

    public List<ConservationActivity> getConservationActivitiesByTeacherId(int teacherId) {
        List<ConservationActivity> activities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"activity_id", "activity_name", "description", "teacher_id"};
        String selection = "teacher_id = ?";
        String[] selectionArgs = {String.valueOf(teacherId)};
        Cursor cursor = db.query("ConservationActivity", columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ConservationActivity activity = new ConservationActivity(
                        cursor.getInt(cursor.getColumnIndexOrThrow("activity_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("activity_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
                );
                activities.add(activity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return activities;
    }

    public int getClassroomIdForActivity(int activityId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"classroom_id"};
        String selection = "activity_id = ?";
        String[] selectionArgs = {String.valueOf(activityId)};
        Cursor cursor = db.query("ClassroomActivity", columns, selection, selectionArgs, null, null, null);
        int classroomId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            classroomId = cursor.getInt(cursor.getColumnIndexOrThrow("classroom_id"));
            cursor.close();
        }
        db.close();
        return classroomId;
    }

    public int updateConservationActivity(ConservationActivity activity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("activity_name", activity.getActivityName());
        values.put("description", activity.getDescription());
        String selection = "activity_id = ?";
        String[] selectionArgs = {String.valueOf(activity.getActivityId())};
        int count = db.update("ConservationActivity", values, selection, selectionArgs);
        db.close();
        return count;
    }

    public void deleteConservationActivity(int activityId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "activity_id = ?";
        String[] selectionArgs = {String.valueOf(activityId)};
        db.delete("ConservationActivity", selection, selectionArgs);
        db.close();
    }

    public void addActivityToClassroom(long activityId, int classroomId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("activity_id", activityId);
        values.put("classroom_id", classroomId);
        db.insert("ClassroomActivity", null, values);
        db.close();
    }

    public void deleteActivityFromClassroom(long activityId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "activity_id = ?";
        String[] whereArgs = {String.valueOf(activityId)};
        db.delete("ClassroomActivity", whereClause, whereArgs);
        db.close();
    }

    public List<ConservationActivity> getConservationActivitiesByClassroomId(int classroomId) {
        List<ConservationActivity> activities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT ca.* " +
                "FROM ConservationActivity ca " +
                "INNER JOIN ClassroomActivity cla ON ca.activity_id = cla.activity_id " +
                "WHERE cla.classroom_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classroomId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ConservationActivity activity = new ConservationActivity(
                        cursor.getInt(cursor.getColumnIndexOrThrow("activity_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("activity_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("teacher_id"))
                );
                activities.add(activity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return activities;
    }
}
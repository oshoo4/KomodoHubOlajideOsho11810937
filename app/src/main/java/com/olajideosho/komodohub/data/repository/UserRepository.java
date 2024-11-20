package com.olajideosho.komodohub.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.olajideosho.komodohub.data.local.DBHelper;
import com.olajideosho.komodohub.data.model.User;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private DBHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String bcryptHashString = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());

        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());
        values.put("email", user.getEmail());
        values.put("password", bcryptHashString);
        values.put("role", user.getRole());
        long userId = db.insert("User", null, values);
        db.close();
        return userId;
    }

    public void addUserToClassroom(int userId, int classroomId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("classroom_id", classroomId);
        db.insert("UserClassroom", null, values);
        db.close();
    }

    public int getClassroomByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"classroom_id"};
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query("UserClassroom", columns, selection, selectionArgs, null, null, null);
        int classroomId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            classroomId = cursor.getInt(cursor.getColumnIndexOrThrow("classroom_id"));
            cursor.close();
        }
        db.close();
        return classroomId;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"user_id", "first_name", "last_name", "email", "password", "role"};
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query("User", columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("first_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("role"))
            );
            cursor.close();
        }
        db.close();
        return user;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"user_id", "first_name", "last_name", "email", "password", "role"};
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query("User", columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("first_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    cursor.getString(cursor.getColumnIndexOrThrow("role"))
            );
            cursor.close();
        }
        db.close();
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"user_id", "first_name", "last_name", "email", "password", "role"};
        Cursor cursor = db.query("User", columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("first_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("last_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("role"))
                );
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return users;
    }

    public List<Integer> getStudentIdsByClassroomId(int classroomId) {
        List<Integer> studentIds = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT uc.user_id " +
                "FROM UserClassroom uc " +
                "INNER JOIN User u ON uc.user_id = u.user_id " +
                "WHERE uc.classroom_id = ? AND u.role = 'student'";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(classroomId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                studentIds.add(userId);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return studentIds;
    }

    public boolean checkPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}
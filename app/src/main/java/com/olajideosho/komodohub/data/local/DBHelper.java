package com.olajideosho.komodohub.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "komodo_hub.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    private static final String CREATE_USER_TABLE =
            "CREATE TABLE User (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT," +
                    "last_name TEXT," +
                    "email TEXT," +
                    "password TEXT," +
                    "role TEXT" +
                    ")";

    private static final String CREATE_SCHOOL_TABLE =
            "CREATE TABLE School (" +
                    "school_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "school_name TEXT" +
                    ")";

    private static final String CREATE_CLASSROOM_TABLE =
            "CREATE TABLE Classroom (" +
                    "classroom_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "classroom_name TEXT," +
                    "teacher_id INTEGER," +
                    "FOREIGN KEY (teacher_id) REFERENCES User(user_id)" +
                    ")";

    private static final String CREATE_CONSERVATION_ACTIVITY_TABLE =
            "CREATE TABLE ConservationActivity (" +
                    "activity_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "activity_name TEXT," +
                    "description TEXT," +
                    "teacher_id INTEGER," +
                    "FOREIGN KEY (teacher_id) REFERENCES User(user_id)" +
                    ")";

    private static final String CREATE_SPECIES_TABLE =
            "CREATE TABLE Species (" +
                    "species_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "common_name TEXT," +
                    "scientific_name TEXT," +
                    "description TEXT," +
                    "conservation_status TEXT," +
                    "image_url TEXT" +
                    ")";

    private static final String CREATE_CONTENT_TABLE =
            "CREATE TABLE Content (" +
                    "content_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "content_type TEXT," +
                    "title TEXT," +
                    "body TEXT," +
                    "user_id INTEGER," +
                    "species_id INTEGER," +
                    "activity_id INTEGER," +
                    "FOREIGN KEY (user_id) REFERENCES User(user_id)," +
                    "FOREIGN KEY (species_id) REFERENCES Species(species_id)," +
                    "FOREIGN KEY (activity_id) REFERENCES ConservationActivity(activity_id)" +
                    ")";

    private static final String CREATE_MESSAGE_TABLE =
            "CREATE TABLE Message (" +
                    "message_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sender_id INTEGER," +
                    "receiver_id INTEGER, " +
                    "classroom_id INTEGER, " +
                    "content TEXT," +
                    "timestamp DATETIME," +
                    "FOREIGN KEY (sender_id) REFERENCES User(user_id)," +
                    "FOREIGN KEY (receiver_id) REFERENCES User(user_id)," +
                    "FOREIGN KEY (classroom_id) REFERENCES Classroom(classroom_id)" +
                    ")";

    private static final String CREATE_CLASSROOM_ACTIVITY_TABLE =
            "CREATE TABLE ClassroomActivity (" +
                    "classroom_id INTEGER," +
                    "activity_id INTEGER," +
                    "PRIMARY KEY (classroom_id, activity_id)," +
                    "FOREIGN KEY (classroom_id) REFERENCES Classroom(classroom_id)," +
                    "FOREIGN KEY (activity_id) REFERENCES ConservationActivity(activity_id)" +
                    ")";

    private static final String CREATE_USER_CLASSROOM_TABLE =
            "CREATE TABLE UserClassroom (" +
                    "user_id INTEGER," +
                    "classroom_id INTEGER," +
                    "PRIMARY KEY (user_id, classroom_id)," +
                    "FOREIGN KEY (user_id) REFERENCES User(user_id)," +
                    "FOREIGN KEY (classroom_id) REFERENCES Classroom(classroom_id)" +
                    ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SCHOOL_TABLE);
        db.execSQL(CREATE_CLASSROOM_TABLE);
        db.execSQL(CREATE_CONSERVATION_ACTIVITY_TABLE);
        db.execSQL(CREATE_SPECIES_TABLE);
        db.execSQL(CREATE_CONTENT_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.execSQL(CREATE_CLASSROOM_ACTIVITY_TABLE);
        db.execSQL(CREATE_USER_CLASSROOM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
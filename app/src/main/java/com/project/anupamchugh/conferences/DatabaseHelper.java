package com.project.anupamchugh.conferences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.anupamchugh.conferences.models.AllConferences;
import com.project.anupamchugh.conferences.models.NewConferences;
import com.project.anupamchugh.conferences.models.Topics;
import com.project.anupamchugh.conferences.models.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by anupamchugh on 30/12/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "API123";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "conferencesApplication";

    // Table Names
    private static final String TABLE_CONF = "conference";
    private static final String TABLE_USERS = "user";
    private static final String TABLE_CONF_USERS = "conf_users";
    private static final String TABLE_TOPICS = "topics";
    private static final String TABLE_TOPICS_USERS = "user_topics";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_MODIFIED_AT = "modified_at";

    // CONFERENCES Table - column nmaes
    private static final String KEY_CONF_DESCRIPTION = "description";
    private static final String KEY_CONF_CALENDAR_DATE = "calendar_date";

    // USERS Table - column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_ADMIN = "is_admin";

    // CONF_USERS Table - column names
    private static final String KEY_CONF_ID = "conf_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ACCEPTED = "accepted";
    private static final String KEY_REJECTED = "rejected";


    // TOPICS Table - column names
    private static final String KEY_TOPIC_NAME = "topic_name";


    // Table Create Statements
    // USER table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USERNAME
            + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_IS_ADMIN + " INTEGER," + KEY_CREATED_AT
            + " DATETIME," + KEY_MODIFIED_AT + " DATETIME" + ")";

    // CONFERENCES table create statement
    private static final String CREATE_TABLE_CONF = "CREATE TABLE " + TABLE_CONF
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USERNAME + " TEXT," + KEY_CONF_DESCRIPTION + " TEXT," + KEY_CONF_CALENDAR_DATE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME," + KEY_MODIFIED_AT + " DATETIME" + ")";


    // TOPICS table create statement
    private static final String CREATE_TABLE_TOPICS = "CREATE TABLE " + TABLE_TOPICS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TOPIC_NAME + " TEXT," + KEY_USERNAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";


    // conf_users table create statement
    private static final String CREATE_TABLE_CONF_USERS = "CREATE TABLE "
            + TABLE_CONF_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USERNAME + " TEXT,"
            + KEY_CONF_ID + " INTEGER," + KEY_USER_ID + " INTEGER,"
            + KEY_ACCEPTED + " INTEGER," + KEY_REJECTED + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME," + KEY_MODIFIED_AT + " DATETIME" + ")";

    /*// topics_users table create statement
    private static final String CREATE_TABLE_TOPICS_USERS = "CREATE TABLE "
            + TABLE_TOPICS_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TOPIC_ID + " INTEGER," + KEY_USER_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME," + KEY_MODIFIED_AT + " DATETIME" + ")";*/

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CONF);
        db.execSQL(CREATE_TABLE_TOPICS);
        db.execSQL(CREATE_TABLE_CONF_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONF_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "todos" table methods ----------------//

    /*
     * Creating a todo
     */
    /*public long createToDo(Todo todo, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long todo_id = db.insert(TABLE_TODO, null, values);

        // insert tag_ids
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }

        return todo_id;
    }*/


    public long creatNewUser(Users user)
    // code to add the new register
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.username);
        values.put(KEY_PASSWORD, user.password);
        values.put(KEY_IS_ADMIN, user.isAdmin);
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_MODIFIED_AT, getDateTime());

        long id = db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection

        return id;

    }

    public long creatNewConf(NewConferences conference)
    // code to add the new register
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, conference.createdBy);
        values.put(KEY_CONF_DESCRIPTION, conference.description);
        values.put(KEY_CONF_CALENDAR_DATE, conference.date);
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_MODIFIED_AT, getDateTime());

        long conf_id = db.insert(TABLE_CONF, null, values);
        db.close(); // Closing database connection

        return conf_id;

    }

    public void createNewTopic(Topics topics)
    // code to add the new register
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOPIC_NAME, topics.description);
        values.put(KEY_USERNAME, topics.createdBy);
        values.put(KEY_CREATED_AT, getDateTime());

        db.insert(TABLE_TOPICS, null, values);
        db.close(); // Closing database connection

    }

    public long inviteDoctor(Users user, long conf_id)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.username);
        values.put(KEY_USER_ID, user.user_id);
        values.put(KEY_CONF_ID, conf_id);
        values.put(KEY_ACCEPTED, 0);
        values.put(KEY_REJECTED, 0);
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_MODIFIED_AT, getDateTime());

        long id = db.insert(TABLE_CONF_USERS, null, values);
        db.close(); // Closing database connection


        Log.d("API123", "inviteDctor " + id);

        return id;

    }

    public void deleteInviteToDoctor(long user_id, long conf_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONF_USERS, KEY_USER_ID + " = ? AND " + KEY_CONF_ID + " = ? ",
                new String[]{String.valueOf(user_id), String.valueOf(conf_id)});
    }


    public List<Users> getAllDoctors(int isAdmin) {
        List<Users> doctors = new ArrayList<Users>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_IS_ADMIN + " = " + isAdmin;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Users user = new Users();

                user.username = c.getString(c.getColumnIndex(KEY_USERNAME));
                user.user_id = c.getInt(c.getColumnIndex(KEY_ID));

                Log.d("API123", "username " + user.username);

                doctors.add(user);

            } while (c.moveToNext());
        }

        return doctors;
    }

    public List<Users> getAllInvitedDoctors(long conf_id) {
        List<Users> doctors = new ArrayList<Users>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONF_USERS + " WHERE "
                + KEY_CONF_ID + " = " + conf_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Users user = new Users();

                user.username = c.getString(c.getColumnIndex(KEY_USERNAME));
                user.user_id = c.getInt(c.getColumnIndex(KEY_USER_ID));

                Log.d("API123", "username " + user.username);

                doctors.add(user);

            } while (c.moveToNext());
        }

        return doctors;
    }


    public List<AllConferences> getAllConferences() {
        List<AllConferences> allConferencesList = new ArrayList<AllConferences>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONF;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                AllConferences allConferences = new AllConferences();

                allConferences.date = c.getString(c.getColumnIndex(KEY_CONF_CALENDAR_DATE));
                allConferences.description = c.getString(c.getColumnIndex(KEY_CONF_DESCRIPTION));
                allConferences.createdBy = c.getString(c.getColumnIndex(KEY_USERNAME));
                allConferences.createdAt = c.getString(c.getColumnIndex(KEY_CREATED_AT));
                allConferences.modifiedAt = c.getString(c.getColumnIndex(KEY_MODIFIED_AT));
                allConferences.conf_id = c.getLong(c.getColumnIndex(KEY_ID));


                allConferencesList.add(allConferences);

            } while (c.moveToNext());
        }

        return allConferencesList;
    }

    public List<Topics> getAllTopics() {
        List<Topics> allConferencesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOPICS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Topics allConferences = new Topics();

                allConferences.description = c.getString(c.getColumnIndex(KEY_TOPIC_NAME));
                allConferences.createdBy = c.getString(c.getColumnIndex(KEY_USERNAME));
                allConferences.createdAt = c.getString(c.getColumnIndex(KEY_CREATED_AT));


                allConferencesList.add(allConferences);

            } while (c.moveToNext());
        }

        return allConferencesList;
    }


    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public boolean checkIfUserNameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, KEY_USERNAME + "=?", new String[]{username}, null, null, null, null);

        if (cursor.getCount() == 0) {

            cursor.close();
            return false;
        } else if (cursor.getCount() >= 1 && cursor.moveToFirst()) {
            cursor.close();
            return true;

        }

        return false;

    }

    public ArrayList<String> getKeyPassword(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String selectquery="SELECT * FROM TABLE_REGISTER";
        Cursor cursor = db.query(TABLE_USERS, null, KEY_USERNAME + "=?", new String[]{username}, null, null, null, null);

        ArrayList<String> returnData = new ArrayList<>();

        if (cursor.getCount() == 0) {
            cursor.close();
            return returnData;
        } else if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

            String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            String isAdmin = String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_IS_ADMIN)));
            String user_id = String.valueOf(cursor.getLong(cursor.getColumnIndex(KEY_ID)));

            returnData.add(password);
            returnData.add(isAdmin);
            returnData.add(user_id);

            cursor.close();

            return returnData;

        }

        return returnData;


    }

    public Users getStatusForUser(long user_id, long conf_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String selectquery="SELECT * FROM TABLE_REGISTER";
        Cursor cursor = db.query(TABLE_CONF_USERS, null, KEY_USER_ID + "=? AND " + KEY_CONF_ID + " = ? ", new String[]{String.valueOf(user_id), String.valueOf(conf_id)}, null, null, null, null);

        Users users = new Users();

        if (cursor.getCount() == 0) {
            cursor.close();
            return users;
        } else if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

            long id = cursor.getLong(cursor.getColumnIndex(KEY_USER_ID));
            int accepted = Integer.parseInt(String.valueOf(cursor.getLong(cursor.getColumnIndex(KEY_ACCEPTED))));
            int rejected = Integer.parseInt(String.valueOf(cursor.getLong(cursor.getColumnIndex(KEY_REJECTED))));

            users.accepted = accepted;
            users.rejected = rejected;
            users.user_id = id;

            cursor.close();

            return users;

        }

        return users;


    }


    public ArrayList<String> getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String selectquery="SELECT * FROM TABLE_REGISTER";
        Cursor cursor = db.query(TABLE_USERS, null, KEY_USERNAME + "=?", new String[]{username}, null, null, null, null);

        ArrayList<String> returnData = new ArrayList<>();

        if (cursor.getCount() == 0) {
            cursor.close();
            return returnData;
        } else if (cursor.getCount() >= 1 && cursor.moveToFirst()) {

            String user_id = cursor.getString(cursor.getColumnIndex(KEY_USER_ID));
            String isAdmin = String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_IS_ADMIN)));

            returnData.add(user_id);
            returnData.add(isAdmin);

            cursor.close();

            return returnData;

        }

        return returnData;


    }

    public int updateConference(String description, String date, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONF_CALENDAR_DATE, date);
        values.put(KEY_CONF_DESCRIPTION, description);
        values.put(KEY_MODIFIED_AT, getDateTime());

        // updating row
        return db.update(TABLE_CONF, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public int statusAccepted(long user_id, long conf_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACCEPTED, 1);
        values.put(KEY_REJECTED, 0);
        values.put(KEY_MODIFIED_AT, getDateTime());

        // updating row
        return db.update(TABLE_CONF_USERS, values, KEY_USER_ID + " = ? AND " + KEY_CONF_ID + " = ? ",
                new String[]{String.valueOf(user_id), String.valueOf(conf_id)});
    }

    public int statusRejected(long user_id, long conf_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACCEPTED, 0);
        values.put(KEY_REJECTED, 1);
        values.put(KEY_MODIFIED_AT, getDateTime());

        // updating row
        return db.update(TABLE_CONF_USERS, values, KEY_USER_ID + " = ? AND " + KEY_CONF_ID + " = ? ",
                new String[]{String.valueOf(user_id), String.valueOf(conf_id)});
    }

    public int deleteConference(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONF, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteConferenceLinks(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONF_USERS, KEY_CONF_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public List<AllConferences> getAllConfForUserId(long user_id) {
        List<AllConferences> allConferencesList = new ArrayList<AllConferences>();

        String selectQuery = "SELECT  * FROM " + TABLE_CONF + " conf, "
                + TABLE_USERS + " usr, " + TABLE_CONF_USERS + " cu WHERE usr."
                + KEY_ID + " = '" + user_id + "'" + " AND usr." + KEY_ID
                + " = " + "cu." + KEY_USER_ID + " AND conf." + KEY_ID + " = "
                + "cu." + KEY_CONF_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                AllConferences allConferences = new AllConferences();
                allConferences.date = c.getString(c.getColumnIndex(KEY_CONF_CALENDAR_DATE));
                allConferences.description = c.getString(c.getColumnIndex(KEY_CONF_DESCRIPTION));
                allConferences.createdBy = c.getString(c.getColumnIndex(KEY_USERNAME));
                allConferences.createdAt = c.getString(c.getColumnIndex(KEY_CREATED_AT));
                allConferences.modifiedAt = c.getString(c.getColumnIndex(KEY_MODIFIED_AT));
                allConferences.conf_id = c.getLong(c.getColumnIndex(KEY_ID));
                Log.d("API123", "allConferences " + allConferences.conf_id);
                allConferencesList.add(allConferences);
            } while (c.moveToNext());
        }

        return allConferencesList;
    }

}


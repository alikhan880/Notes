package kz.kbtu.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by abakh on 22-Jun-17.
 */

public class Database extends SQLiteOpenHelper {

    //Database
    private static final String DATABASE_NAME = "NotesAppDatabase";
    private static final int DATABASE_VERSION = 1;

    //Table Users
    private static final String TABLE_NAME_USERS = "users_table";
    private static final String COL_USERS_ID = "id";
    private static final String COL_USERS_NAME= "username";
    private static final String COL_USERS_PASSWORD = "password";


    //Notes
    private static final String TABLE_NAME_NOTES = "notes_table";
    private static final String COL_NOTES_ID = "id";
    private static final String COL_NOTES_TEXT = "text";
    private static final String COL_NOTES_AUTHOR = "author";
    private static final String COL_NOTES_STATUS = "status";
    private static final String COL_NOTES_DATE = "date";


    //Status
    private static final String TABLE_NAME_STATUS = "status_table";
    private static final String COL_STATUS_ID = "id";
    private static final String COL_STATUS_NAME = "name";
    private static final String COL_STATUS_AUTHOR = "author";
    private static final String TAG = "DEBUG" ;


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "create table " + TABLE_NAME_USERS + "(" +
                COL_USERS_ID + " integer primary key autoincrement, " +
                COL_USERS_NAME + " text unique, " +
                COL_USERS_PASSWORD + " text not null)";

        String createTableNotes = "create table " + TABLE_NAME_NOTES + "(" +
                COL_NOTES_ID + " integer primary key autoincrement, " +
                COL_NOTES_TEXT + " text, " +
                COL_NOTES_AUTHOR + " integer, " +
                COL_NOTES_STATUS + " integer, " +
                COL_NOTES_DATE + " datetime)";

        String createTableStatus = "create table " + TABLE_NAME_STATUS + "(" +
                COL_STATUS_ID + " integer primary key autoincrement, " +
                COL_STATUS_NAME + " text unique not null, " +
                COL_STATUS_AUTHOR + " integer not null)";

        db.execSQL(createTableUsers);
        db.execSQL(createTableNotes);
        db.execSQL(createTableStatus);
        Log.d(TAG, createTableUsers);
        Log.d(TAG, createTableNotes);
        Log.d(TAG, createTableStatus);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String commandUsers = "DROP TABLE IF EXISTS " + TABLE_NAME_USERS;
        String commandNotes = "DROP TABLE IF EXISTS " + TABLE_NAME_NOTES;
        String commandStatus = "DROP TABLE IF EXISTS " + TABLE_NAME_STATUS;
        db.execSQL(commandStatus);
        db.execSQL(commandUsers);
        db.execSQL(commandNotes);
        onCreate(db);
    }

    public Boolean createUser(String name, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COL_USERS_NAME, name);
        content.put(COL_USERS_PASSWORD, password);
        long result = db.insert(TABLE_NAME_USERS, null, content);
        return result != -1;
    }

    public Boolean checkUserExist(String username){
        String query = "select distinct " + COL_USERS_NAME + " from " + TABLE_NAME_USERS + " where "
                       + COL_USERS_NAME + " = '" + username + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount() > 0;
    }


    public User getUser(String username){
        String query = "select distinct * from " + TABLE_NAME_USERS + " where "
                    + COL_USERS_NAME + " = '" + username + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        return new User(cursor.getString(cursor.getColumnIndex(COL_USERS_ID)),
                cursor.getString(cursor.getColumnIndex(COL_USERS_NAME)),
                cursor.getString(cursor.getColumnIndex(COL_USERS_PASSWORD)));
    }


    public ArrayList<Note> getAllNotes(String id){
        ArrayList<Note> notes = new ArrayList<>();
        String query = "select * from " + TABLE_NAME_NOTES + " where " + COL_NOTES_AUTHOR + " = CAST(" + id + " as integer)";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            notes.add(new Note(
                    cursor.getString(cursor.getColumnIndex(COL_NOTES_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_NOTES_TEXT)),
                    cursor.getString(cursor.getColumnIndex(COL_NOTES_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(COL_NOTES_STATUS)),
                    cursor.getString(cursor.getColumnIndex(COL_NOTES_DATE))));
            cursor.moveToNext();
        }
        return notes;
    }

    public Boolean addStatus(Status status, User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COL_STATUS_NAME, status.getName());
        content.put(COL_STATUS_AUTHOR, user.getId());
        long res = db.insert(TABLE_NAME_STATUS, null, content);
        return res != -1;
    }

    public ArrayList<Status> getAllStatuses(User user){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Status> statuses = new ArrayList<>();
        String cmd = "select * from "+ TABLE_NAME_STATUS + " where " + COL_STATUS_AUTHOR + " = cast(" + user.getId() + " as integer)";
        Cursor cursor = db.rawQuery(cmd, null);
        Log.d(TAG, cursor.getCount() + "");
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            String id = cursor.getString(cursor.getColumnIndex(COL_STATUS_ID));
            String text = cursor.getString(cursor.getColumnIndex(COL_STATUS_NAME));
            String author = cursor.getString(cursor.getColumnIndex(COL_STATUS_AUTHOR));
            statuses.add(new Status(id, text, author));
            cursor.moveToNext();
        }

        return statuses;
    }

    public void deleteStatus(Status status){
        SQLiteDatabase db = getWritableDatabase();
        String cmd = "delete from " + TABLE_NAME_STATUS + " where " + COL_STATUS_ID + " = cast(" + status.getId() +
                " as integer)";
        db.execSQL(cmd);
    }

    public void updateStatus(Status status){
        SQLiteDatabase db = getWritableDatabase();
        String cmd = "update " + TABLE_NAME_STATUS + " set " +
                COL_STATUS_NAME + " = '" + status.getName() +
                "' where " + COL_STATUS_ID + " = cast(" + status.getId() + " as integer)";
        db.execSQL(cmd);
    }


}

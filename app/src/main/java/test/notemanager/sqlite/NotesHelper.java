package test.notemanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import test.notemanager.models.Note;
import test.notemanager.utils.BitmapUtils;

public class NotesHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notes.db";
    public static final String NOTE_TABLE_NAME = "note";
    public static final String NOTE_COLUMN_ID = "_id";
    public static final int NOTE_COLUMN_ID_NUMBER = 0;
    public static final String NOTE_COLUMN_HEAD = "head";
    public static final int NOTE_COLUMN_HEAD_NUMBER = 1;
    public static final String NOTE_COLUMN_CONTENT = "content";
    public static final int NOTE_COLUMN_CONTENT_NUMBER = 2;
    public static final String NOTE_COLUMN_PRIORITY = "priority";
    public static final int NOTE_COLUMN_PRIORITY_NUMBER = 3;
    public static final String NOTE_COLUMN_IMAGE = "photo";
    public static final int NOTE_COLUMN_IMAGE_NUMBER = 4;

    public NotesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NOTE_TABLE_NAME + " ( " +
                NOTE_COLUMN_ID + " integer primary key autoincrement, " +
                NOTE_COLUMN_HEAD + " text , " +
                NOTE_COLUMN_CONTENT + " text,  " +
                NOTE_COLUMN_IMAGE + " blob,  " +
                NOTE_COLUMN_PRIORITY + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + NOTE_TABLE_NAME);
        onCreate(db);
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_HEAD, note.getHead());
        values.put(NOTE_COLUMN_CONTENT, note.getContent());
        values.put(NOTE_COLUMN_PRIORITY, note.getPriority().toString());
        if (note.getPhoto() != null)
            values.put(NOTE_COLUMN_IMAGE, BitmapUtils.getBytes(note.getPhoto()));

        return db.update(NOTE_TABLE_NAME, values, NOTE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_HEAD, note.getHead());
        values.put(NOTE_COLUMN_CONTENT, note.getContent());
        values.put(NOTE_COLUMN_PRIORITY, note.getPriority().toString());
        if (note.getPhoto() != null)
            values.put(NOTE_COLUMN_IMAGE, BitmapUtils.getBytes(note.getPhoto()));

        db.insert(NOTE_TABLE_NAME, null, values);
        db.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE_NAME, NOTE_COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public Cursor selectAllNotes() {
        String selectQuery = "select * from " + NOTE_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int getNotesCount() {
        String countQuery = "select * from " + NOTE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }


}

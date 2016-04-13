package test.notemanager.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import test.notemanager.models.Note;

public class NotesHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notes.db";
    public static final String NOTE_TABLE_NAME = "note";
    public static final String NOTE_COLUMN_ID = "_id";              //col 0
    public static final String NOTE_COLUMN_HEAD = "head";           //col 1
    public static final String NOTE_COLUMN_CONTENT = "content";     //col 2
    public static final String NOTE_COLUMN_IMAGE = "photo";         //col 3
    public static final String NOTE_COLUMN_PRIORITY = "priority";   //col 4
    public static final String NOTE_COLUMN_LATITUDE = "latitude";   //col 5
    public static final String NOTE_COLUMN_LONGITUDE = "longitude"; //col 6

    public NotesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NOTE_TABLE_NAME + " ( " +
                NOTE_COLUMN_ID + " integer primary key autoincrement, " +
                NOTE_COLUMN_HEAD + " text , " +
                NOTE_COLUMN_CONTENT + " text,  " +
                NOTE_COLUMN_IMAGE + " text,  " +
                NOTE_COLUMN_PRIORITY + " text," +
                NOTE_COLUMN_LATITUDE + " real," +
                NOTE_COLUMN_LONGITUDE + " real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + NOTE_TABLE_NAME);
        onCreate(db);
    }

    private void updateNoteImageUriNull(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (note.getImageUri() != null)
            values.put(NOTE_COLUMN_IMAGE, note.getImageUri().toString());
    }

    private void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_HEAD, note.getHead());
        values.put(NOTE_COLUMN_CONTENT, note.getContent());
        values.put(NOTE_COLUMN_PRIORITY, note.getPriority().toString());
        if (note.getImageUri() != null)
            values.put(NOTE_COLUMN_IMAGE, note.getImageUri().toString());
        if (note.getLocation() != null) {
            values.put(NOTE_COLUMN_LATITUDE, note.getLocation().latitude);
            values.put(NOTE_COLUMN_LONGITUDE, note.getLocation().longitude);
        }
            db.update(NOTE_TABLE_NAME, values, NOTE_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public Note saveNote(Note note) {
        if (note.getId() == null) note.setId(insertNote(note));
        else updateNote(note);
        return note;
    }

    private int insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_HEAD, note.getHead());
        values.put(NOTE_COLUMN_CONTENT, note.getContent());
        values.put(NOTE_COLUMN_PRIORITY, note.getPriority().toString());
        if (note.getImageUri() != null)
            values.put(NOTE_COLUMN_IMAGE, note.getImageUri().toString());
        if (note.getLocation() != null) {
            values.put(NOTE_COLUMN_LATITUDE, note.getLocation().latitude);
            values.put(NOTE_COLUMN_LONGITUDE, note.getLocation().longitude);
        }

        int id = (int) db.insert(NOTE_TABLE_NAME, null, values);
        db.close();
        return id;
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

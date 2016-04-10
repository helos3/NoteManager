package test.notemanager.models;

import android.database.Cursor;
import android.graphics.Bitmap;

import java.io.Serializable;

import test.notemanager.utils.BitmapUtils;

public class Note implements Serializable {
    Integer id;
    String head;
    String content;
    Priority priority;
    Bitmap photo;


    public static Note fromCursor(Cursor cursor) {
        String head = cursor.getString(1);
        String content = cursor.getString(2);
        Priority priority = Priority.valueOf(cursor.getString(4));
        Note note;
        if (cursor.getBlob(3) != null) {
            Bitmap photo = BitmapUtils.getImage(cursor.getBlob(3));
            note = new Note(head, content, photo, priority);
        }
        note = new Note(head, content, priority);
        note.setId(cursor.getInt(0));
        return note;
    }

    public Note(String head, String content, Priority priority) {
        this.priority = priority;
        this.content = content;
        this.head = head;
    }

    public Note(String head, String content) {
        this(head, content, Priority.NO_PRIORITY);
    }

    public Note(String head, String content, Bitmap image, Priority priority) {
        this.head = head;
        this.content = content;
        this.photo = image;
        this.priority = priority;
    }

    public Note(String head, String content, Bitmap image) {
        this(head, content, image, Priority.NO_PRIORITY);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPhoto(Bitmap image) {
        this.photo = image;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getHead() {
        return head;
    }

    public String getContent() {
        return content;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public Priority getPriority() {
        return priority;
    }
}

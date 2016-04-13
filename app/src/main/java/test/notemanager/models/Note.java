package test.notemanager.models;

import android.database.Cursor;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Note implements Serializable {
    Integer id;
    String head;
    String content;
    Priority priority;
    Uri imageUri;
    LatLng location;

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Uri getImageUri() {

        return imageUri;
    }

    public LatLng getLocation() {
        return location;
    }

    public static Note fromCursor(Cursor cursor) {
        Note note;
        String head = cursor.getString(1);
        String content = cursor.getString(2);
        Priority priority = Priority.valueOf(cursor.getString(4));
        note = new Note(head, content, priority);
        if (cursor.getString(3) != null) {
            Uri imageUri = Uri.parse(cursor.getString(3));
            note.setImageUri(imageUri);
        }
        if (cursor.isNull(5)) {
            LatLng location = new LatLng(cursor.getDouble(5), cursor.getDouble(6));
        }


//        if (cursor.getBlob(3) != null) {
//            Bitmap photo = BitmapUtils.getImage(cursor.getBlob(3));
//            note = new Note(head, content, photo, priority);
//        }
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

    public Note(String head, String content, Uri imageUri, Priority priority) {
        this.head = head;
        this.content = content;
        this.priority = priority;
    }

    public Note(String head, String content, Uri imageUri) {
        this(head, content, imageUri, Priority.NO_PRIORITY);
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


    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getHead() {
        return head;
    }

    public String getContent() {
        return content;
    }

    public Priority getPriority() {
        return priority;
    }
}

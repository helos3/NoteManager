package test.notemanager.components;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import test.notemanager.R;
import test.notemanager.models.Note;
import test.notemanager.models.Priorities;
import test.notemanager.utils.StringUtils;

public class NotesCursorAdapter extends CursorRecyclerViewAdapterAdapter<NotesCursorAdapter.NoteViewHolder> {

    public NotesCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.view_note_list_item, parent, false);
        NoteViewHolder vh = new NoteViewHolder(itemView);
        return vh;
    }

    public Note getNote(int position) {
        Cursor cursor = super.getItem(position);
        Note note = cursor == null ? null : Note.fromCursor(cursor);
        return note;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder viewHolder, Cursor cursor) {
        Note note = Note.fromCursor(cursor);
        viewHolder.mHead.setText(note.getHead());
        if (note.getImageUri() != null) {
            try {
                viewHolder.mIcon.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), note.getImageUri()));
            } catch (Exception e) {e.printStackTrace();}
        }
        viewHolder.mContent.setText(StringUtils.createBriefContent(note.getContent()));
        viewHolder.mHead.setText(note.getHead());
        viewHolder.mPriority.setImageResource(Priorities.getEnumInstance().get(note.getPriority()));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIcon;
        public final TextView mHead;
        public final TextView mContent;
        public final ImageView mPriority;

        public NoteViewHolder(View view) {
            super(view);
            mView = view;
            mIcon = (ImageView) view.findViewById(R.id.item_note_icon);
            mPriority = (ImageView) view.findViewById(R.id.item_note_priority);
            mHead = (TextView) view.findViewById(R.id.item_note_head);
            mContent = (TextView) view.findViewById(R.id.item_note_content);
        }

    }

}

package test.notemanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.notemanager.R;
import test.notemanager.components.NotesCursorAdapter;
import test.notemanager.listeners.RecyclerViewItemListener;
import test.notemanager.models.Note;
import test.notemanager.sqlite.NotesHelper;

public class FragmentNotes extends Fragment {

    OnNoteTouchListener mCallback;

    public interface OnNoteTouchListener {
        void onItemSelected(Note note);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static FragmentNotes newInstance() {
        FragmentNotes fragment = new FragmentNotes();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.note_list);
        NotesHelper db = new NotesHelper(getActivity());
        Context context = rootView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new NotesCursorAdapter(context, db.selectAllNotes()));
        recyclerView.addOnItemTouchListener(new RecyclerViewItemListener(getActivity(), recyclerView, new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCallback.onItemSelected(((NotesCursorAdapter) recyclerView.getAdapter()).getNote(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnNoteTouchListener) {
            mCallback = (OnNoteTouchListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

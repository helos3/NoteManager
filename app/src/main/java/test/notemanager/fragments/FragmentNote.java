package test.notemanager.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import test.notemanager.R;
import test.notemanager.models.Note;
import test.notemanager.sqlite.NotesHelper;

public class FragmentNote extends Fragment {

    private Note mNote;
    private NotesHelper mHelper;

    private OnFragmentInteractionListener mListener;


    public void setNote(Note mNote) {
        this.mNote = mNote;
    }

    public FragmentNote() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("logs", getClass().getSimpleName() + "onActivityCreated()");

        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            mNote = (Note) savedInstanceState.getSerializable("mNote");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mNote", mNote);
    }

    public static FragmentNote newInstance(Note note) {
        FragmentNote fragment = new FragmentNote();
//        fragment.setNote(note);
        Bundle args = new Bundle();
        args.putSerializable("mNote", note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("logs", getClass().getSimpleName() + "onCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("logs", getClass().getSimpleName() + "onCreateView()");

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.content_view);
        EditText editText = (EditText) rootView.findViewById(R.id.content_edit);
        textView.setText(mNote.getContent());
        editText.setText(mNote.getContent());
        getActivity().setTitle(mNote.getHead());

        ViewSwitcher viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.content_switcher);
//        viewSwitcher.
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("logs", getClass().getSimpleName() + "onAttach()");
        mNote = (Note) getArguments().getSerializable("mNote");
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mHelper = new NotesHelper(getActivity());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_share:
                shareOption();
                break;
            case R.id.menu_save:
                saveOption();
                break;
            case R.id.menu_edit:
                editOption();
                break;
            case R.id.menu_delete:
                deleteOption();
                break;
            case R.id.menu_update_photo:
                updatePhotoOption();
                break;
            case R.id.menu_rename_head:
                renameHeadOption();
                break;
            case R.id.menu_choose_priority:
                choosePriorityOption();
                break;
            case R.id.menu_insert_gps:
                insertOrUpdateGPSOption();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }

    private void saveOption() {

    }

    private void deleteOption() {

    }

    private void editOption() {

    }

    private void updatePhotoOption() {

    }

    private void renameHeadOption() {

    }

    private void choosePriorityOption() {

    }

    private void insertOrUpdateGPSOption() {

    }

    private void shareOption() {

    }

}

package test.notemanager.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import test.notemanager.R;
import test.notemanager.models.Note;
import test.notemanager.sqlite.NotesHelper;

public class FragmentNote extends Fragment {

    private Note mNote;
    private NotesHelper mHelper;
    private ViewSwitcher viewSwitcher;
    private OnFragmentInteractionListener mListener;
    private boolean isEditable, isNewNote;
    private TextView contentTextView;
    private EditText contentEditText;


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

    public static FragmentNote newInstance(Note note, boolean newNote) {
        FragmentNote fragment = new FragmentNote();
        Bundle args = new Bundle();
        args.putBoolean("newNote", newNote);
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
        contentTextView = (TextView) rootView.findViewById(R.id.content_view);
        contentEditText = (EditText) rootView.findViewById(R.id.content_edit);
        contentTextView.setText(mNote.getContent());
        getActivity().setTitle(mNote.getHead());

        viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.content_switcher);

        if (isNewNote) editOption();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("logs", getClass().getSimpleName() + "onAttach()");
        mNote = (Note) getArguments().getSerializable("mNote");
        isNewNote = (boolean) getArguments().getBoolean("newNote");
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mHelper = new NotesHelper(getActivity());
        isEditable = false;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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
            case R.id.menu_props:
                propsOption();
                break;
            case R.id.menu_file:
                importToFile();
                break;
            case R.id.menu_delete:
                deleteOption();
                break;
            case R.id.menu_rename_head:
                renameHeadOption();
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
        void onNoteDeleted();
    }

    private void saveOption() {
        if (isEditable) mNote.setContent(contentEditText.getText().toString());
        else mNote.setContent(contentTextView.getText().toString());
        mNote.setHead(getActivity().getTitle().toString());
        mNote = mHelper.saveNote(mNote);
    }

    private void deleteOption() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Вы уверены?");
        dialogBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mNote.getId() != null) mHelper.deleteNote(mNote);
                mListener.onNoteDeleted();
            }
        });
        dialogBuilder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void editOption() {
        if (!isEditable) {
            contentEditText.setText(contentTextView.getText());
            isEditable = true;
        } else {
            contentTextView.setText(contentEditText.getText());
            isEditable = false;
        }
        viewSwitcher.showNext();
    }

    private void renameHeadOption() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.input_head_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit_text_head);
        dialogBuilder.setTitle("Введие заголовок");
        dialogBuilder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!edt.getText().toString().equals(""))
                    getActivity().setTitle(edt.getText().toString());
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void shareOption() {

    }

    private void propsOption() {

    }

    private void importToFile() {

        File root = new File(Environment.getExternalStorageDirectory() + File.separator +
                            Environment.DIRECTORY_DCIM + File.separator + "NoteManager", "Notes");
        if (!root.exists()) {
            root.mkdirs();
        }
        File noteFile = new File(root, mNote.getHead() + ".txt");
        for (int i = 0; noteFile.exists(); i++)
            noteFile = new File(root, mNote.getHead() + i + ".txt");

        noteFile.setReadable(true);
        noteFile.setWritable(true);
        try {
            FileOutputStream f = new FileOutputStream(noteFile);
            OutputStreamWriter osw = new OutputStreamWriter(f);
            osw.write(mNote.getContent());
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Сгенерирован файл " + noteFile.getName(), Toast.LENGTH_LONG).show();

    }

}

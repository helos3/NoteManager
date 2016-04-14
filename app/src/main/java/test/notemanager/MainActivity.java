package test.notemanager;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import test.notemanager.fragments.FragmentNote;
import test.notemanager.fragments.FragmentNotes;
import test.notemanager.models.Note;

public class MainActivity extends AppCompatActivity implements FragmentNotes.OnNoteTouchListener, FragmentNote.OnFragmentInteractionListener {

    Fragment currentFragment;

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }

    @Override
    public void onNoteDeleted() {
        if (currentFragment instanceof FragmentNote) {
            currentFragment = FragmentNotes.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof FragmentNote) {
            currentFragment = FragmentNotes.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commit();
            setTitle(R.string.app_name);

        }

    }

    @Override
    public void onItemSelected(Note note, boolean newNote) {
        currentFragment = FragmentNote.newInstance(note, newNote);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, currentFragment)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, "currentFragment", currentFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFragment = getFragmentManager().getFragment(savedInstanceState, "currentFragment");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        String[] mScope = {VKScope.WALL};
//        VKSdk.login(this, mScope);
//        if (savedInstanceState == null) currentFragment = FragmentNotes.newInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        currentFragment = savedInstanceState != null ?
                getFragmentManager().getFragment(savedInstanceState, "currentFragment") : FragmentNotes.newInstance();


        getFragmentManager().beginTransaction().replace(R.id.fragment_container, currentFragment).commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_create_note) {
            onItemSelected(new Note("Новая заметка", ""), true);
        }

        return super.onOptionsItemSelected(item);
    }
}

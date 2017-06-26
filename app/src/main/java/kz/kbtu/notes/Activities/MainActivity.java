package kz.kbtu.notes.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kz.kbtu.notes.Adapters.NotesAdapter;
import kz.kbtu.notes.ArrayHolder;
import kz.kbtu.notes.Database;
import kz.kbtu.notes.Dialogs.NoteLongClickDialog;
import kz.kbtu.notes.Interfaces.RecyclerItemClickListener;
import kz.kbtu.notes.Note;
import kz.kbtu.notes.R;
import kz.kbtu.notes.Status;
import kz.kbtu.notes.User;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener, NoteLongClickDialog.NoteDialogListener {

    private static final int REQUEST_ADD = 880;
    private static final int REQUEST_UPDATE = 202;
    private NotesAdapter adapter;
    private RecyclerView recycler;
    private User sessionUser;
    private Database db;
    private Note last;
    private ArrayHolder arrayHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notes");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        arrayHolder = ArrayHolder.getInstance();
        adapter = new NotesAdapter(arrayHolder.notes, this);
        recycler = (RecyclerView) findViewById(R.id.recycler_notes);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        sessionUser = intent.getParcelableExtra("user");
        db = new Database(this);
        initArrays(sessionUser);


    }

    private void initArrays(User user) {
        AsyncTask<User, Void, Void> task = new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User user = params[0];
                arrayHolder.notes.clear();
                arrayHolder.notes.addAll(db.getAllNotes(user.getId()));
                arrayHolder.statuses.clear();
                arrayHolder.statuses.addAll(db.getAllStatuses(user));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(user);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.add_action:
                intent = new Intent(this, EditActivity.class);
                intent.putExtra("requestCode", REQUEST_ADD);
                startActivityForResult(intent, REQUEST_ADD);
                break;
            case R.id.logout_action:
                finish();
                break;
            case R.id.edit_status_action:
                intent = new Intent(this, StatusActivity.class);
                intent.putExtra("user", sessionUser);
                startActivity(intent);
                break;
        }
        return true;
    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return df.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD) {
            if (resultCode == Activity.RESULT_OK) {
                String text = data.getStringExtra("text");
                Status status = data.getParcelableExtra("status");
                String author = sessionUser.getId();
                String date = getDateTime();
                addNote(new Note(null, text, author, sessionUser.getUsername(), status.getId(), status.getName(), date));

            }
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == Activity.RESULT_OK) {
                String text = data.getStringExtra("text");
                Status status = data.getParcelableExtra("status");
                updateNote(new Note(last.getId(), text, last.getAuthor(), last.getAuthor_name() , status.getId(), status.getName(), getDateTime()));
            }
        }
    }

    private void updateNote(Note note) {
        AsyncTask<Note, Void, Void> task = new AsyncTask<Note, Void, Void>() {
            @Override
            protected Void doInBackground(Note... params) {
                Note note = params[0];
                db.updateNote(note);
                arrayHolder.notes.clear();
                arrayHolder.notes.addAll(db.getAllNotes(sessionUser.getId()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(note);
    }

    private void addNote(Note note) {
        AsyncTask<Note, Void, Boolean> task = new AsyncTask<Note, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Note... params) {
                Note note = params[0];
                boolean check = db.addNote(note);
                arrayHolder.notes.clear();
                arrayHolder.notes.addAll(db.getAllNotes(sessionUser.getId()));
                return check;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(note);
    }


    @Override
    public void btnDeleteClicked(int adapterPosition) {

    }

    @Override
    public void itemClicked(int adapterPosition) {
        last = adapter.getItem(adapterPosition);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("text", last.getText());
        intent.putExtra("status", last.getStatus_id());
        intent.putExtra("requestCode", REQUEST_UPDATE);
        startActivityForResult(intent, REQUEST_UPDATE);
    }

    @Override
    public void itemLongClicked(int adapterPosition) {
        last = adapter.getItem(adapterPosition);
        DialogFragment dialog = new NoteLongClickDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("position", adapterPosition);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Action");
    }

    @Override
    public void actionClicked(int which, int adapterPosition) {
        switch (which) {
            case 0:
                actionDelete(adapterPosition);
                break;
            case 1:
                actionShare(adapterPosition);
                break;
        }
    }

    private void actionShare(int adapterPosition) {
        Note note = adapter.getItem(adapterPosition);
        String message = note.toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void actionDelete(int adapterPosition) {
        Note note = adapter.getItem(adapterPosition);
        AsyncTask<Note, Void, Void> task = new AsyncTask<Note, Void, Void>() {
            @Override
            protected Void doInBackground(Note... params) {
                Note note = params[0];
                db.deleteNote(note);
                arrayHolder.notes.clear();
                arrayHolder.notes.addAll(db.getAllNotes(sessionUser.getId()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(note);
    }


}

package kz.kbtu.notes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kz.kbtu.notes.Adapters.NotesAdapter;
import kz.kbtu.notes.Database;
import kz.kbtu.notes.Interfaces.RecyclerItemClickListener;
import kz.kbtu.notes.Note;
import kz.kbtu.notes.R;
import kz.kbtu.notes.Status;
import kz.kbtu.notes.User;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private static final int REQUEST_CODE = 880;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private RecyclerView recycler;
    private User sessionUser;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notes");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        notes = new ArrayList<>();
        adapter = new NotesAdapter(notes, this);
        recycler = (RecyclerView)findViewById(R.id.recycler_notes);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        sessionUser = intent.getParcelableExtra("user");
        db = new Database(this);
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
        switch (item.getItemId()){
            case R.id.add_action:
//                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, EditActivity.class);
                intent.putExtra("user", sessionUser);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.logout_action:
//                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
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

    private String getDateTime(){
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Log.d("DEBUG", df.format(calendar.getTime()));
        return df.format(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
//                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                String text = data.getStringExtra("text");
                Status status = data.getParcelableExtra("status");
                String author = sessionUser.getId();
                String date = getDateTime();
                addNote(new Note(null, text, author, status.getId(), date));

            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addNote(Note note){
        AsyncTask <Note, Void, Boolean> task = new AsyncTask<Note, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Note... params) {
                Note note = params[0];
                boolean check  = db.addNote(note);
                notes.clear();
                notes.addAll(db.getAllNotes(sessionUser.getId()));
                return check;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
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

    }

}

package kz.kbtu.notes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import kz.kbtu.notes.Adapters.NotesAdapter;
import kz.kbtu.notes.Interfaces.RecyclerItemClickListener;
import kz.kbtu.notes.Note;
import kz.kbtu.notes.R;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener {

    private static final int REQUEST_CODE = 880;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notes");
        setSupportActionBar(toolbar);

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
        switch (item.getItemId()){
            case R.id.add_action:
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EditActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.logout_action:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.edit_status_action:

                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void itemClicked() {

    }
}

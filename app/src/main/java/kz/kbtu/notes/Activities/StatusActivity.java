package kz.kbtu.notes.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import kz.kbtu.notes.Adapters.StatusAdapter;
import kz.kbtu.notes.Database;
import kz.kbtu.notes.Dialogs.AddStatusDialog;
import kz.kbtu.notes.Dialogs.EditStatusDialog;
import kz.kbtu.notes.Interfaces.RecyclerItemClickListener;
import kz.kbtu.notes.R;
import kz.kbtu.notes.Status;
import kz.kbtu.notes.User;

/*

!NEED TO HANDLE NULL POINTER EXCEPTION FOR STATUS LIST

 */

public class StatusActivity extends AppCompatActivity implements
        RecyclerItemClickListener, AddStatusDialog.AddStatusDialogListener, EditStatusDialog.EditStatusDialogListener{

    private ArrayList<Status> statuses;
    private RecyclerView recycler;
    private StatusAdapter adapter;
    private Database db;
    private User sessionUser;
    private Status last;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Intent intent = getIntent();
        sessionUser = intent.getParcelableExtra("user");
        adapter = new StatusAdapter(statuses, this);
        db = new Database(this);
        statuses = new ArrayList<>();
        recycler = (RecyclerView)findViewById(R.id.recycler_status);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getStatuses();
    }

    private void getStatuses(){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                statuses.clear();
                statuses = db.getAllStatuses(sessionUser);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter = new StatusAdapter(statuses, StatusActivity.this);
                recycler.setAdapter(adapter);
            }
        };
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.status_add_action:
                showAddDialog();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    private void showAddDialog(){
        DialogFragment dialog = new AddStatusDialog();
        dialog.show(getFragmentManager(), "Add");
    }

    @Override
    public void btnDeleteClicked(int adapterPosition) {
        Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        AsyncTask<Status, Void, Void> task = new AsyncTask<Status, Void, Void>() {

            @Override
            protected Void doInBackground(kz.kbtu.notes.Status... params) {
                kz.kbtu.notes.Status status = params[0];
                db.deleteStatus(status);
                statuses.clear();
                statuses.addAll(db.getAllStatuses(sessionUser));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(adapter.getItem(adapterPosition));
    }

    @Override
    public void itemClicked(int adapterPosition) {
        DialogFragment dialog = new EditStatusDialog();
        Status status = adapter.getItem(adapterPosition);
        last = status;
        Bundle bundle = new Bundle();
        bundle.putString("text", status.getName());
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "Edit");
    }

    @Override
    public void onPositiveClick(DialogFragment dialog, String text) {
        Toast.makeText(this, "Positive Clicked " + dialog + text, Toast.LENGTH_SHORT).show();
        Log.d("DEBUG", dialog.getTag());
        if(dialog.getTag().equals("Add")){
            if(!text.equals("")){
                AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        String text = params[0];
                        return db.addStatus(new kz.kbtu.notes.Status(null, text, sessionUser.getId()), sessionUser);
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if(aBoolean){
                            Toast.makeText(StatusActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(StatusActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                        }
                        getStatuses();
                    }
                };
                task.execute(text);
            }
        }
        else if(dialog.getTag().equals("Edit")){
            if(!text.equals("")){
                AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        String text = params[0];
                        db.updateStatus(new kz.kbtu.notes.Status(last.getId(), text, last.getAuthor()));
                        statuses.clear();
                        statuses.addAll(db.getAllStatuses(sessionUser));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        adapter.notifyDataSetChanged();
                    }
                };
                task.execute(text);
            }
        }

    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {

    }



}

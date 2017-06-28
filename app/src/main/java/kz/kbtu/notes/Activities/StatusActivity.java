package kz.kbtu.notes.Activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
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

import kz.kbtu.notes.Adapters.StatusAdapter;
import kz.kbtu.notes.ArrayHolder;
import kz.kbtu.notes.Database;
import kz.kbtu.notes.Dialogs.AddStatusDialog;
import kz.kbtu.notes.Dialogs.EditStatusDialog;
import kz.kbtu.notes.Interfaces.RecyclerItemClickListener;
import kz.kbtu.notes.R;
import kz.kbtu.notes.Status;
import kz.kbtu.notes.User;

public class StatusActivity extends AppCompatActivity implements
        RecyclerItemClickListener, AddStatusDialog.AddStatusDialogListener, EditStatusDialog.EditStatusDialogListener{

    private RecyclerView recycler;
    private StatusAdapter adapter;
    private Database db;
    private User sessionUser;
    private Status last;
    private ArrayHolder arrayHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        arrayHolder = ArrayHolder.getInstance();
        db = new Database(this);
        Intent intent = getIntent();
        sessionUser = intent.getParcelableExtra("user");
        adapter = new StatusAdapter(arrayHolder.statuses, this);
        recycler = (RecyclerView)findViewById(R.id.recycler_status);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        addCommonStatus();
    }

    private void addCommonStatus(){
        if(arrayHolder.statuses.isEmpty()){
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    db.addStatus(new kz.kbtu.notes.Status(null, "Common", sessionUser.getId()), sessionUser);
                    arrayHolder.statuses.addAll(db.getAllStatuses(sessionUser));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    adapter.notifyDataSetChanged();
                }
            };
            task.execute();
        }
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
        AsyncTask<Status, Void, Void> task = new AsyncTask<Status, Void, Void>() {

            @Override
            protected Void doInBackground(kz.kbtu.notes.Status... params) {
                kz.kbtu.notes.Status status = params[0];
                db.deleteStatus(status);
                arrayHolder.statuses.clear();
                arrayHolder.statuses.addAll(db.getAllStatuses(sessionUser));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                addCommonStatus();
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
    public void itemLongClicked(int adapterPosition) {

    }

    @Override
    public void onPositiveClick(DialogFragment dialog, String text) {
        if(dialog.getTag().equals("Add")){
            if(!text.equals("")){
                AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
                    boolean check = false;
                    boolean cause = false;
                    @Override
                    protected Boolean doInBackground(String... params) {
                        String text = params[0];
                        try{
                            check = db.addStatus(new kz.kbtu.notes.Status(null, text, sessionUser.getId()), sessionUser);
                        }
                        catch (SQLiteConstraintException e){
                            cause = e.getMessage().contains("2067");
                        }
                        arrayHolder.statuses.clear();
                        arrayHolder.statuses.addAll(db.getAllStatuses(sessionUser));
                        return check;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if(aBoolean){
                            Toast.makeText(StatusActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(cause){
                                Toast.makeText(StatusActivity.this, "Status exists", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(StatusActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
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
                        arrayHolder.statuses.clear();
                        arrayHolder.statuses.addAll(db.getAllStatuses(sessionUser));
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

package kz.kbtu.notes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import kz.kbtu.notes.Database;
import kz.kbtu.notes.R;
import kz.kbtu.notes.Status;
import kz.kbtu.notes.User;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText etEditNote;
    private Spinner spinner;
    private ArrayList<String> labels;
    private ArrayList<Status> statusList;
    private User sessionUser;
    private Database db;
    private ArrayAdapter<String> statusArrayAdapter;
    private Status selectedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        db = new Database(this);
        etEditNote = (EditText)findViewById(R.id.etEditNote);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        labels = new ArrayList<>();
        statusList = new ArrayList<>();
        Intent intent = getIntent();
        sessionUser = intent.getParcelableExtra("user");
        statusArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
        statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(statusArrayAdapter);
        getStatusList(sessionUser);
    }


    private void getStatusList(User user){
        AsyncTask<User, Void, Void> task = new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User user = params[0];
                statusList.clear();
                statusList.addAll(db.getAllStatuses(user));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                labels.clear();
                for(int i = 0; i < statusList.size(); i++){
                    labels.add(statusList.get(i).getName());
                }
                statusArrayAdapter.notifyDataSetChanged();
            }
        };
        task.execute(user);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_action:
//                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("text", etEditNote.getText().toString());
                intent.putExtra("status", selectedStatus);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.cancel_action:
//                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedStatus = statusList.get(position);
//        Toast.makeText(this, selectedStatus.getName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedStatus = statusList.get(0);
    }
}

package kz.kbtu.notes.Activities;

import android.app.Activity;
import android.content.Intent;
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

import kz.kbtu.notes.ArrayHolder;
import kz.kbtu.notes.R;
import kz.kbtu.notes.Status;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText etEditNote;
    private Spinner spinner;
    private ArrayAdapter<String> statusArrayAdapter;
    private Status selectedStatus;
    private ArrayHolder arrayHolder;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        arrayHolder = ArrayHolder.getInstance();
        etEditNote = (EditText)findViewById(R.id.etEditNote);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        initLabels();
        Intent intent = getIntent();
        requestCode = intent.getIntExtra("requestCode", 0);
        statusArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayHolder.labels);
        statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(statusArrayAdapter);

        if(requestCode == 202){
            etEditNote.setText(intent.getStringExtra("text"));
            etEditNote.setSelection(intent.getStringExtra("text").length());
            spinner.setSelection(getSelection(intent.getStringExtra("status")));
        }
    }


    private int getSelection(String id){
        int selection = 0;
        for(int i = 0; i < arrayHolder.statuses.size(); i++){
            String statId = arrayHolder.statuses.get(i).getId();
            if(statId.equals(id)){
                selection = i;
            }
        }
        return selection;
    }


    private void initLabels(){
        arrayHolder.labels.clear();
        for(int i = 0; i < arrayHolder.statuses.size(); i++){
            arrayHolder.labels.add(arrayHolder.statuses.get(i).getName());
        }
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
                Intent intent = new Intent();
                intent.putExtra("text", etEditNote.getText().toString());
                intent.putExtra("status", selectedStatus);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.cancel_action:
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedStatus = arrayHolder.statuses.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedStatus = arrayHolder.statuses.get(0);
    }
}

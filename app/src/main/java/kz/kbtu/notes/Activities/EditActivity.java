package kz.kbtu.notes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import kz.kbtu.notes.R;

public class EditActivity extends AppCompatActivity {

    EditText etEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        etEditNote = (EditText)findViewById(R.id.etEditNote);
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
                Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, new Intent().putExtra("text", etEditNote.getText().toString()));
                finish();
                break;
            case R.id.cancel_action:
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
                break;
        }

        return true;
    }
}

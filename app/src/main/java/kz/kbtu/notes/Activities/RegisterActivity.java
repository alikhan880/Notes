package kz.kbtu.notes.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kz.kbtu.notes.Database;
import kz.kbtu.notes.R;
import kz.kbtu.notes.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etLogin_Reg;
    private EditText etPassword_Reg;
    private Button btnSignUp;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bind();
        Intent intent = getIntent();
        String username = intent.getStringExtra("login");
        String password = intent.getStringExtra("password");
        etLogin_Reg.setText(username);
        etPassword_Reg.setText(password);
        db = new Database(this);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private void bind(){
        etLogin_Reg = (EditText)findViewById(R.id.etLogin_Reg);
        etPassword_Reg = (EditText)findViewById(R.id.etPassword_Reg);
        btnSignUp = (Button)findViewById(R.id.register);

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                String username = etLogin_Reg.getText().toString();
                String password = etPassword_Reg.getText().toString();
                if(username.equals("") || password.equals("")){
                    Toast.makeText(this, "Please, fill in all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    register(new User(null, username, password));
                }
                break;
        }
    }

    private void register(User user){
        AsyncTask<String, Void, Integer> task = new AsyncTask<String, Void, Integer>() {

            @Override
            protected Integer doInBackground(String... params) {
                String username = params[0];
                String password = params[1];
                if(db.checkUserExist(username)){
                    return 200;
                }
                else return db.createUser(username, password) ? 1 : 0;

            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if(integer == 200){
                    Toast.makeText(RegisterActivity.this, "User exist", Toast.LENGTH_SHORT).show();
                }
                else if(integer == 0){
                    Toast.makeText(RegisterActivity.this, "Failed to create", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        task.execute(user.getUsername(), user.getPassword());
    }
}

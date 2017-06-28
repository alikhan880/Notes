package kz.kbtu.notes.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kz.kbtu.notes.Database;
import kz.kbtu.notes.R;
import kz.kbtu.notes.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "DEBUG";
    private EditText etLogin;
    private EditText etPassword;
    private Button btnSignIn;
    private Button btnSignUp;
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind();
        db = new Database(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    private void bind(){
        etLogin = (EditText)findViewById(R.id.etLogin);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnSignIn = (Button)findViewById(R.id.sign_in);
        btnSignUp = (Button)findViewById(R.id.sign_up);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in:
                signIn(wrapData());
                break;
            case R.id.sign_up:
                signUp(wrapData());
                break;
        }
    }

    private void signIn(final User user){
        AsyncTask<String, Void, Integer> task = new AsyncTask<String, Void, Integer>() {

            String username;
            String password;
            User user;
            @Override
            protected Integer doInBackground(String... params) {
                username = params[0];
                password = params[1];
                if(db.checkUserExist(username)){
                    user = db.getUser(username);
                    if(checkPassword(user.getPassword(), password)){
                        return 0;
                    }
                    else{
                        return 1;
                    }
                }
                else{
                    return 404;
                }
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                if(integer == 404){
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
                else if(integer == 1){
                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        };
        task.execute(user.getUsername(), user.getPassword());
    }

    private void signUp(User user){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("login", user.getUsername());
        intent.putExtra("password", user.getPassword());
        startActivity(intent);
    }

    private User wrapData(){
        String username = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        return new User(null, username, password);
    }

    private Boolean checkPassword(String str1, String str2){
        return str1.equals(str2);
    }
}

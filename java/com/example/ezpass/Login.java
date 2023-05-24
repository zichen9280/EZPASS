package com.example.ezpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private Button login;
    private EditText Password;
    private TextView forgetPass, hint;
    private ProgressBar progressBar;
    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mySQLiteAdapter = new SQLiteAdapter(this);
        Password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        login = (Button) findViewById(R.id.login);
        forgetPass = (TextView) findViewById(R.id.ForgetPass);
        hint = (TextView) findViewById(R.id.Hint);

        mySQLiteAdapter.openToRead_1();
        Boolean checker = mySQLiteAdapter.checkEmpty();
        mySQLiteAdapter.close();

        if (checker == true) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(Login.this, "Welcome to EZPASS. Please register to continue.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Register.class));
            progressBar.setVisibility(View.GONE);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySQLiteAdapter.openToRead_1();
                String passHint = mySQLiteAdapter.forgetPass();
                mySQLiteAdapter.close();
                forgetPass.setVisibility(View.GONE);
                hint.setText("Hint :" + passHint + "\n\nIf still unable to remember Login Password\nClear the App Data in phone settings.\n \n*All the stored data will be deleted too.*");
                hint.setVisibility(View.VISIBLE);
            }
        });

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hint.setVisibility(View.GONE);
                forgetPass.setVisibility(View.VISIBLE);
            }
        });
    }

    private void login() {

        String password = Password.getText().toString().trim();

        if (password.isEmpty()) {
            Password.setError("Password is required.");
            Password.requestFocus();
            return;
        }

        mySQLiteAdapter.openToRead_1();
        Boolean checkPass = mySQLiteAdapter.checkPassword(password);
        mySQLiteAdapter.close();
        if (checkPass == true) {
            Toast.makeText(Login.this, "Successfully Signed in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            Toast.makeText(Login.this, "Incorrect Password! Please try again.", Toast.LENGTH_SHORT).show();
            Password.setError("Incorrect Password.");
            Password.requestFocus();
            return;
        }


    }
}

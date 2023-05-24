package com.example.ezpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private Button registerUser;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mySQLiteAdapter = new SQLiteAdapter(this);
        registerUser = (Button) findViewById(R.id.register);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerUser();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void registerUser() throws InterruptedException {
        String password = editTextPassword.getText().toString().trim();
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            editTextPassword.setError("Password should be at least 8 characters long.");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mySQLiteAdapter.openToWrite_1();
        mySQLiteAdapter.insertPass(password);
        mySQLiteAdapter.close();
        progressBar.setVisibility(View.GONE);
        Toast.makeText(Register.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
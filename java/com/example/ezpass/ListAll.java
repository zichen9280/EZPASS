package com.example.ezpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListAll extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;
    private TextView ShowData;
    private Button Confirm, Cancel;
    private EditText Password;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_all);

        getSupportActionBar().setTitle("List All");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.neon_green)));

        mySQLiteAdapter = new SQLiteAdapter(this);
        ShowData = findViewById(R.id.showData);
        Confirm = (Button) findViewById(R.id.confirm);
        Cancel = (Button) findViewById(R.id.cancel);
        Password = (EditText) findViewById(R.id.PassConfirm);
        ll = (LinearLayout) findViewById(R.id.ListAllConfirm);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

    }

    private void confirm() {
        String password = Password.getText().toString().trim();

        if (password.isEmpty()) {
            Password.setError("Login password cannot be empty.");
            Password.requestFocus();
            return;
        }

        mySQLiteAdapter.openToRead_1();
        Boolean checkPass = mySQLiteAdapter.checkPassword(password);
        mySQLiteAdapter.close();
        if (checkPass == true) {

            ll.setVisibility(View.GONE);
            mySQLiteAdapter.openToRead_1();
            Boolean checkList = mySQLiteAdapter.checkEmpty_2();
            mySQLiteAdapter.close();

            String readAll;
            if (checkList == false) {
                mySQLiteAdapter.openToRead_1();
                readAll = "Data List : " + mySQLiteAdapter.queueAll();
                mySQLiteAdapter.close();
            } else {
                readAll = "List is Empty. \nFeel free to add data.";
            }
            ShowData.setText(readAll);
        } else {
            Toast.makeText(ListAll.this, "Incorrect Password! Please try again.", Toast.LENGTH_SHORT).show();
            Password.setError("Incorrect Password.");
            Password.requestFocus();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    private void cancel() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
package com.example.ezpass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView ShowData;
    private EditText SiteName, Username, Password;
    private Button Add, Update, Delete, List_All;
    private ProgressBar progressBar;
    private SQLiteAdapter mySQLiteAdapter;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.neon_green)));

        mySQLiteAdapter = new SQLiteAdapter(this);

        SiteName = (EditText) findViewById(R.id.SiteNameInput);
        Username = (EditText) findViewById(R.id.UsernameInput);
        Password = (EditText) findViewById(R.id.PasswordInput);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        Add = (Button) findViewById(R.id.AddBTN);
        Update = (Button) findViewById(R.id.UpdateBTN);
        Delete = (Button) findViewById(R.id.DeleteBTN);
        List_All = (Button) findViewById(R.id.ListAllBTN);

        ShowData = findViewById(R.id.showData);

        mySQLiteAdapter.openToRead_1();
        Boolean checkList = mySQLiteAdapter.checkEmpty_2();
        mySQLiteAdapter.close();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String siteName = SiteName.getText().toString().trim();
                if (siteName.isEmpty()) {
                    SiteName.setError("Site name can't be empty.");
                    SiteName.requestFocus();
                    return;
                }
                String username = Username.getText().toString().trim();
                if (username.isEmpty()) {
                    Username.setError("Username can't be empty.");
                    Username.requestFocus();
                    return;
                }
                mySQLiteAdapter.openToRead_1();
                Boolean CheckDuplicate = mySQLiteAdapter.checkSiteUsername(siteName, username);
                mySQLiteAdapter.close();
                if (CheckDuplicate == true) {
                    SiteName.setError("Try again.");
                    Toast.makeText(MainActivity.this, "Same record was added before. Try another new record.", Toast.LENGTH_SHORT).show();
                    SiteName.requestFocus();
                    return;
                }
                String password = Password.getText().toString().trim();
                if (password.isEmpty()) {
                    Password.setError("Password can't be empty");
                    Password.requestFocus();
                    return;
                }

                if (password.length() < 4) {
                    Password.setError("Password should be at least 4 characters long.");
                    Password.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mySQLiteAdapter.openToWrite_1();
                mySQLiteAdapter.insertData(siteName, username, password);
                mySQLiteAdapter.close();
                Toast.makeText(MainActivity.this, "Data Added Successfully.", Toast.LENGTH_SHORT).show();
                mySQLiteAdapter.openToRead_1();
                String readAll = "This list only show first 3 character of each password.\nClick List All to view full password." + mySQLiteAdapter.queueAllEncyrtp();
                mySQLiteAdapter.close();
                ShowData.setText(readAll);
                Toast.makeText(MainActivity.this, "Ths list is updated.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String siteName = SiteName.getText().toString().trim();
                if (siteName.isEmpty()) {
                    SiteName.setError("Site name cant be empty.");
                    SiteName.requestFocus();
                    return;
                }
                String username = Username.getText().toString().trim();
                if (username.isEmpty()) {
                    Username.setError("Username can't be empty.");
                    Username.requestFocus();
                    return;
                }
                mySQLiteAdapter.openToRead_1();
                Boolean CheckExists = mySQLiteAdapter.checkSiteUsername(siteName, username);
                mySQLiteAdapter.close();
                if (CheckExists == false) {
                    SiteName.setError("Try again.");
                    Username.setError("Try again.");
                    Toast.makeText(MainActivity.this, "Could not find any result. Please make sure details are correct.", Toast.LENGTH_SHORT).show();
                    SiteName.requestFocus();
                    return;
                }
                String password = Password.getText().toString().trim();
                if (password.isEmpty()) {
                    Password.setError("Password can't be empty.");
                    Password.requestFocus();
                    return;
                }
                if (password.length() < 4) {
                    Password.setError("Password should be at least 4 characters long.");
                    Password.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mySQLiteAdapter.openToWrite_1();
                Boolean UpdatePass = mySQLiteAdapter.updatePass(siteName, username, password);
                mySQLiteAdapter.close();
                if (UpdatePass == true)
                    Toast.makeText(MainActivity.this, "Password Updated Successfully.", Toast.LENGTH_SHORT).show();
                    mySQLiteAdapter.openToRead_1();
                    String readAll = "This list only show first 3 character of each password.\nClick List All to view full password." + mySQLiteAdapter.queueAllEncyrtp();
                    mySQLiteAdapter.close();
                    ShowData.setText(readAll);
                    Toast.makeText(MainActivity.this, "Ths list is updated.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String siteName = SiteName.getText().toString().trim();
                if (siteName.isEmpty()) {
                    SiteName.setError("Site name cant be empty.");
                    SiteName.requestFocus();
                    return;
                }
                String username = Username.getText().toString().trim();
                if (username.isEmpty()) {
                    Username.setError("Username can't be empty.");
                    Username.requestFocus();
                    return;
                }

                String password = Password.getText().toString().trim();
                if (password.isEmpty()) {
                    Password.setError("Password can't be empty.");
                    Password.requestFocus();
                    return;
                }
                if (password.length() < 4) {
                    Password.setError("Password should be at least 4 characters long.");
                    Password.requestFocus();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);
                mySQLiteAdapter.openToWrite_1();
                Boolean deleteData = mySQLiteAdapter.deleteData(siteName, username, password);
                mySQLiteAdapter.close();
                if (deleteData == true) {
                    Toast.makeText(MainActivity.this, "Data Deleted Successfully.", Toast.LENGTH_SHORT).show();
                    mySQLiteAdapter.openToRead_1();
                    String readAll = "This list only show first 3 character of each password.\nClick List All to view full password." + mySQLiteAdapter.queueAllEncyrtp();
                    mySQLiteAdapter.close();
                    ShowData.setText(readAll);
                    Toast.makeText(MainActivity.this, "Ths list is updated.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Data Failed to Delete. Please try again.", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
            }
        });


        List_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                ListAll();
                progressBar.setVisibility(View.GONE);
            }
        });


        if (checkList == false) {
            mySQLiteAdapter.openToRead_1();
            String readAll = "This list only show first 3 character of each password.\nClick List All to view full password." + mySQLiteAdapter.queueAllEncyrtp();
            mySQLiteAdapter.close();
            ShowData.setText(readAll);
        } else {
            String readAll = "List is Empty. \nFeel free to add data.";
            ShowData.setText(readAll);
        }
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            Toast.makeText(getBaseContext(), "Double tap back to exit the app.", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


    private void ListAll() {
        startActivity(new Intent(this, ListAll.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.ChangePass) {
            startActivity(new Intent(this, ChangeLoginPassword.class));
            finish();
        }
        if (id == R.id.Logout) {
            startActivity(new Intent(this, Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
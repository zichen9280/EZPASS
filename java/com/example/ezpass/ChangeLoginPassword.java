package com.example.ezpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeLoginPassword extends AppCompatActivity {
    EditText OldPass, NewPass, ReNewPass;
    Button Confirm, Cancel;
    SQLiteAdapter mySQLiteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.neon_green)));


        setContentView(R.layout.activity_change_login_password);
        OldPass = (EditText) findViewById(R.id.OldPassConfirm);
        NewPass = (EditText) findViewById(R.id.NewPassConfirm);
        ReNewPass = (EditText) findViewById(R.id.ReNewPassConfirm);
        Confirm = (Button) findViewById(R.id.ConfirmBTN);
        Cancel = (Button) findViewById(R.id.CancelBTN);

        mySQLiteAdapter = new SQLiteAdapter(this);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = OldPass.getText().toString().trim();
                if (oldPass.isEmpty()) {
                    OldPass.setError("Login Password can't be empty.");
                    OldPass.requestFocus();
                    return;
                }

                mySQLiteAdapter.openToRead_1();
                Boolean checkOldPass = mySQLiteAdapter.checkPassword(oldPass);
                mySQLiteAdapter.close();

                if (checkOldPass == false) {
                    OldPass.setError("Incorrect Password");
                    OldPass.requestFocus();
                    return;
                }

                String newPass = NewPass.getText().toString().trim();
                if (newPass.isEmpty()) {
                    NewPass.setError("Login Password can't be empty.");
                    NewPass.requestFocus();
                    return;
                }

                if (newPass.length() < 8) {
                    NewPass.setError("New Login Password need to at least 8 character long.");
                    NewPass.requestFocus();
                    return;
                }

                String reNewPass = ReNewPass.getText().toString().trim();
                if (reNewPass.isEmpty()) {
                    ReNewPass.setError("Login Password can't be empty.");
                    ReNewPass.requestFocus();
                    return;
                }

                if (reNewPass.compareTo(newPass) != 0) {
                    ReNewPass.setError("Retype New Login Password is different with the New Login Password.");
                    ReNewPass.requestFocus();
                    return;
                }
                mySQLiteAdapter.openToWrite_1();
                mySQLiteAdapter.deleteAll();
                mySQLiteAdapter.insertPass(newPass);
                mySQLiteAdapter.close();
                Toast.makeText(ChangeLoginPassword.this, "Successfully Changed New Login Password.", Toast.LENGTH_SHORT).show();
                backToMain();
            }
        });
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

    private void backToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
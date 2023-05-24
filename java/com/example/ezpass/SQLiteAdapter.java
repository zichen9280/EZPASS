package com.example.ezpass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLiteAdapter {

    public static final String MYDATABASE_NAME_1 = "EZPASS.db";
    public static final String MYDATABASE_TABLE_1 = "Login_TABLE";
    public static final String MYDATABASE_TABLE_2 = "UserData_TABLE";
    public static final int MYDATABASE_VERSION = 1;
    public static final String KEY_CONTENT_1 = "Password";
    public static final String KEY_CONTENT_2 = "SiteName";
    public static final String KEY_CONTENT_3 = "Username";
    public static final String KEY_CONTENT_4 = "Password";


    private static final String SCRIPT_CREATE_DATABASE =
            "create table " + MYDATABASE_TABLE_1 + " (" + KEY_CONTENT_1 + " text not null);";

    private static final String SCRIPT_CREATE_DATABASE2 =
            "create table " + MYDATABASE_TABLE_2
                    + " (" + KEY_CONTENT_2 + " text not null, "
                    + KEY_CONTENT_3 + " text, "
                    + KEY_CONTENT_4 + " text);";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SQLiteAdapter(Context c) {
        context = c;
    }

    public SQLiteAdapter openToRead_1() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME_1, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite_1() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME_1, null,
                MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        sqLiteHelper.close();
    }

    public long insertPass(String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT_1, password);
        return sqLiteDatabase.insert(MYDATABASE_TABLE_1, null, contentValues);
    }

    public long insertData(String siteName, String username, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT_2, siteName);
        contentValues.put(KEY_CONTENT_3, username);
        contentValues.put(KEY_CONTENT_4, password);
        return sqLiteDatabase.insert(MYDATABASE_TABLE_2, null, contentValues);
    }

    public int deleteAll() {
        return sqLiteDatabase.delete(MYDATABASE_TABLE_1, null, null);
    }

    public String queueAll() {
        String[] columns = new String[]{KEY_CONTENT_2, KEY_CONTENT_3, KEY_CONTENT_4};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE_2, columns,
                null, null, null, null, null);
        String result = "";
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_2);
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_3);
        int index_CONTENT_3 = cursor.getColumnIndex(KEY_CONTENT_4);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            result = result + "\n\nSite Name: " + cursor.getString(index_CONTENT) + "\nUsername: "
                    + cursor.getString(index_CONTENT_2) + "\nPassword: "
                    + cursor.getString(index_CONTENT_3);
        }

        return result;
    }

    public String forgetPass() {
        String[] columns = new String[]{KEY_CONTENT_1};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE_1, columns,
                null, null, null, null, null);
        String forgetPass_encrypt_1, forgetPass_encrypt_2, forgetHint = "";
        cursor.moveToLast();
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_1);
        forgetPass_encrypt_1 = cursor.getString(index_CONTENT);
        forgetPass_encrypt_2 = forgetPass_encrypt_1.substring(0, 3);
        for (int i = 3; i < forgetPass_encrypt_1.length(); i++) {
            forgetPass_encrypt_2 = forgetPass_encrypt_2 + "*";
        }
        forgetHint = forgetPass_encrypt_2;
    return forgetHint;
    }

    public String queueAllEncyrtp() {
        String[] columns = new String[]{KEY_CONTENT_2, KEY_CONTENT_3, KEY_CONTENT_4};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE_2, columns,
                null, null, null, null, null);
        String pass_encrypt_1, pass_encrypt_2;
        String result = "";
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_2);
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_3);
        int index_CONTENT_3 = cursor.getColumnIndex(KEY_CONTENT_4);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            pass_encrypt_1 = cursor.getString(index_CONTENT_3);
            pass_encrypt_2 = pass_encrypt_1.substring(0, 3);
            for (int i = 3; i < pass_encrypt_1.length(); i++) {
                pass_encrypt_2 = pass_encrypt_2 + "*";
            }
            result = result + "\n\nSite Name: " + cursor.getString(index_CONTENT) + "\nUsername: "
                    + cursor.getString(index_CONTENT_2) + "\nPassword: "
                    + pass_encrypt_2;
        }
        return result;
    }

    public Boolean checkEmpty() {
        String[] columns = new String[]{KEY_CONTENT_1};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE_1, columns,
                null, null, null, null, null);
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_1);
        if (cursor.getCount() < 1)
            return true;
        else
            return false;
    }

    public Boolean checkEmpty_2() {
        String[] columns = new String[]{KEY_CONTENT_2};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE_2, columns,
                null, null, null, null, null);
        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT_1);
        if (cursor.getCount() < 1)
            return true;
        else
            return false;
    }

    public Boolean checkPassword(String password) throws android.database.SQLException {
        String[] columns = new String[]{KEY_CONTENT_1};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE_1, columns,
                KEY_CONTENT_1 + "=?", new String[]{password}, null, null, null);
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkSiteUsername(String sitename, String username) throws android.database.SQLException {
        String[] columns = new String[]{KEY_CONTENT_2, KEY_CONTENT_3};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE_2, columns,
                KEY_CONTENT_2 + "=? and " + KEY_CONTENT_3 + "=?", new String[]{sitename, username}, null, null, null);
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean updatePass(String siteName, String username, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT_4, password);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from UserData_TABLE where SiteName = ? and Username = ?", new String[]{siteName, username});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.update("UserData_TABLE", contentValues, "SiteName = ? and Username = ?", new String[]{siteName, username});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteData(String sitename, String username, String password) {
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from UserData_TABLE where SiteName = ? and Username = ? and Password = ?", new String[]{sitename, username, password});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.delete("UserData_Table", "SiteName = ? and Username = ? and Password = ?", new String[]{sitename, username, password});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            Toast.makeText(context, "Could not find data.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // SQLiteOpenHelper: A helper class to manage database creation and version management
    public class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
            db.execSQL(SCRIPT_CREATE_DATABASE2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Login_TABLE");
            db.execSQL("DROP TABLE IF EXISTS UserData_TABLE");
            onCreate(db);
        }
    }


}

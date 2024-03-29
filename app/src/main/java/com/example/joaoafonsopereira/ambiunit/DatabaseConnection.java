package com.example.joaoafonsopereira.ambiunit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseConnection extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ambiunit.db";
    public static final String TABLE_NAME = "registeruser";
    public static final String TABLE_2_NAME = "measurements";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";

    public DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY  KEY AUTOINCREMENT, username TEXT, password TEXT, name TEXT, email TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE measurements (ID INTEGER PRIMARY  KEY AUTOINCREMENT, ax_value DOUBLE, d4_value DOUBLE, mics_value, temperature DOUBLE, humidity DOUBLE, date TEXT, time TEXT, username TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_2_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addUser(String user, String password, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user);
        contentValues.put("password", password);
        contentValues.put("name", name);
        contentValues.put("email", email);
        long res = db.insert("registeruser", null, contentValues);
        db.close();
        return res;
    }

    public boolean checkUser(String username, String password) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?" + " and " + COL_3 + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean usernameIsValid(String username) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_2 + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count == 0)
            return true;
        else
            return false;
    }

    public String getName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {username};
        String query = "SELECT name FROM registeruser WHERE username = ?";
        Cursor c = db.rawQuery(query,args);
        String name = new String();
        while(c.moveToNext()){
            name = c.getString(0);
        }
        c.close();
        db.close();
        return name;
    }

    public String getPassword(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {username};
        String query = "SELECT password FROM registeruser WHERE username = ?";
        Cursor c = db.rawQuery(query,args);
        String pass = new String();
        while(c.moveToNext()){
            pass = c.getString(0);
        }
        c.close();
        db.close();
        return pass;
    }

    public String getEmail(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {username};
        String query = "SELECT email FROM registeruser WHERE username = ?";
        Cursor c = db.rawQuery(query,args);
        String email = new String();
        while(c.moveToNext()){
            email = c.getString(0);
        }
        c.close();
        db.close();
        return email;
    }

    public long getMeasurementsCount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(
                "SELECT COUNT (*) FROM  measurements WHERE username =?",
                new String[] { String.valueOf(username) }
        );
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();
        db.close();
        return count;
    }

    public int setPassword(String username, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", pass);
        return db.update("registeruser", values, "username='" + username + "'", null);
    }

    public int deleteAccount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete("registeruser", "username=?", new String[]{username});
    }

    public long addData(double ax_value, double d4_value, double mics_value, double temperature, double humidity, String date, String time, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ax_value", ax_value);
        contentValues.put("d4_value", d4_value);
        contentValues.put("mics_value", mics_value);
        contentValues.put("temperature", temperature);
        contentValues.put("humidity", humidity);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("username", username);
        long res = db.insert("measurements", null, contentValues);
        db.close();
        return res;
    }

    public ArrayList getMeasurementsByDate(String date, String username) {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {date, username};
        String query = "SELECT time, ax_value, d4_value, mics_value, temperature, humidity FROM measurements WHERE date=? AND username=?";
        Cursor c = db.rawQuery(query,args);
        while(c.moveToNext()){
            data.add(c.getString(0));
            data.add(c.getString(1));
            data.add(c.getString(2));
            data.add(c.getString(3));
            data.add(c.getString(4));
            data.add(c.getString(5));
        }
        c.close();
        db.close();
        return data;
    }

    public ArrayList getCOAXMeasurements(String username) {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {username};
        String query = "SELECT ax_value FROM measurements WHERE username=?";
        Cursor c = db.rawQuery(query,args);
        while(c.moveToNext()){
            data.add(c.getString(0));
        }
        c.close();
        db.close();
        return data;
    }

    public ArrayList getCOD4Measurements(String username) {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {username};
        String query = "SELECT d4_value FROM measurements WHERE username=?";
        Cursor c = db.rawQuery(query,args);
        while(c.moveToNext()){
            data.add(c.getString(0));
        }
        c.close();
        db.close();
        return data;
    }

    public ArrayList getMICSMeasurements(String username) {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {username};
        String query = "SELECT mics_value FROM measurements WHERE username=?";
        Cursor c = db.rawQuery(query,args);
        while(c.moveToNext()){
            data.add(c.getString(0));
        }
        c.close();
        db.close();
        return data;
    }

    public int clearData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete("measurements", "username=?", new String[]{username});
    }

}
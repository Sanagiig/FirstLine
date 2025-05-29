package com.example.myapplication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SQLiiteHelper extends SQLiteOpenHelper {
    Context context;
    public  static final String CREATE_BOOKS_TABLE = "CREATE TABLE Books (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT," +
            "author TEXT," +
            "price REAL," +
            "pages INTEGER)";

    public static  final String CREATE_CATEGORY_TABLE = "CREATE TABLE Category (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT," +
            "code TEXT)";

    public SQLiiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_BOOKS_TABLE);


        Toast.makeText(context, "Table created successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Books");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}

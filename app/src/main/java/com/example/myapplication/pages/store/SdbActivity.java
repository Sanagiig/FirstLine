package com.example.myapplication.pages.store;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.dto.Books;
import com.example.myapplication.utils.SQLiiteHelper;

import org.litepal.LitePal;

public class SdbActivity extends AppCompatActivity {
    private static final String TAG = "SdbActivity";
    private SQLiiteHelper sqlLiteHelper;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sdb);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    public void init() {
        sqlLiteHelper = new SQLiiteHelper(this, "BookStore.db", null, 1);
        dbWriter = sqlLiteHelper.getWritableDatabase();
        findViewById(R.id.gen_sqlite_btn).setOnClickListener(v -> {
        });

        findViewById(R.id.add_book_btn).setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put("name", getRanName());
            values.put("author", getRanName());
            values.put("price", (double) (Math.random() * 100));
            values.put("pages", (int) (Math.random() * 1000));
            dbWriter.insert("books", null, values);
        });

        findViewById(R.id.add_book_orm_btn).setOnClickListener(v -> {
            var b = new Books("orm", "me", (double) (Math.random() * 100), (int) (Math.random() * 1000));
            b.save();
        });

        findViewById(R.id.display_books_btn).setOnClickListener(v -> {
            var res = dbWriter.rawQuery("SELECT * FROM books", null);
            if (res == null || !res.moveToFirst()) {
                Log.d(TAG, "query : no data");
                return;
            }

            var nameIndex = res.getColumnIndex("name");
            var authorIndex = res.getColumnIndex("author");
            var priceIndex = res.getColumnIndex("price");
            var pagesIndex = res.getColumnIndex("pages");

            Log.d(TAG, "Booksidx: " + nameIndex + " " + authorIndex + " " + priceIndex + " " + pagesIndex);
            do {
                @SuppressLint("Range") var name = res.getString(nameIndex);
                @SuppressLint("Range") var author = res.getString(authorIndex);
                @SuppressLint("Range") var price = res.getDouble(priceIndex);
                @SuppressLint("Range") var pages = res.getInt(pagesIndex);
                Log.d(TAG, "lineData: " + name + " " + author + " " + price + " " + pages);
            } while (res.moveToNext());
            res.close();
        });

        LitePal.initialize(this);
    }

    public String getRanName() {
        var str = "abcdefghijklmnopqrstuvwxyz";
        var name = "";
        for (int i = 0; i < 10; i++) {
            name += str.charAt((int) (Math.random() * 26));
        }
        return name;
    }
}
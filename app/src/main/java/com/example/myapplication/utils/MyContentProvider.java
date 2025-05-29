package com.example.myapplication.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    public static final String TAG = "MyContentProvider";
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    public static final String AUTHORITY = "com.example.myapplication.provider";
    public static final String SEND_URI = "content://com.example.myapplication.provider";
    public static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI("com.example.myapplication.provider", "book", BOOK_DIR);
        uriMatcher.addURI("com.example.myapplication.provider", "book/#", BOOK_ITEM);
        uriMatcher.addURI("com.example.myapplication.provider", "category", CATEGORY_DIR);
        uriMatcher.addURI("com.example.myapplication.provider", "category/#", CATEGORY_ITEM);
    }

    private SQLiiteHelper db;

    @Override
    public boolean onCreate() {
        db = new SQLiiteHelper(getContext(), "book.db", null, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return db.getReadableDatabase().query("Books", projection, selection,
                        selectionArgs, null, null, sortOrder);
            case BOOK_ITEM:
                var bookId = uri.getPathSegments().get(1);
                return db.getReadableDatabase().query("Books", projection, "id=?",
                        new String[]{
                                bookId
                        }, null, null, sortOrder);
            case CATEGORY_DIR:
                return db.getReadableDatabase().query("Category", projection, selection,
                        selectionArgs, null, null, sortOrder);
            case CATEGORY_ITEM:
                return db.getReadableDatabase().query("Category", projection, "id=?",
                        new String[]{
                                uri.getPathSegments().get(1)
                        }, null, null, sortOrder);
            default:
                Log.d(TAG, "query: uri error \n" + uri);
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.myapplication.provider.book_dir";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.myapplication.provider.book_item";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.myapplication.provider.category_dir";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.myapplication.provider.category_item";
        }
        return "";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                var id = db.getWritableDatabase().insert("Books", null, values);
                Log.d(TAG, "insert: " + id);
                return Uri.parse("content://" + AUTHORITY + "/book/" + id);
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                id = db.getWritableDatabase().insert("Category", null, values);
                Log.d(TAG, "insert: " + id);
                return Uri.parse("content://" + AUTHORITY + "/category/" + id);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                deleteRows = db.getWritableDatabase().delete("Books", selection, selectionArgs);
                Log.d(TAG, "delete: " + deleteRows);
                return deleteRows;
            case BOOK_ITEM:
                var bookId = uri.getPathSegments().get(1);
                deleteRows = db.getWritableDatabase().delete("Books", "id=?", new String[]{bookId});
                Log.d(TAG, "delete: " + deleteRows);
                return deleteRows;
            case CATEGORY_DIR:
                deleteRows = db.getWritableDatabase().delete("Category", selection, selectionArgs);
                Log.d(TAG, "delete: " + deleteRows);
                return deleteRows;
            case CATEGORY_ITEM:
                var categoryId = uri.getPathSegments().get(1);
                deleteRows = db.getWritableDatabase().delete("Category", "id=?", new String[]{categoryId});
                Log.d(TAG, "delete: " + deleteRows);
                return deleteRows;
        }
        return deleteRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                return db.getWritableDatabase().update("Books", values, selection, selectionArgs);
            case BOOK_ITEM:
                var bookId = uri.getPathSegments().get(1);
                return db.getWritableDatabase().update("Books", values, "id=?", new String[]{bookId});
            case CATEGORY_DIR:
                return db.getWritableDatabase().update("Category", values, selection, selectionArgs);
            case CATEGORY_ITEM:
                var categoryId = uri.getPathSegments().get(1);
                return db.getWritableDatabase().update("Category", values, "id=?", new String[]{categoryId});
        }
        return 0;
    }
}

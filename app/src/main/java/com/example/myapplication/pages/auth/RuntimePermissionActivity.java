package com.example.myapplication.pages.auth;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.MyContentProvider;

import java.util.Arrays;

public class RuntimePermissionActivity extends AppCompatActivity {
    public static final String TAG = "RuntimePermissionActivity";
    private ListView contactListView;
    private ArrayAdapter<String> addapter;
    private EditText bookIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_runtime_permission);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    private void init() {
        contactListView = findViewById(R.id.contact_list);
        addapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        contactListView.setAdapter(addapter);

        bookIdEditText = findViewById(R.id.book_name_et);
        findViewById(R.id.call_phone_btn).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            } else {
                call();
            }
        });

        findViewById(R.id.read_contact_btn).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 2);
            } else {
                readContact();
            }
        });

        findViewById(R.id.add_book_by_provider_btn).setOnClickListener(v -> {
            var cv = new ContentValues();
            cv.put("name", "book name");
            cv.put("author", "book author");
            var res = getContentResolver().insert(Uri.parse(MyContentProvider.SEND_URI + "/book"),
                    cv);
            Log.d(TAG, "add book by provider " + res);
        });

        findViewById(R.id.get_book_btn).setOnClickListener(v -> {
            var id = bookIdEditText.getText().toString();
            if (id.isEmpty()) {
                Toast.makeText(this, "please input book id", Toast.LENGTH_SHORT).show();
                return;
            }

            var cursor = getContentResolver().query(Uri.parse(MyContentProvider.SEND_URI +
                            "/book/" + id),
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") var name = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") var author = cursor.getString(cursor.getColumnIndex("author"));
                    @SuppressLint("Range") var price = cursor.getDouble(cursor.getColumnIndex("price"));
                    @SuppressLint("Range") var pages = cursor.getInt(cursor.getColumnIndex("pages"));
                    bookIdEditText.setText("name: " + name + " " + "author: " + author + " " + "price: " + price + " " + "pages: " + pages);
                }
            }
        });
    }

    private void call() {
        try {
            var intent = new Intent(Intent.ACTION_CALL);
            intent.setData(android.net.Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (Exception e) {
            Log.d(TAG, "call phone error ");
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    private void readContact() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            Log.d(TAG, "phone uri is : " + ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") var name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    @SuppressLint("Range") var phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    addapter.add(name + ": " + phone);
                }
                Log.d(TAG, "readContact: " + addapter.getCount() + addapter.getItem(0));
                addapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.d(TAG, "read contact error ");
            Log.d(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Arrays.asList(permissions).contains(android.Manifest.permission.CALL_PHONE)) {
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0 && grantResults[0] == 0) {
                        call();
                    } else {
                        Log.d(TAG, "call phone permission denied");
                        Toast.makeText(this, "call phone permission denied.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } else if (Arrays.asList(permissions).contains(android.Manifest.permission.READ_CONTACTS)) {
            switch (requestCode) {
                case 2:
                    if (grantResults.length > 0 && grantResults[0] == 0) {
                        readContact();
                    } else {
                        Log.d(TAG, "read contact permission denied");
                        Toast.makeText(this, "read contact permission denied.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
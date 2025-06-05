package com.example.myapplication.pages.provider;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.LogUtil;

public class ProviderTestActivity extends AppCompatActivity {
    public static final String TAG = "ProviderTestActivity";
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        scrollView = findViewById(R.id.scroll_view);
        findViewById(R.id.query_album_btn).setOnClickListener(v -> {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            var resolver = getContentResolver();

            try (var cursor = resolver.query(uri, null, null, null, null)) {
                if (cursor != null) {
                    var container = (LinearLayout) findViewById(R.id.container);
                    while (cursor.moveToNext()) {
                        @SuppressLint("Range") var name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        @SuppressLint("Range") var id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                        @SuppressLint("Range") var size = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                        @SuppressLint("Range") var date =
                                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                        var imageUri = Uri.withAppendedPath(uri, String.valueOf(id));

                        ImageView iv = new ImageView(this);
                        iv.setImageURI(imageUri);
                        iv.setMaxWidth(container.getWidth());

                        container.addView(iv);
                        LogUtil.d(TAG, "name: " + name + ", id: " + id + ", size: " + size + ", " +
                                "date: " + date + ", imageUri: " + imageUri);
                    }
                }
            }
        });
    }
}
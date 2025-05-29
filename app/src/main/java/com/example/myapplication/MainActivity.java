package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.pages.auth.RuntimePermissionActivity;
import com.example.myapplication.pages.backend.BackendEntryActivity;
import com.example.myapplication.pages.mutiMedia.NotificationActivity;
import com.example.myapplication.pages.store.PreferrencesActivity;
import com.example.myapplication.pages.store.SdbActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init(){
        findViewById(R.id.go_preferences_btn).setOnClickListener(v -> {
            var intent = new Intent(this, PreferrencesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.go_sqlite_btn).setOnClickListener(v -> {
            var intent = new Intent(this, SdbActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.go_auth_btn).setOnClickListener(v -> {
            var intent = new Intent(this, RuntimePermissionActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.go_muti_media_btn).setOnClickListener(v -> {
            var intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.go_backend_btn).setOnClickListener(v -> {
            var intent = new Intent(this, BackendEntryActivity.class);
            startActivity(intent);
        });
    }
}
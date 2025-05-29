package com.example.myapplication.pages.backend;

import android.Manifest;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.ForeServiceConnection;
import com.example.myapplication.utils.MyServiceConnection;
import com.example.myapplication.utils.PermissionRequestCode;

public class BackendEntryActivity extends AppCompatActivity {
    public static final String TAG = "BackendEntryActivity";
    private ServiceConnection serviceConnection;
    private ForegroundService  foregroundService;
    private ForeServiceConnection  foregroundServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_backend_entry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionRequestCode.PERMISSION_FOREGROUND_SERVICE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFore();
                }
                break;
        }
    }

    private void init() {
        if (serviceConnection == null) {
            serviceConnection = new MyServiceConnection();
        }
        findViewById(R.id.open_ui_test_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, UiTestActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_service_btn).setOnClickListener(v -> {
            startService(new Intent(this, MyService.class));
        });

        findViewById(R.id.stop_service_btn).setOnClickListener(v -> {
            stopService(new Intent(this, MyService.class));
        });

        findViewById(R.id.bind_service_btn).setOnClickListener(v -> {
            bindService(new Intent(this, MyService.class), serviceConnection, BIND_AUTO_CREATE);
        });

        findViewById(R.id.unbind_service_btn).setOnClickListener(v -> {
            unbindService(serviceConnection);
        });

        findViewById(R.id.open_fore_notification_btn).setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE,
                                Manifest.permission.FOREGROUND_SERVICE},
                        PermissionRequestCode.PERMISSION_FOREGROUND_SERVICE);
            } else {
                startFore();
            }
        });

        findViewById(R.id.stop_foreground_service_btn).setOnClickListener(v -> {

        });
    }

    private void startFore() {
        Intent intent = new Intent(this, ForegroundService.class);
        startForegroundService(intent);
        intent.putExtra("data",10);
        foregroundServiceConnection = new ForeServiceConnection();
        bindService(intent, foregroundServiceConnection, BIND_AUTO_CREATE);
    }
}
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.dto.SendMsg;
import com.example.myapplication.dto.SendMsg2;
import com.example.myapplication.helper.MyApplication;
import com.example.myapplication.pages.auth.RuntimePermissionActivity;
import com.example.myapplication.pages.backend.BackendEntryActivity;
import com.example.myapplication.pages.mutiMedia.NotificationActivity;
import com.example.myapplication.pages.provider.ProviderTestActivity;
import com.example.myapplication.pages.store.PreferrencesActivity;
import com.example.myapplication.pages.store.SdbActivity;
import com.example.myapplication.pages.tool.DrawerTestActivity;
import com.example.myapplication.receiver.CountReceiver;

import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    Toolbar toolbar;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_launcher_foreground);
        toolbar.setTitle("My Application");
        toolbar.setNavigationIcon(R.drawable.ic_launcher_foreground);
//        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(item -> {
            String msg = "";
            final int id = item.getItemId();
            if (id == R.id.action_edit) {
                msg += "点击编辑";
            } else if (id == R.id.action_settings) {
                msg += "点击设置";
            } else if (id == R.id.action_new) {
                msg += "点击新建";
            } else if (id == R.id.action_share) {
                msg += "点击分享";
            }

            if (!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        toolbar.setNavigationOnClickListener(v -> {
            Log.d(TAG, "init: nav click");
        });
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

        findViewById(R.id.go_tool_btn).setOnClickListener(v -> {
            var intent = new Intent(this, DrawerTestActivity.class);
            var data = new SendMsg("title", "content");
            intent.putExtra("data", data);

            var data2 = new SendMsg2("title2", "content2");
            intent.putExtra("data2", data2);
            startActivity(intent);
        });

        findViewById(R.id.context_test_btn).setOnClickListener(v -> {
            Toast.makeText(MyApplication.getContext(), "context test", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.send_static_broadcast).setOnClickListener(v -> {
            var intent = new Intent(this, CountReceiver.class);
            intent.putExtra("data", LocalDateTime.now().getSecond());
            sendBroadcast(intent);
        });

        var intentFilter = new IntentFilter("com.example.myapplication.BROADCAST_1");
        var receiver = new CountReceiver();
        registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED);
        findViewById(R.id.send_dynamic_broadcast).setOnClickListener(v -> {
            var intent = new Intent();
            intent.setAction("com.example.myapplication.BROADCAST_1");
            intent.putExtra("data", LocalDateTime.now().getSecond());

            sendBroadcast(intent);
        });

        findViewById(R.id.open_provider_test_btn).setOnClickListener(v -> {
            var intent = new Intent(this, ProviderTestActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_timer).setOnClickListener(v -> startTimer());
    }

    @SuppressLint({"ScheduleExactAlarm", "ShortAlarm"})
    private void startTimer() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, CountReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerAtMillis = SystemClock.elapsedRealtime() + 1000;
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis,1000,
                pendingIntent);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    triggerAtMillis, pendingIntent);
//        } else {
//        }
    }
}
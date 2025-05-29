package com.example.myapplication.pages.backend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UiTestActivity extends AppCompatActivity {
    TextView tv;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            tv.setText(msg + "  " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy" +
                    "-MM-dd HH:mm:ss")));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ui_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        tv = findViewById(R.id.text_view);
        findViewById(R.id.change_ui_btn).setOnClickListener(v -> {
            Thread t = new Thread(() -> {
                Message m = new Message();
                m.what = 1;
                handler.sendMessage(new Message());
            });
            t.start();
        });
    }
}
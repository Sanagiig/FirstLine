package com.example.myapplication.pages.tool;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.example.myapplication.dto.SendMsg;
import com.example.myapplication.dto.SendMsg2;

public class DrawerTestActivity extends AppCompatActivity {
    private static final String TAG = "DrawerTestActivity";
    Toolbar toolbar;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drawer_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.open();
        }
        return true;
    }

    private void init() {
        var intent = getIntent();
        if (intent != null) {
            SendMsg data = null;
            SendMsg2 data2 = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data = intent.getSerializableExtra("data", SendMsg.class);
                data2 = intent.getParcelableExtra("data2", SendMsg2.class);
            } else {
                data = (SendMsg) intent.getSerializableExtra("data");
                data2 = (SendMsg2) intent.getParcelableExtra("data2");
            }

            if (data2 != null) {
//                Toast.makeText(this, data.getTitle() + ": " + data.getContent(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, data2.getTitle() + ": " + data2.getContent(),
                        Toast.LENGTH_SHORT).show();
            }
        }
//        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
//
//        toolbar.setTitle("My Application");
//        toolbar.setNavigationIcon(R.drawable.ic_launcher_foreground);
//        toolbar.inflateMenu(R.menu.menu_main);
        Log.d(TAG, "init: ");
//        toolbar.setOnMenuItemClickListener(item -> {
//            Log.d(TAG, "init: click");
//            drawerLayout.open();
//            return true;
//        });

        findViewById(R.id.open_drawer_btn).setOnClickListener(v -> {
            drawerLayout.open();
        });
    }
}
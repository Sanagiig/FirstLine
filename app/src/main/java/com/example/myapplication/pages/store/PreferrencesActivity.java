package com.example.myapplication.pages.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class PreferrencesActivity extends AppCompatActivity {
    Button saveButton;
    EditText saveEditText;

    SharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preferrences);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
    }

    private void init() {
        saveButton = findViewById(R.id.save_btn);
        saveEditText = findViewById(R.id.save_edit_text);
        mySharedPreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);

        var savedTxt = mySharedPreferences.getString("save_edit_text","");

        saveEditText.setText(savedTxt);

        saveButton.setOnClickListener(v -> {
            var txt = saveEditText.getText().toString();
            mySharedPreferences.edit().putString("save_edit_text", txt).apply();
        });
    }
}
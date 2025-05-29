package com.example.myapplication.pages.mutiMedia;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WebViewActivity extends AppCompatActivity {
    public static final String TAG = "WebViewActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        webView = findViewById(R.id.media_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.bing.com");

        findViewById(R.id.http_get_btn).setOnClickListener(v -> {
            URL url = null;
            try {
                HttpClient hc = new HttpClient();
                hc.get("/ihome-h5/basePackages/login/index/index", this::displayResponse);

            } catch (MalformedURLException e) {
                Log.d(TAG, "url error: " + Log.getStackTraceString(e));

            } catch (IOException e) {
                Log.d(TAG, "io error: " + Log.getStackTraceString(e));
            } catch (Exception e) {
                Log.d(TAG, "exception: " + Log.getStackTraceString(e));
            }
        });
    }

    private void displayResponse(InputStream in) {
        if(in == null){
            Log.d(TAG, "displayResponse: input stream is null");
            return;
        }

        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader buffer = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
            Log.d(TAG, "displayResponse: " + sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                Log.d(TAG, "displayResponse: close error " + Log.getStackTraceString(e));
            }
        }
    }
}
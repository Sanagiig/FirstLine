package com.example.myapplication.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private static final String TAG = "HttpClient";
    private String baseUrl = "http://10.100.2.104:6001";

    private HttpURLConnection getConn(String path, String method) throws IOException {
        URL url = new URL(baseUrl + path);
        Log.d(TAG, "getConn: " + "url is : " + baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        conn.setRequestMethod(method);
        return conn;
    }

    public void get(String url, IHandler handler) throws Exception {
        Thread t = new Thread(() -> {
            try {
                HttpURLConnection conn = getConn(url, "GET");
                conn.connect();
                int code = conn.getResponseCode();
                Log.d(TAG, "get: " + "code is : " + code);
                if (code == 200) {
                    // 获取响应数据
                    InputStream inputStream = conn.getInputStream();
                    handler.handle(inputStream);
                } else {
                    handler.handle(null);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
    }
}


package com.example.myapplication.utils;

import android.app.Application;

import org.litepal.LitePal;

public class LitepalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}

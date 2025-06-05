package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.utils.LogUtil;

public class CountReceiver extends BroadcastReceiver {
    public static final String TAG = "CountReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        var data = intent.getIntExtra("data", 0);
        LogUtil.d(TAG, "data: " + data);
    }
}

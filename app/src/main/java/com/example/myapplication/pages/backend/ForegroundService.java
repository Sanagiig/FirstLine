package com.example.myapplication.pages.backend;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class ForegroundService extends Service {
    public static final String TAG = "ForegroundService";
    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private NotificationCompat.Builder notificationBuilder;
    private int currentProgress = 0;
    private Thread progressThread;
    private MyBinder myBinder;
    private int data;
    private Map<Object,IProgressChanger> progressCallbackMap = new HashMap<>();

    public class MyBinder extends Binder {
        public ForegroundService getService() {
            return ForegroundService.this;
        }
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        myBinder = new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setProgress(100, 0, false)
                .setContentTitle("Foreground Service")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_launcher_background);


        ServiceCompat.startForeground(this, NOTIFICATION_ID, notificationBuilder.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);

        if (progressThread == null) {
            progressThread = new Thread(() -> {
                while (true) {
                    try {
                        if (Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        Thread.sleep(100);
                        currentProgress += data;
                        if (currentProgress > 100) {
                            currentProgress = 0;
                        }
                        notificationBuilder.setProgress(100, currentProgress, false);
                        startForeground(NOTIFICATION_ID, notificationBuilder.build());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            progressThread.start();
        }

        currentProgress = 0;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        data = intent.getIntExtra("data", 1);
        Log.d(TAG, "onBind: get data " + data);
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public void stopProgress() {
        if (progressThread != null) {
            progressThread.interrupt();
            progressThread = null;
        }
    }
}


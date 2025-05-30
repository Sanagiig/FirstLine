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
import com.example.myapplication.helper.CustomThread;
import com.example.myapplication.utils.download.DownloadTask;
import com.example.myapplication.utils.download.IDownloadListener;

import java.util.HashMap;
import java.util.Map;

public class ForegroundService extends Service {
    public static final String TAG = "ForegroundService";
    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private NotificationCompat.Builder notificationBuilder;
    private int currentProgress = 0;
    private CustomThread progressThread;
    private MyBinder myBinder;
    private int data;
    private Map<Object, IProgressChanger> progressCallbackMap = new HashMap<>();
    private DownloadTask downloadTask;

    private IDownloadListener downloadListener = new IDownloadListener() {
        @Override
        public void onProgressChanged(int progress) {
            Log.d(TAG, "onProgressChanged: " + progress);
            Log.d(TAG, "onProgressChanged: xx " +  progressCallbackMap.values());
            notificationBuilder.setProgress(100, progress, false);
            startForeground(NOTIFICATION_ID, notificationBuilder.build());
            for (IProgressChanger progressChanger : progressCallbackMap.values()) {
                progressChanger.onProgressChanged(progress);
            }
        }

        @Override
        public void onDownloadFinished() {
            Log.d(TAG, "onDownloadFinished: download finished");
        }

        @Override
        public void onDownloadFailed() {
            Log.d(TAG, "onDownloadFailed: download failed");
        }

        @Override
        public void onDownloadStarted() {
            Log.d(TAG, "onDownloadStarted: ");
        }

        @Override
        public void onDownloadPaused() {
            Log.d(TAG, "onDownloadPaused: ");
        }

        @Override
        public void onDownloadResumed() {
            Log.d(TAG, "onDownloadResumed: ");
        }

        @Override
        public void onDownloadCancelled() {
            Log.d(TAG, "onDownloadCancelled: ");
        }
    };

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

//        if (progressThread == null) {
//            progressThread = new CustomThread(this::updateProgress);
//            progressThread.start();
//        }else{
//            progressThread.resumeThread();
//        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        data = intent.getIntExtra("data", 1);
        Log.d(TAG, "onBind: get data " + data);
        downloadTask = new DownloadTask(downloadListener);
        downloadTask.execute("https://oss.polyic.cn/aj-web-app/static/server/common/home_banner2" +
                ".png");
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

    public void addProgressListener(Object key, IProgressChanger progressChanger) {
        progressCallbackMap.put(key, progressChanger);
    }

    public void removeProgressListener(Object key) {
        progressCallbackMap.remove(key);
    }

    public void clearProgressListener() {
        progressCallbackMap.clear();
    }

    public void stopProgress() {
        progressThread.pauseThread();
    }

    private void updateProgress() {
        try {
            Log.d(TAG, "updateProgress: ");
            Thread.sleep(100);
            currentProgress += data;
            if (currentProgress > 100) {
                currentProgress = 0;
            }
            notificationBuilder.setProgress(100, currentProgress, false);
            startForeground(NOTIFICATION_ID, notificationBuilder.build());
            for (IProgressChanger progressChanger : progressCallbackMap.values()) {
                progressChanger.onProgressChanged(currentProgress);
            }
        } catch (InterruptedException e) {
            Log.d(TAG, "updateProgress: " + Log.getStackTraceString(e));
        }
    }

}


package com.example.myapplication.helper;

import android.util.Log;

public class CustomThread extends Thread {
    public static final String TAG = "CustomThread";
    private boolean isPause = false;
    private Runnable work;

    public CustomThread(Runnable work) {
        this.work = work;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: " + work);
        if (work == null) {
            Log.d(TAG, "run: no work");
            return;
        }

        while (true) {
            while (isPause) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Log.d(TAG, "run: " + Log.getStackTraceString(e));
                    }
                }
            }
            work.run();
        }
    }

    public void pauseThread() {
        synchronized (this) {
            isPause = true;
        }
    }

    public void resumeThread() {
        synchronized (this) {
            isPause = false;
            notifyAll();
        }
    }
}

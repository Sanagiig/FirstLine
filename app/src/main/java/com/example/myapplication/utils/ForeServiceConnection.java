package com.example.myapplication.utils;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.myapplication.pages.backend.ForegroundService;
import com.example.myapplication.pages.backend.IProgressChanger;

public class ForeServiceConnection implements ServiceConnection {
    public static final String TAG = "ForeServiceConnection";
    private ForegroundService mService;
    private IProgressChanger  mProgressChanger;

    public ForeServiceConnection(IProgressChanger progressChanger) {
        mProgressChanger = progressChanger;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((ForegroundService.MyBinder) service).getService();
        addProgressListener(mProgressChanger);
        Log.d(TAG, "onServiceConnected: ");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
        Log.d(TAG, "onServiceDisconnected: ");
    }

    @Override
    public void onBindingDied(ComponentName name) {
        ServiceConnection.super.onBindingDied(name);
        Log.d(TAG, "onBindingDied: ");
    }

    @Override
    public void onNullBinding(ComponentName name) {
        ServiceConnection.super.onNullBinding(name);
        Log.d(TAG, "onNullBinding: ");
    }

    public ForegroundService getService(){
        return mService;
    }

    public void addProgressListener(IProgressChanger progressChanger){
        mProgressChanger = progressChanger;
        if(mService != null){
            mService.addProgressListener(this, mProgressChanger);
        }
    }
}

package com.example.myapplication.utils.download;

public interface IDownloadListener {
    void onProgressChanged(int progress);

    void onDownloadFinished();

    void onDownloadFailed();

    void onDownloadStarted();

    void onDownloadPaused();

    void onDownloadResumed();

    void onDownloadCancelled();
}

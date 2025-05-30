package com.example.myapplication.utils.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, DownloadStatus> {
    private static final String TAG = "DownloadTask";
    private boolean isCancel = false;
    private boolean isPause = false;

    private IDownloadListener listener;
    private int lastProgress = 0;

    public DownloadTask(IDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected DownloadStatus doInBackground(String... params) {
        InputStream in = null;
        RandomAccessFile saveFile = null;
        File file = null;

        try {
            long downloadLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            Log.d(TAG, "doInBackground: file path: " + directory + fileName);
            if (file.exists()) {
                downloadLength = file.length();
            }

            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return DownloadStatus.DOWNLOAD_FAILED;
            } else if (contentLength == downloadLength) {
                return DownloadStatus.DOWNLOAD_FINISHED;
            }

            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + downloadLength + "-")
                    .url(downloadUrl)
                    .build();

            Response res = client.newCall(req).execute();
            if (res != null) {
                in = res.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downloadLength);

                byte[] buffer = new byte[1024];
                int total = 0;
                int len;
                int progress = 0;
                while ((len = in.read(buffer)) != -1) {
                    if (isPause) {
                        return DownloadStatus.DOWNLOAD_PAUSED;
                    } else if (isCancel) {
                        return DownloadStatus.DOWNLOAD_CANCELLED;
                    } else {
                        total += len;
                        saveFile.write(buffer, 0, len);
                        progress = (int) ((total + downloadLength) * 100 / contentLength);
                        Log.d(TAG, "doInBackground: pulish progress " + progress);
                        publishProgress(progress);
                    }
                }
                return DownloadStatus.DOWNLOAD_FINISHED;
            }

        } catch (Exception e) {
            Log.d(TAG, "doInBackground: " + Log.getStackTraceString(e));
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (saveFile != null) {
                    saveFile.close();
                }

                if (isCancel && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                Log.d(TAG, "doInBackground: " + Log.getStackTraceString(e));
            }
        }
        return DownloadStatus.DOWNLOAD_FAILED;
    }

    @Override
    public void onProgressUpdate(Integer... values) {
        int progress = values[0];
        Log.d(TAG, "onProgressUpdate: progress " +progress);
        if (progress > lastProgress) {
            listener.onProgressChanged(progress);
        }
    }

    @Override
    public void onPostExecute(DownloadStatus status) {
        switch (status) {
            case DOWNLOAD_FINISHED:
                listener.onDownloadFinished();
                break;
            case DOWNLOAD_FAILED:
                listener.onDownloadFailed();
                break;
            case DOWNLOAD_CANCELLED:
                listener.onDownloadCancelled();
                break;
            case DOWNLOAD_PAUSED:
                listener.onDownloadPaused();
                break;
        }
    }

    public void pauseDownload() {
        isPause = true;
    }

    public void cancelDownload() {
        isCancel = true;
    }

    public void resumeDownload() {
        isPause = false;
    }

    public long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder()
                .url(downloadUrl)
                .build();

        Response res = client.newCall(req).execute();
        if (res != null && res.isSuccessful()) {
            long contentLength = res.body().contentLength();
            res.close();
            return contentLength;
        }
        return 0;
    }
}

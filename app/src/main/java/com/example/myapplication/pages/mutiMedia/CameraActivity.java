package com.example.myapplication.pages.mutiMedia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.PermissionRequestCode;
import com.example.myapplication.utils.ToastHelper;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    public static final String TAG = "CameraActivity";
    Button takePhotoBtn, openGalleryBtn;
    ImageView photoIV;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionRequestCode.PERMISSION_CAMERA) {
            if (resultCode == RESULT_OK) {
                photoIV.setImageURI(imgUri);
            }
        } else if (requestCode == PermissionRequestCode.PERMISSION_OPEN_GALLERY) {
            if (resultCode == RESULT_OK) {
                photoIV.setImageURI(data.getData());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestCode.PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                ToastHelper.showToast(this, "无摄像权限");
            }
        } else if (requestCode == PermissionRequestCode.PERMISSION_OPEN_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                ToastHelper.showToast(this, "无读权限");
            }
        }
    }

    private void init() {
        takePhotoBtn = findViewById(R.id.take_photo_btn);
        openGalleryBtn = findViewById(R.id.open_gallery_btn);
        photoIV = findViewById(R.id.photo_iv);
        photoIV.setImageResource(R.drawable.ic_launcher_background);

        Log.d(TAG, "init: " + getExternalFilesDir(null));
        Log.d(TAG, "init: " + getExternalCacheDir());
        takePhotoBtn.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        PermissionRequestCode.PERMISSION_CAMERA);
            } else {
                takePhoto();
            }
        });

        openGalleryBtn.setOnClickListener(v -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        PermissionRequestCode.PERMISSION_READ_STORAGE);
//                return;
//            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        PermissionRequestCode.PERMISSION_READ_STORAGE);
//            } else {
//                openGallery();
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PermissionRequestCode.PERMISSION_OPEN_GALLERY);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 可以不申请权限，直接通过 MediaStore 访问
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionRequestCode.PERMISSION_OPEN_GALLERY);
            }
        });
    }

    private void takePhoto() {
        File outputFile = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputFile.exists()) {
                outputFile.delete();
            }
            if (!outputFile.createNewFile()) {
                Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.d(TAG, "init: create img file err.");
            Log.d(TAG, "init: " + Log.getStackTraceString(e));
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imgUri = FileProvider.getUriForFile(this, "com.example.myapplication.fileprovider",
                    outputFile);
        } else {
            imgUri = Uri.fromFile(outputFile);
        }
        Log.d(TAG, "init: provider content uri: " + FileProvider.getUriForFile(this, "com" +
                        ".example.myapplication.fileprovider",
                outputFile));
        Log.d(TAG, "init: file uri " + Uri.fromFile(outputFile));

        Intent Intentintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PermissionRequestCode.PERMISSION_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PermissionRequestCode.PERMISSION_OPEN_GALLERY);
    }
}
package com.bytes.mangalho.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

public class PermissionUtils {

    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_MANAGE_EXTERNAL_STORAGE = 2;
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 3;

    public static boolean checkAndRequestStoragePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Permission is automatically granted on devices running Android versions lower than Marshmallow
            return true;
        }

        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED
                || readExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and higher, check and request MANAGE_EXTERNAL_STORAGE permission if needed
            int manageExternalStoragePermission = ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.MANAGE_EXTERNAL_STORAGE);

            if (manageExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                        REQUEST_MANAGE_EXTERNAL_STORAGE);
                return false;
            }
        }

        // Permissions are already granted
        return true;
    }

    public static boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE
                || requestCode == REQUEST_READ_EXTERNAL_STORAGE
                || requestCode == REQUEST_MANAGE_EXTERNAL_STORAGE) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    // Permission denied
                    return false;
                }
            }
            // All permissions granted
            return true;
        }
        return true; // Handle other permissions if needed
    }

    public static boolean isScopedStorageMode() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static boolean isDocumentUriSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


}

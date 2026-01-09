package com.bytes.mangalho.activity.myprofile;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {

    public static String saveImageToGallery(Context context, Bitmap bitmap) {
        String savedImagePath = null;

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "IMG_" + timeStamp + ".png";

        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/YourAppDirectoryName");

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }

        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.close();

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, imageFileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by YourApp");
                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.DATA, savedImagePath);

                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savedImagePath;
    }
}


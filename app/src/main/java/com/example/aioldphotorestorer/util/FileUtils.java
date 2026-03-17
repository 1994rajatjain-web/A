package com.example.aioldphotorestorer.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FileUtils {

    private FileUtils() {
    }

    public static File createTempImageFile(@NonNull Context context) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File storageDir = new File(context.getCacheDir(), "images");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return File.createTempFile("PHOTO_" + timestamp + "_", ".jpg", storageDir);
    }

    public static File copyUriToFile(Context context, Uri uri) throws IOException {
        ContentResolver resolver = context.getContentResolver();
        File file = createTempImageFile(context);

        try (InputStream inputStream = resolver.openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            if (inputStream == null) {
                throw new IOException("Unable to read selected image.");
            }
            byte[] buffer = new byte[8 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
        }
        return file;
    }

    public static File getDownloadTarget(Context context) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        return new File(dir, "restored_" + timestamp + ".jpg");
    }
}

package com.ternaryop.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Pair;

/**
 * Created by dave on 25/04/14.
 * Helper class to work with Uri
 */
public class UriUtils {
    public static final String SCHEMA_FILE = "file";

    public static Pair<String, String> getRealPathAndMimeFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();

            return Pair.create(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

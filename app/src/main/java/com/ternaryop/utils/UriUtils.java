package com.ternaryop.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Pair;

/**
 * Created by dave on 25/04/14.
 * Helper class to work with Uri
 */
public class UriUtils {
    public static final String SCHEMA_FILE = "file";

    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    public static String getPath(final Context context, final Uri uri)
    {

        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String[] projection = {
                MediaStore.Images.Media.DATA
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Pair<String, String> getRealPathAndMimeFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.MIME_TYPE };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return null;
            }
            cursor.moveToFirst();

            return Pair.create(getPath(context, contentUri),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * URI doesn't encode invalid characters, so we do it
     * For example the url http://my.com/hello\u00a0-world.jpg is fixed into
     * http://my.com/hello%C2%A0-world.jpg
     * @param uriStr the url to convert to URI
     * @param enc the encoding to use
     * @param retryCount how many time must retry before throw exception
     * @return the fixed url
     * @throws URISyntaxException unable to encode the illegal characters
     * @throws UnsupportedEncodingException unable to encode the url
     */
    public static URI encodeIllegalChar(final String uriStr, String enc, int retryCount) throws URISyntaxException, UnsupportedEncodingException {
        String _uriStr = uriStr;
        while (true) {
            try {
                return new URI(_uriStr);
            } catch(URISyntaxException e) {
                String reason = e.getReason();
                if (reason == null ||
                        !(
                                reason.contains("in path") ||
                                        reason.contains("in query") ||
                                        reason.contains("in fragment")
                        )
                        ) {
                    throw e;
                }
                if (0 > retryCount--) {
                    throw e;
                }
                String input = e.getInput();
                int idx = e.getIndex();
                String illChar = String.valueOf(input.charAt(idx));
                _uriStr = input.replace(illChar, URLEncoder.encode(illChar, enc));
            }
        }
    }
}

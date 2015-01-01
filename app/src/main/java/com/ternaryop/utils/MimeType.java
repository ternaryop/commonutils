package com.ternaryop.utils;

import java.util.HashMap;

import android.webkit.MimeTypeMap;

/**
 * Created by dave on 27/04/14.
 * Helper class to work with Mime types
 */
public class MimeType {
    public static MimeType instance;
    private final HashMap<String, String> map;

    private MimeType() {
        map = new HashMap<>();
        map.put("flv", "video/x-flv");
        map.put("mp4", "video/mp4");
        map.put("m3u8", "application/x-mpegURL");
        map.put("ts", "video/MP2T");
        map.put("3gp", "video/3gpp");
        map.put("mov", "video/quicktime");
        map.put("avi", "video/x-msvideo");
        map.put("wmv", "video/x-ms-wmv");
    }

    public static MimeType getInstance() {
        if (instance == null) {
            instance = new MimeType();
        }
        return instance;
    }

    public String getTypeFromMimeType(String mimeType) {
        String[] comps = mimeType.split("/");
        if (comps.length == 2) {
            return comps[0];
        }
        return mimeType;
    }

    public static boolean isVideoMimeType(String mimeType) {
        return mimeType != null && mimeType.startsWith("video/");
    }

    public static boolean isImageMimeType(String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }

    public String getMimeTypeFromExtension(String extension) {
        String mimeType = map.get(extension);

        if (mimeType == null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }
}

package com.ternaryop.utils.dialog.app;

import android.content.ComponentName;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * Created by dave on 31/12/14.
 * Helper class to get info abput picked app from AppPickerDialog
 */
public class AppPickerUtils {
    private static final String DEFAULT_VIEWER_PREFIX = "default_viewer_";

    public static void setDefaultViewer(Context context, String mimeType, String activityPackage, String activityName) {
        if (TextUtils.isEmpty(mimeType)) {
            throw new IllegalArgumentException("Default viewer type can't be empty");
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(DEFAULT_VIEWER_PREFIX + mimeType, activityPackage + "\t" + activityName)
                .apply();
    }

    public static void resetDefaultViewer(Context context, String mimeType) {
        if (!TextUtils.isEmpty(mimeType)) {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .remove(DEFAULT_VIEWER_PREFIX + mimeType).apply();
        }
    }

    public static boolean hasDefaultViewer(Context context, String mimeType) {
        if (!TextUtils.isEmpty(mimeType)) {
            return PreferenceManager.getDefaultSharedPreferences(context).contains(DEFAULT_VIEWER_PREFIX + mimeType);
        }
        return false;
    }

    public static ComponentName getDefaultViewerComponentName(Context context, String fullPath, String mimeType) {
        String viewer = PreferenceManager.getDefaultSharedPreferences(context).getString(DEFAULT_VIEWER_PREFIX + fullPath, null);
        if (viewer == null) {
            viewer = PreferenceManager.getDefaultSharedPreferences(context).getString(DEFAULT_VIEWER_PREFIX + mimeType, null);
        }

        if (viewer != null) {
            String[] comps = viewer.split("\t");
            if (comps.length == 2) {
                return new ComponentName(comps[0], comps[1]);
            }
            throw new IllegalArgumentException("Unable to decode " + TextUtils.join(" ", comps));
        }
        return null;
    }

}

package com.ternaryop.utils.dialog.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
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

        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(DEFAULT_VIEWER_PREFIX + mimeType, activityPackage + "\t" + activityName);
        edit.apply();
    }

    public static ComponentName getDefaultViewerComponentName(Context context, String mimeType) {
        String viewer = PreferenceManager.getDefaultSharedPreferences(context).getString(DEFAULT_VIEWER_PREFIX + mimeType, null);

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

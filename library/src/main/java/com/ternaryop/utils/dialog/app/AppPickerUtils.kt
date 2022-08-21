package com.ternaryop.utils.dialog.app

import android.content.ComponentName
import android.content.Context
import androidx.preference.PreferenceManager

/**
 * Created by dave on 31/12/14.
 * Helper class to get info about picked app from AppPickerDialog
 */
object AppPickerUtils {
    private const val DEFAULT_VIEWER_PREFIX = "default_viewer_"

    fun setDefaultViewer(context: Context, mimeType: String, activityPackage: String, activityName: String) {
        require(mimeType.isNotBlank()) { "Default viewer type can't be empty" }

        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(DEFAULT_VIEWER_PREFIX + mimeType, activityPackage + "\t" + activityName)
            .apply()
    }

    fun resetDefaultViewer(context: Context, mimeType: String) {
        if (mimeType.isNotBlank()) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(DEFAULT_VIEWER_PREFIX + mimeType).apply()
        }
    }

    fun hasDefaultViewer(context: Context, mimeType: String): Boolean {
        return mimeType.isNotBlank()
            && PreferenceManager.getDefaultSharedPreferences(context).contains(DEFAULT_VIEWER_PREFIX + mimeType)
    }

    fun getDefaultViewerComponentName(context: Context, fullPath: String, mimeType: String): ComponentName? {
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val viewer = defaultSharedPreferences.getString(DEFAULT_VIEWER_PREFIX + fullPath, null)
        ?: defaultSharedPreferences.getString(DEFAULT_VIEWER_PREFIX + mimeType, null)
        ?: return null

        val comps = viewer.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (comps.size == 2) {
            return ComponentName(comps[0], comps[1])
        }
        throw IllegalArgumentException("Unable to decode " + comps.joinToString(" "))
    }
}

package com.ternaryop.utils.intent

import android.app.Activity
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import com.ternaryop.utils.dialog.app.AppPickerDialog
import com.ternaryop.utils.dialog.app.AppPickerUtils

/**
 * Created by dave on 11/05/14.
 * Helper class to create intents
 */
object MimeType {
    fun getAppIntentFromMimeType(activity: Activity, uri: Uri, componentName: ComponentName, mimeType: String): Intent? {
        val intent = createViewIntent(activity, uri, componentName, mimeType)
        // check intent existence
        val list = activity.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list != null && list.size > 0) {
            return intent
        }
        return null
    }

    @Suppress("UNUSED_PARAMETER")
    fun createViewIntent(activity: Activity, uriFile: Uri, componentName: ComponentName, mimeType: String): Intent {
        //        return ShareCompat.IntentBuilder.from(activity)
        //                .setStream(uriFile) // uri from FileProvider
        //                .setType(mimeType)
        //                .getIntent()
        //                .setAction(Intent.ACTION_VIEW) //Change if needed
        //                .setDataAndType(uriFile, mimeType)
        //                .addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT
        //                        | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
        //                        | Intent.FLAG_RECEIVER_FOREGROUND
        //                        | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uriFile, mimeType)
        intent.flags = (Intent.FLAG_ACTIVITY_FORWARD_RESULT
            or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
            or Intent.FLAG_RECEIVER_FOREGROUND
            or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.component = componentName
        return intent
    }

    fun startViewActivity(activity: Activity,
        uri: Uri,
        mimeType: String,
        useDefaultViewer: Boolean) {
        val path = uri.path ?: ""
        if (useDefaultViewer) {
            val componentName = AppPickerUtils.getDefaultViewerComponentName(activity, path, mimeType)
            if (componentName != null) {
                val intent = getAppIntentFromMimeType(activity, uri, componentName, mimeType)
                // use default saved viewer
                if (intent != null) {
                    activity.startActivity(intent)
                    return
                }
            }
        }
        val appPickerDialog = AppPickerDialog(activity, path, mimeType)
        appPickerDialog.setOpenAppClickListener(DialogInterface.OnClickListener { _, _ ->
            val activityInfo = appPickerDialog.selectedApp!!.activityInfo
            val componentName1 = ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
            activity.startActivity(createViewIntent(activity, uri, componentName1, mimeType))
        })
        appPickerDialog.show()
    }

    fun getActivitiesFromMimeType(manager: PackageManager, mimeType: String): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_VIEW, null)
        intent.type = mimeType
        val resolveInfos = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        resolveInfos.sortWith(Comparator { lhs, rhs -> lhs.loadLabel(manager).toString().compareTo(rhs.loadLabel(manager).toString()) })
        return resolveInfos
    }
}

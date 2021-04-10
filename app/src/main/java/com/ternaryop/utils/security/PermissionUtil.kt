package com.ternaryop.utils.security

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by dave on 03/07/16.
 * Code useful under Android M to check permissions
 */
object PermissionUtil {

    fun askPermission(activity: Activity, permission: String, requestCode: Int, alert: AlertDialog.Builder) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> ActivityCompat.requestPermissions(activity,
                            arrayOf(permission),
                            requestCode)
                }
            }

            alert
                .setPositiveButton(android.R.string.ok, dialogClickListener)
                .setNegativeButton(android.R.string.cancel, dialogClickListener)
                .show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }
}

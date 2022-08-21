package com.ternaryop.utils.dialog

import android.app.AlertDialog
import android.content.Context
import android.text.TextUtils
import java.util.ArrayList

fun Throwable.showErrorDialog(context: Context, title: String) {
    DialogUtils.showSimpleMessageDialog(context, title, TextUtils.join("\n\n", getExceptionMessageChain()))
}

fun Throwable.showErrorDialog(context: Context) = showErrorDialog(context, "Error")
fun Throwable.showErrorDialog(context: Context, resId: Int) = showErrorDialog(context, context.getString(resId))

fun Throwable.getExceptionMessageChain(): List<String> {
    val list = ArrayList<String>()
    var c: Throwable? = this
    while (c != null) {
        c.localizedMessage?.also { list.add(it) }
        c = c.cause
    }
    return list
}

object DialogUtils {
    fun showSimpleMessageDialog(context: Context, resId: Int, message: String) {
        showSimpleMessageDialog(context, context.getString(resId), message)
    }

    fun showSimpleMessageDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setCancelable(false) // This blocks the 'BACK' button
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

}

package com.ternaryop.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

public class DialogUtils {

    public static void showErrorDialog(Context context, Throwable t) {
        showErrorDialog(context, "Error", t);
    }

    public static void showErrorDialog(Context context, String title, Throwable t) {
        showSimpleMessageDialog(context, title, TextUtils.join("\n\n", getExceptionMessageChain(t)));
    }

    public static void showErrorDialog(Context context, int resId, Throwable t) {
        showErrorDialog(context, context.getString(resId), t);
    }

    public static void showSimpleMessageDialog(Context context, int resId, String message) {
        showSimpleMessageDialog(context, context.getString(resId), message);
    }

    public static void showSimpleMessageDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
        .setCancelable(false) // This blocks the 'BACK' button
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();                    
            }
        })
        .show();
    }

    private static List<String> getExceptionMessageChain(Throwable t) {
        List<String> list = new ArrayList<>();
        for (Throwable c = t; c != null; c = c.getCause()) {
            list.add(c.getLocalizedMessage());
        }
        return list;
    }
}

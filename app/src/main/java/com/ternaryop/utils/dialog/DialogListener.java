package com.ternaryop.utils.dialog;

import android.app.Dialog;

/**
 * Created by dave on 04/05/14.
 * Attempt to standardize common dialog methods
 */
public interface DialogListener {
    public void onCancel(Dialog dialog);
    public void onOk(Dialog dialog);
}

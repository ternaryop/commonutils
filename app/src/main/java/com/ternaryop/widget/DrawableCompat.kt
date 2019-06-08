@file:Suppress("DEPRECATION")

package com.ternaryop.widget

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build

fun Drawable.srcAtop(color: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    } else {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)

    }
}


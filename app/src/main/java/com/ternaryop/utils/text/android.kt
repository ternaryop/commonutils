@file:Suppress("DEPRECATION")
package com.ternaryop.utils.text

import android.text.Html
import android.text.Spanned

fun String.fromHtml(): CharSequence = Html.fromHtml(this)

fun Spanned.toHtml(): String = Html.toHtml(this)

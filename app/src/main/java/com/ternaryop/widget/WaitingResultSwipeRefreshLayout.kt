package com.ternaryop.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.util.TypedValue
import com.ternaryop.utils.R
import com.ternaryop.utils.R.attr.checkedColorFilter
import com.ternaryop.utils.R.attr.clickedColor

/**
 * Created by dave on 13/09/14.
 * Add a flag to check if waiting result is in progress, the isRefreshing flag can't be used because it is set to true
 * before the onRefresh() is called so testing its value inside the onRefresh() is wrong
 */
open class WaitingResultSwipeRefreshLayout : SwipeRefreshLayout {
    var isWaitingResult = false

    constructor(context: Context) : super(context) {
        adjustPosition(context)
        setup(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        adjustPosition(context)
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs,
            R.styleable.com_ternaryop_widget_WaitingResultSwipeRefreshLayout, 0, 0)
        try {
            val colorSchemeId = a.getResourceId(R.styleable.com_ternaryop_widget_WaitingResultSwipeRefreshLayout_colorScheme, 0)
            if (colorSchemeId != 0) {
                setColorScheme(colorSchemeId)
            }
        } finally {
            a.recycle()
        }
    }

    fun setColorScheme(arrayResId: Int) {
        val colorScheme = resources.obtainTypedArray(arrayResId)
        setColorSchemeResources(
            colorScheme.getResourceId(0, 0),
            colorScheme.getResourceId(1, 0),
            colorScheme.getResourceId(2, 0),
            colorScheme.getResourceId(3, 0)
        )
        colorScheme.recycle()
    }

    fun setRefreshingAndWaitingResult(refreshing: Boolean) {
        isRefreshing = refreshing
        isWaitingResult = refreshing
    }

    /**
     * Starting since android.support.v4 v 21.0.0 the swipe refresh is shown under the toolbar (ie not visible!!)
     * so we adjust its position based on actionBar size
     * @param context the context to use
     */
    private fun adjustPosition(context: Context) {
        val typedValue = TypedValue()
        if (context.theme.resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            setProgressViewOffset(false, 0, resources.getDimensionPixelSize(typedValue.resourceId))
        }
    }
}

package com.ternaryop.widget

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.util.TypedValue
import com.ternaryop.utils.R

/**
 * Created by dave on 13/09/14.
 * Add a flag to check if waiting result is in progress, the isRefreshing flag can't be used because it is set to true
 * before the onRefresh() is called so testing its value inside the onRefresh() is wrong
 */
class WaitingResultSwipeRefreshLayout : SwipeRefreshLayout {
    var isWaitingResult: Boolean = false

    constructor(context: Context) : super(context) {
        adjustPosition(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        adjustPosition(context)
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

    fun setRefreshingAndWaintingResult(refreshing: Boolean) {
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

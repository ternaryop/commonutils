package com.ternaryop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.LinearLayout

/**
 * Contains views which are highlighted (by default using fade animation) when the progress increments
 */
class ProgressHighlightViewLayout
constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    var progress: Int = 0
        private set
    var currentView: View? = null
        private set
    var progressViewListener: ProgressViewListener? = null
    var progressAnimation: Animation? = null

    fun startProgress() {
        progress = 0
        updateProgress()
    }

    fun incrementProgress() {
        if (progress < childCount) {
            ++progress
            updateProgress()
        }
    }

    private fun updateProgress() {
        unselectView()
        if (progress < childCount) {
            currentView = getChildAt(progress)
            selectView()
        }
    }

    private fun selectView() {
        currentView?.let {
            if (progressAnimation != null) {
                it.startAnimation(progressAnimation)
            }
            progressViewListener?.onSelectView(it)
        }
    }

    private fun unselectView() {
        currentView?.let {
            if (progressAnimation != null) {
                it.clearAnimation()
            }
            progressViewListener?.onUnselectView(it)
        }
    }

    fun stopProgress() {
        unselectView()
    }

    interface ProgressViewListener {
        fun onSelectView(view: View)
        fun onUnselectView(view: View)
    }
}
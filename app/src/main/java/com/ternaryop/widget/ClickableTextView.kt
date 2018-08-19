package com.ternaryop.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.ternaryop.utils.R

class ClickableTextView : AppCompatTextView, OnTouchListener {
    private var defaultColor: Int = 0
    private var defaultBackground: Drawable? = null
    private var defaultBackgroundColor: Int = 0
    private var clickedColor: Int = 0
    private var clickedBackgroundColor: Int = 0

    constructor(context: Context, attrs: AttributeSet,
        defStyle: Int) : super(context, attrs, defStyle) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context) : super(context) {
        setup(null)
    }

    private fun setup(attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs,
            R.styleable.com_ternaryop_widget_ClickableTextView, 0, 0)
        try {
            clickedColor = a.getColor(R.styleable.com_ternaryop_widget_ClickableTextView_clickedColor, Color.BLUE)
            clickedBackgroundColor = a.getColor(
                R.styleable.com_ternaryop_widget_ClickableTextView_clickedBackground, Color.MAGENTA)
        } finally {
            a.recycle()
        }
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (hasOnClickListeners()) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    defaultColor = textColors.defaultColor
                    // beckground instance is cached and to avoid problems
                    // we save only the color if it's a ColorDrawable instance
                    defaultBackground = background
                    if (defaultBackground is ColorDrawable) {
                        defaultBackgroundColor = (defaultBackground as ColorDrawable).color
                        defaultBackground = null
                    }
                    setTextColor(clickedColor)
                    setBackgroundColor(clickedBackgroundColor)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    setTextColor(defaultColor)
                    if (defaultBackground == null) {
                        setBackgroundColor(defaultBackgroundColor)
                    } else {
                        background = defaultBackground
                    }
                }
            }
        }
        // allow target view to handle click
        return false
    }
}

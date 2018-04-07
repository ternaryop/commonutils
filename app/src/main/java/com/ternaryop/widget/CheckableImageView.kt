package com.ternaryop.widget

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.widget.Checkable
import com.ternaryop.utils.R

class CheckableImageView : AppCompatImageView, Checkable {
    private var isChecked: Boolean = false
    private var checkedColorFilter: Int = 0

    constructor(context: Context) : super(context) {
        setup(null)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs,
            R.styleable.com_ternaryop_widget_CheckableImageView, 0, 0)
        try {
            checkedColorFilter = a.getColor(R.styleable.com_ternaryop_widget_CheckableImageView_checkedColorFilter,
                DEFAULT_CHECKED_COLOR_FILTER)
        } finally {
            a.recycle()
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        if (checked) {
            drawable.setColorFilter(checkedColorFilter, PorterDuff.Mode.SRC_ATOP)
        } else {
            drawable.colorFilter = null
        }
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    /**
     * Using TouchListener listener doesn't work everytime,
     * for example with gridview_photo_picker_item the color doens't always change
     * if the action == MotionEvent.ACTION_DOWN returns false then the MotionEvent.ACTION_UP
     * isn't called (this is the documented behavior) and the image stay highlighted
     * if the action == MotionEvent.ACTION_DOWN returns true the onClickListener
     * isn't called (this is the documented behavior)
     * @param pressed true if pressed, false otherwise
     */
    override fun setPressed(pressed: Boolean) {
        if (!isChecked()) {
            if (pressed) {
                drawable.setColorFilter(DEFAULT_PRESSED_COLOR, PorterDuff.Mode.SRC_ATOP)
                invalidate()
            } else {
                drawable.clearColorFilter()
                invalidate()
            }
        }
        super.setPressed(pressed)
    }

    companion object {
        private const val DEFAULT_CHECKED_COLOR_FILTER = -0x78000000
        private const val DEFAULT_PRESSED_COLOR = 0x77000000
    }
}

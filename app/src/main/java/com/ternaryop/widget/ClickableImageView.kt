package com.ternaryop.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ClickableImageView : AppCompatImageView {
    constructor(context: Context, attrs: AttributeSet,
        defStyle: Int) : super(context, attrs, defStyle) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    /**
     * Using TouchListener listener doesn't work everytime,
     * for example with gridview_photo_picker_item the color doens't always change
     * if the action == MotionEvent.ACTION_DOWN returns false then
     * the MotionEvent.ACTION_UP isn't called (this is the documented behavior) and the image stay highlighted
     * if the action == MotionEvent.ACTION_DOWN returns true
     * the onClickListener isn't called (this is the documented behavior)
     * @param pressed true if pressed, false otherwise
     */
    override fun setPressed(pressed: Boolean) {
        if (pressed) {
            drawable.srcAtop(DEFAULT_PRESSED_COLOR)
            invalidate()
        } else {
            drawable.clearColorFilter()
            invalidate()
        }
        super.setPressed(pressed)
    }

    companion object {
        private const val DEFAULT_PRESSED_COLOR = 0x77000000
    }
}

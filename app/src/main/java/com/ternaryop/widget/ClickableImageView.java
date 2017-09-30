package com.ternaryop.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

public class ClickableImageView extends android.support.v7.widget.AppCompatImageView {

    public ClickableImageView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public ClickableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableImageView(Context context) {
        super(context);
    }

    /**
     * Using TouchListener listener doesn't work everytime, for example with gridview_photo_picker_item the color doens't always change
     * if the action == MotionEvent.ACTION_DOWN returns false then the MotionEvent.ACTION_UP isn't called (this is the documented behavior) and the image stay highlighted
     * if the action == MotionEvent.ACTION_DOWN returns true the onClickListener isn't called (this is the documented behavior)
     * @param pressed true if pressed, false otherwise
     */
    @Override
    public void setPressed(boolean pressed) {
        if (pressed) {
            getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
            invalidate();
        } else {
            getDrawable().clearColorFilter();
            invalidate();
        }
        super.setPressed(pressed);
    }
}

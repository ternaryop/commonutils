package com.ternaryop.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.ternaryop.utils.R;

public class CheckableImageView extends android.support.v7.widget.AppCompatImageView implements Checkable {
    public static final int DEFAULT_CHECKED_COLOR_FILTER = 0x88000000;
    public static final int DEFAULT_PRESSED_COLOR = 0x77000000;
    private boolean isChecked;
    private int checkedColorFilter;

    public CheckableImageView(Context context) {
        super(context);
        setup(null);
    }

    public CheckableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(attrs);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    private void setup(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.com_ternaryop_widget_CheckableImageView, 0, 0);
        try {
            checkedColorFilter = a.getColor(R.styleable.com_ternaryop_widget_CheckableImageView_checkedColorFilter,
                    DEFAULT_CHECKED_COLOR_FILTER);
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked) {
            getDrawable().setColorFilter(checkedColorFilter, PorterDuff.Mode.SRC_ATOP);
        } else {
            getDrawable().setColorFilter(null);
        }
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
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
    @Override
    public void setPressed(boolean pressed) {
        if (!isChecked()) {
            if (pressed) {
                getDrawable().setColorFilter(DEFAULT_PRESSED_COLOR, PorterDuff.Mode.SRC_ATOP);
                invalidate();
            } else {
                getDrawable().clearColorFilter();
                invalidate();
            }
        }
        super.setPressed(pressed);
    }
}

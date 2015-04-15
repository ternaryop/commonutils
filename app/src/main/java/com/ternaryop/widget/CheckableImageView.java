package com.ternaryop.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

public class CheckableImageView extends ImageView implements Checkable {
    public static final String PACKAGE_NAME = "http://schemas.android.com/apk/res-auto";
    public static final int DEFAULT_CHECKED_COLOR_FILTER = 0x88000000;
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
        if (attrs == null) {
            checkedColorFilter = DEFAULT_CHECKED_COLOR_FILTER;
        } else {
            checkedColorFilter = attrs.getAttributeIntValue(PACKAGE_NAME, "checkedColorFilter", DEFAULT_CHECKED_COLOR_FILTER);
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

}

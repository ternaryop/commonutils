package com.ternaryop.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class ClickableTextView extends TextView implements OnTouchListener {

    private int defaultColor;
    private Drawable defaultBackground;
    private int defaultBackgroundColor;
    private int clickedColor;
    private int clickedBackgroundColor;
    public static final String PACKAGE_NAME = "http://schemas.android.com/apk/res-auto";

    public ClickableTextView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        setup(attrs);
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public ClickableTextView(Context context) {
        super(context);
        setup(null);
    }

    private void setup(AttributeSet attrs) {
        if (attrs == null) {
            clickedColor = Color.BLUE;
            clickedBackgroundColor = Color.WHITE;
        } else {
            clickedColor = attrs.getAttributeIntValue(PACKAGE_NAME, "clickedColor", Color.BLUE);
            clickedBackgroundColor = attrs.getAttributeIntValue(PACKAGE_NAME, "clickedBackground", Color.WHITE);
        }
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (hasOnClickListeners()) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                defaultColor = getTextColors().getDefaultColor();
                // beckground instance is cached and to avoid problems
                // we save only the color if it's a ColorDrawable instance
                defaultBackground = getBackground();
                if (defaultBackground instanceof ColorDrawable) {
                    defaultBackgroundColor = ((ColorDrawable) defaultBackground).getColor();
                    defaultBackground = null;
                }
                setTextColor(clickedColor);
                setBackgroundColor(clickedBackgroundColor);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setTextColor(defaultColor);
                if (defaultBackground == null) {
                    setBackgroundColor(defaultBackgroundColor);
                } else {
                    setBackground(defaultBackground);
                }
                break;
            }
        }

        // allow target view to handle click
        return false;
    }
}

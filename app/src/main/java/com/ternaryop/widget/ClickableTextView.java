package com.ternaryop.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.ternaryop.utils.R;

public class ClickableTextView extends android.support.v7.widget.AppCompatTextView implements OnTouchListener {

    private int defaultColor;
    private Drawable defaultBackground;
    private int defaultBackgroundColor;
    private int clickedColor;
    private int clickedBackgroundColor;

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
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.com_ternaryop_widget_ClickableTextView, 0, 0);
        try {
            clickedColor = a.getColor(R.styleable.com_ternaryop_widget_ClickableTextView_clickedColor, Color.BLUE);
            clickedBackgroundColor = a.getColor(
                    R.styleable.com_ternaryop_widget_ClickableTextView_clickedBackground, Color.MAGENTA);
        } finally {
            a.recycle();
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

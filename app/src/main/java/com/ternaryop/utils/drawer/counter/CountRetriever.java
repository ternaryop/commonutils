package com.ternaryop.utils.drawer.counter;

import android.widget.TextView;

public interface CountRetriever {
    public void updateCount(TextView textView);
    public Long getCount();
}

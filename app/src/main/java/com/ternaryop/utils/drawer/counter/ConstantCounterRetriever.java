package com.ternaryop.utils.drawer.counter;

import android.view.View;
import android.widget.TextView;

/**
 * Created by dave on 31/12/14.
 * Contains a constant value, after creation it never updates
*/
public class ConstantCounterRetriever implements CountRetriever {
    private final Long count;

    public ConstantCounterRetriever(long count) {
        this.count = count;
    }

    @Override
    public void updateCount(TextView textView) {
        if (count > 0) {
            textView.setText(count.toString());
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Long getCount() {
        return count;
    }
}

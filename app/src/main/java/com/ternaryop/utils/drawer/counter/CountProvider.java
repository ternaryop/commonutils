package com.ternaryop.utils.drawer.counter;

public interface CountProvider {
    public void setCountChangedListener(CountChangedListener countChangedListener);
    public CountChangedListener getCountChangedListener();
}

package com.ternaryop.utils.drawer.counter;

@Deprecated
public interface CountProvider {
    public void setCountChangedListener(CountChangedListener countChangedListener);
    public CountChangedListener getCountChangedListener();
}

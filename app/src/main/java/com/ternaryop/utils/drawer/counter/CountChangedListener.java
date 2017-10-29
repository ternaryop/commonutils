package com.ternaryop.utils.drawer.counter;

@Deprecated
public interface CountChangedListener {
    public void onChangeCount(CountProvider provider, long newCount);
}

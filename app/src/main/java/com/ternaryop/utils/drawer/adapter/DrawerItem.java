package com.ternaryop.utils.drawer.adapter;

import android.app.Fragment;
import android.content.Context;

import com.ternaryop.utils.drawer.counter.CountRetriever;

public class DrawerItem {
    public final static DrawerItemDivider DRAWER_ITEM_DIVIDER = new DrawerItemDivider();
    private String title;
    private Class<? extends Fragment> fragmentClass;
    private boolean counterVisible;
    private CountRetriever countRetriever;
    private final boolean isHeader;
    private final boolean isDivider;

    public DrawerItem() {
        this(null, null, false, null);
    }

    public DrawerItem(String title) {
        this(title, null, false, null);
    }
    
    public DrawerItem(String title, Class<? extends Fragment> fragmentClass) {
        this(title, fragmentClass, false, null);
    }

    public DrawerItem(String title, Class<? extends Fragment> fragmentClass, boolean showCounter, CountRetriever countRetriever) {
        this.title = title;
        this.fragmentClass = fragmentClass;
        this.counterVisible = showCounter;
        this.countRetriever = countRetriever;
        this.isHeader = fragmentClass == null && title != null;
        this.isDivider = fragmentClass == null && title == null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isDivider() {
        return isDivider;
    }

    public boolean isCounterVisible() {
        return counterVisible;
    }

    public void setCounterVisible(boolean showCounter) {
        this.counterVisible = showCounter;
    }

    public CountRetriever getCountRetriever() {
        return countRetriever;
    }

    public void setCountRetriever(CountRetriever countRetriever) {
        this.countRetriever = countRetriever;
    }

    public Fragment instantiateFragment(Context context) {
        return Fragment.instantiate(context, getFragmentClass().getName());
    }

    public static class DrawerItemDivider extends DrawerItem {
        @Override
        public void setTitle(String title) {
        }

        @Override
        public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        }

        @Override
        public void setCounterVisible(boolean showCounter) {
        }

        @Override
        public void setCountRetriever(CountRetriever countRetriever) {
        }
    }
}
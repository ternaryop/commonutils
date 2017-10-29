package com.ternaryop.utils.drawer.adapter;

import android.app.Fragment;
import android.content.Context;

import com.ternaryop.utils.drawer.counter.CountRetriever;

public class DrawerItem {
    public final static DrawerItemDivider DRAWER_ITEM_DIVIDER = new DrawerItemDivider();
    private int itemId;
    private String title;
    private Class<? extends Fragment> fragmentClass;
    private boolean counterVisible;
    private CountRetriever countRetriever;
    private final boolean isHeader;
    private final boolean isDivider;
    private String badge;

    public DrawerItem() {
        this(-1, null, null, false);
    }

    public DrawerItem(int itemId, String title) {
        this(itemId, title, null, false);
    }
    
    public DrawerItem(int itemId, String title, Class<? extends Fragment> fragmentClass) {
        this(itemId, title, fragmentClass, false);
    }

    public DrawerItem(int itemId, String title, Class<? extends Fragment> fragmentClass, boolean showCounter) {
        this(title, fragmentClass, showCounter, null);
        this.itemId = itemId;
    }

    @Deprecated
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

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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

        @Override
        public void setBadge(String badge) {
        }

        @Override
        public void setItemId(int itemId) {
        }
    }
}
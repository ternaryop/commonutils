package com.ternaryop.utils.drawer.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ternaryop.utils.R;
import com.ternaryop.utils.drawer.adapter.DrawerAdapter;
import com.ternaryop.utils.drawer.adapter.DrawerItem;


public abstract class DrawerActionBarActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout drawerLinearLayout;

    private ListView drawerList;

    // nav drawer title
    private CharSequence drawerTitle;

    // used to store app title
    private CharSequence title;
    private CharSequence subtitle;

    private int lastClickedMenuIndex = -1;
    // used to update subtitle only if the drawer closed without change selected menu
    private boolean selectedMenuChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutResId());
        setupActionBar();

        title = drawerTitle = getTitle();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLinearLayout = (LinearLayout) findViewById(R.id.drawer_frame);

        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(this);
    }

    protected int getActivityLayoutResId() {
        return R.layout.activity_drawer;
    }

    protected abstract DrawerAdapter initDrawerAdapter();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDrawerSlide(View view, float v) {
    }

    @Override
    public void onDrawerOpened(View view) {
        selectedMenuChanged = false;
        getSupportActionBar().setTitle(drawerTitle);
        // save current subtitle
        subtitle = getSupportActionBar().getSubtitle();
        getSupportActionBar().setSubtitle(null);
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(View view) {
        getSupportActionBar().setTitle(title);
        if (!selectedMenuChanged) {
            getSupportActionBar().setSubtitle(subtitle);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerStateChanged(int i) {
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectClickedItem(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Select menu item from clicked position in drawer list.
     * If the position is valid and differs from the current one then call setDrawerListSelection and onDrawerItemSelected
     * @param position the item postion
     * @return true if new menu item has been selected, false otherwise
     */
    protected boolean selectClickedItem(int position) {
        if (lastClickedMenuIndex == position) {
            return false;
        }
        if (position < 0 || getAdapter().getCount() < position) {
            return false;
        }
        lastClickedMenuIndex = position;
        selectedMenuChanged = true;

        setDrawerListSelection(position);
        closeDrawer();
        onDrawerItemSelected(getAdapter().getItem(position));
        return true;
    }

    protected void setDrawerListSelection(int position) {
        final ListView list = getDrawerList();

        if (position < 0 || list.getAdapter().getCount() < position) {
            return;
        }
        list.setItemChecked(position, true);
        list.setSelection(position);
        setTitle(getAdapter().getItem(position).getTitle());
        getSupportActionBar().setSubtitle(null);
    }

    /**
     * The drawer item selected can be used to create fragment
     * @param drawerItem the selected item
     */
    protected abstract void onDrawerItemSelected(DrawerItem drawerItem);

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getSupportActionBar().setTitle(title);
    }

    protected Toolbar setupActionBar() {
        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);

        return toolbar;
    }

    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(drawerLinearLayout);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(drawerLinearLayout);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(drawerLinearLayout);
    }

    public void rebuildDrawerMenu() {
        drawerList.setAdapter(initDrawerAdapter());
    }

    public DrawerAdapter getAdapter() {
        return (DrawerAdapter) drawerList.getAdapter();
    }

    public ListView getDrawerList() {
        return drawerList;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    public abstract Toolbar getToolbar();
}

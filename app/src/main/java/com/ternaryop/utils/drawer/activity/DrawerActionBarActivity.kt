package com.ternaryop.utils.drawer.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.ternaryop.utils.R
import com.ternaryop.utils.drawer.adapter.DrawerAdapter
import com.ternaryop.utils.drawer.adapter.DrawerItem

abstract class DrawerActionBarActivity : AppCompatActivity(), DrawerLayout.DrawerListener {
    private lateinit var drawerLayout: DrawerLayout
    lateinit var drawerToggle: ActionBarDrawerToggle
        private set
    private lateinit var drawerLinearLayout: LinearLayout
    lateinit var drawerList: ListView
        private set
    // nav drawer title
    private lateinit var drawerTitle: CharSequence
    // used to store app title
    private var appTitle: CharSequence? = null
    private var subtitle: CharSequence? = null
    private var lastClickedMenuIndex = -1
    // used to update subtitle only if the drawer closed without change selected menu
    private var selectedMenuChanged: Boolean = false
    open val activityLayoutResId: Int
        get() = R.layout.activity_drawer
    val isDrawerOpen: Boolean
        get() = drawerLayout.isDrawerOpen(drawerLinearLayout)
    val adapter: DrawerAdapter
        get() = drawerList.adapter as DrawerAdapter
    abstract val toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLayoutResId)
        setupActionBar()

        drawerTitle = title
        appTitle = drawerTitle

        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawerLinearLayout = findViewById<View>(R.id.drawer_frame) as LinearLayout

        drawerList = findViewById<View>(R.id.drawer_list) as ListView
        drawerList.onItemClickListener = DrawerItemClickListener()
        // enabling action bar app icon and behaving it as toggle button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close)
        drawerLayout.addDrawerListener(this)
    }

    protected abstract fun initDrawerAdapter(): DrawerAdapter

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onDrawerSlide(view: View, v: Float) {}

    override fun onDrawerOpened(view: View) {
        selectedMenuChanged = false
        supportActionBar!!.title = drawerTitle
        // save current subtitle
        subtitle = supportActionBar!!.subtitle
        supportActionBar!!.subtitle = null
        invalidateOptionsMenu()
    }

    override fun onDrawerClosed(view: View) {
        supportActionBar?.let { supportActionBar ->
            supportActionBar.title = appTitle
            if (!selectedMenuChanged) {
                supportActionBar.subtitle = subtitle
            }
        }
        invalidateOptionsMenu()
    }

    override fun onDrawerStateChanged(i: Int) {}

    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectClickedItem(position)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }

    /**
     * Select menu item from clicked position in drawer list.
     * If the position is valid and differs from the current one then call setDrawerListSelection and onDrawerItemSelected
     * @param position the item postion
     * @return true if new menu item has been selected, false otherwise
     */
    protected fun selectClickedItem(position: Int): Boolean {
        if (lastClickedMenuIndex == position) {
            return false
        }
        if (position < 0 || adapter.count < position) {
            return false
        }
        lastClickedMenuIndex = position
        selectedMenuChanged = true

        setDrawerListSelection(position)
        closeDrawer()
        adapter.getItem(position)?.also { onDrawerItemSelected(it) }
        return true
    }

    protected fun setDrawerListSelection(position: Int) {
        val list = drawerList

        if (position < 0 || list.adapter.count < position) {
            return
        }
        list.setItemChecked(position, true)
        list.setSelection(position)
        title = adapter.getItem(position)?.title
        supportActionBar?.subtitle = null
    }

    /**
     * The drawer item selected can be used to create fragment
     * @param drawerItem the selected item
     */
    abstract fun onDrawerItemSelected(drawerItem: DrawerItem)

    override fun setTitle(title: CharSequence?) {
        this.appTitle = title
        supportActionBar?.title = title
    }

    protected fun setupActionBar(): Toolbar {
        val toolbar = toolbar
        setSupportActionBar(toolbar)

        return toolbar
    }

    fun openDrawer() {
        drawerLayout.openDrawer(drawerLinearLayout)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(drawerLinearLayout)
    }

    fun rebuildDrawerMenu() {
        drawerList.adapter = initDrawerAdapter()
    }
}

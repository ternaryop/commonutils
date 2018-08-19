package com.ternaryop.utils.drawer.adapter

import android.content.Context
import androidx.fragment.app.Fragment

open class DrawerItem constructor(open var itemId: Int = -1,
    open var title: String? = null,
    open var fragmentClass: Class<out Fragment>? = null,
    open var isCounterVisible: Boolean = false) {
    val isHeader: Boolean
        get() =  fragmentClass == null && title != null
    val isDivider: Boolean
        get() = fragmentClass == null && title == null
    open var badge: String? = null

    open fun instantiateFragment(context: Context): Fragment {
        return Fragment.instantiate(context, fragmentClass!!.name)
    }

    @Suppress("UNUSED_PARAMETER")
    class DrawerItemDivider : DrawerItem() {
        override var title: String?
            get() = super.title
            set(title) {}
        override var fragmentClass: Class<out Fragment>?
            get() = super.fragmentClass
            set(fragmentClass) {}
        override var isCounterVisible: Boolean
            get() = super.isCounterVisible
            set(showCounter) {}
        override var badge: String?
            get() = super.badge
            set(badge) {}
        override var itemId: Int
            get() = super.itemId
            set(itemId) {}
    }

    companion object {
        val DRAWER_ITEM_DIVIDER = DrawerItemDivider()
    }
}
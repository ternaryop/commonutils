package com.ternaryop.utils.drawer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ternaryop.utils.R

class DrawerAdapter(context: Context) : ArrayAdapter<DrawerItem>(context, 0) {
    var isSelectionEnabled: Boolean = false
    private var inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflatedView: View
        val item = getItem(position)!!
        val holder: ViewHolder

        if (convertView == null) {
            inflatedView = when {
                item.isHeader -> inflater!!.inflate(R.layout.drawer_header_list_item, parent, false)
                item.isDivider -> inflater!!.inflate(R.layout.drawer_divider_list_item, parent, false)
                else -> inflater!!.inflate(R.layout.drawer_list_item, parent, false)
            }
            holder = ViewHolder(inflatedView)
            inflatedView.tag = holder
        } else {
            inflatedView = convertView
            holder = inflatedView.tag as ViewHolder
        }
        // the header hasn't title
        holder.title?.text = item.title

        // the header hasn't counter
        holder.counter?.let { counter ->
            counter.visibility = View.GONE
            if (item.isCounterVisible) {
                if (item.badge != null && item.badge!!.isNotBlank()) {
                    counter.text = item.badge.toString()
                    counter.visibility = View.VISIBLE
                }
            }
        }
        return inflatedView
    }

    override fun isEnabled(position: Int): Boolean {
        return isSelectionEnabled && !getItem(position)!!.isHeader
    }

    override fun getItemViewType(position: Int): Int {
        val drawerItem = super.getItem(position)
        if (drawerItem!!.isHeader) {
            return 0
        } else if (drawerItem.isDivider) {
            return 1
        }
        return 2
    }

    override fun getViewTypeCount(): Int {
        // header, divider and normal row
        return 3
    }

    private inner class ViewHolder internal constructor(view: View) {
        val title = view.findViewById<View>(android.R.id.text1) as? TextView
        val counter = view.findViewById<View>(R.id.counter) as? TextView
    }

    fun getItemById(id: Int): DrawerItem? {
        for (i in 0 until count) {
            val item = getItem(i)!!

            if (item.itemId == id) {
                return item
            }
        }
        return null
    }
}

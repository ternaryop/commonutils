package com.ternaryop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.RelativeLayout

/**
 * Used to select items in listView
 *
 * @author dave
 */
class CheckableRelativeLayout : RelativeLayout, Checkable {
    private var isChecked: Boolean = false
    private var checkableViews = mutableListOf<Checkable>()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        isSelected = isChecked
        for (c in checkableViews) {
            c.isChecked = checked
        }
    }

    override fun toggle() {
        isChecked = !isChecked
        isSelected = isChecked
        for (c in checkableViews) {
            c.toggle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val childCount = this.childCount
        for (i in 0 until childCount) {
            findCheckableChildren(this.getChildAt(i))
        }
    }

    private fun findCheckableChildren(v: View) {
        if (v is Checkable) {
            checkableViews.add(v as Checkable)
        }
        if (v is ViewGroup) {
            val vg = v
            val childCount = vg.childCount
            for (i in 0 until childCount) {
                findCheckableChildren(vg.getChildAt(i))
            }
        }
    }
}
package com.ternaryop.utils.recyclerview

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by dave on 16/12/17.
 * Item swiping
 * https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
 */
abstract class SwipeCallback(
    private val defaultIcon: Drawable,
    private val defaultBackground: Drawable,
    swipeDirs: Int = ItemTouchHelper.LEFT
) : ItemTouchHelper.SimpleCallback(0, swipeDirs) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ) = false

    /**
     * Called inside onChildDraw before drawing
     * This is the right place to changed the drawable
     */
    protected open fun prepareIconBeforeDraw(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Drawable? = defaultIcon

    protected open fun prepareBackgroundBeforeDraw(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Drawable? = defaultBackground

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        prepareBackgroundBeforeDraw(recyclerView, viewHolder)?.also {
            drawBackground(it, itemView, dX, c)
        }

        prepareIconBeforeDraw(recyclerView, viewHolder)?.also {
            drawIcon(it, itemView, c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawIcon(
        icon: Drawable,
        itemView: View,
        c: Canvas
    ) {
        val intrinsicWidth = icon.intrinsicWidth
        val intrinsicHeight = icon.intrinsicHeight
        val itemHeight = itemView.bottom - itemView.top

        // Calculate position of icon
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + intrinsicHeight

        // Draw the icon
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)
    }

    private fun drawBackground(
        background: Drawable,
        itemView: View,
        dX: Float,
        c: Canvas
    ) {
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)
    }
}

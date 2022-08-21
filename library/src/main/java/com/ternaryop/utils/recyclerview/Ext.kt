package com.ternaryop.utils.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.scrollItemOnTopByPosition(position: Int) {
    // offset set to 0 put the item to the top
    (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
}

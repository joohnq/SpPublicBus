package com.joohnq.sppublicbus.common.helper

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewHelper {
    fun setupRecyclerViewWithoutScroll(
        recyclerView: RecyclerView,
        context: Context,
    ): RecyclerView {
        return recyclerView.apply {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        }
    }
}
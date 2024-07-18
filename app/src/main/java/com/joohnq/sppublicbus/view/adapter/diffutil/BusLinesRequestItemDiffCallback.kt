package com.joohnq.sppublicbus.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.joohnq.sppublicbus.model.entity.BusLinesRequestItem

class BusLinesRequestItemDiffCallback : DiffUtil.ItemCallback<BusLinesRequestItem>() {
    override fun areItemsTheSame(
        oldItem: BusLinesRequestItem,
        newItem: BusLinesRequestItem
    ): Boolean {
        return oldItem.cl == newItem.cl
    }

    override fun areContentsTheSame(
        oldItem: BusLinesRequestItem,
        newItem: BusLinesRequestItem
    ): Boolean {
        return oldItem == newItem
    }
}

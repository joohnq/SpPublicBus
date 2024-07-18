package com.joohnq.sppublicbus.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.joohnq.sppublicbus.model.entity.BusLines

class BusLinesItemDiffCallback : DiffUtil.ItemCallback<BusLines>() {
    override fun areItemsTheSame(
        oldItem: BusLines,
        newItem: BusLines
    ): Boolean {
        return oldItem.cl == newItem.cl
    }

    override fun areContentsTheSame(
        oldItem: BusLines,
        newItem: BusLines
    ): Boolean {
        return oldItem == newItem
    }
}

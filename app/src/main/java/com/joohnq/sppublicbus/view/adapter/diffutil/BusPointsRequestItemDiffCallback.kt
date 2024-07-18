package com.joohnq.sppublicbus.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.joohnq.sppublicbus.model.entity.BusStop

class BusPointsRequestItemDiffCallback : DiffUtil.ItemCallback<BusStop>() {
    override fun areItemsTheSame(
        oldItem: BusStop,
        newItem: BusStop
    ): Boolean {
        return oldItem.cp == newItem.cp
    }

    override fun areContentsTheSame(
        oldItem: BusStop,
        newItem: BusStop
    ): Boolean {
        return oldItem == newItem
    }
}

package com.joohnq.sppublicbus.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.joohnq.sppublicbus.model.entity.StreetRunner

class RunnersRequestItemDiffCallback : DiffUtil.ItemCallback<StreetRunner>() {
    override fun areItemsTheSame(
        oldItem: StreetRunner,
        newItem: StreetRunner
    ): Boolean {
        return oldItem.cc == newItem.cc
    }

    override fun areContentsTheSame(
        oldItem: StreetRunner,
        newItem: StreetRunner
    ): Boolean {
        return oldItem == newItem
    }
}

package com.joohnq.sppublicbus.model.entity

import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.view.entity.CustomMarker

data class BusStop(
    val cp: Int,
    val ed: String,
    val np: String,
    val px: Double,
    val py: Double
)

fun BusStop.toCustomMarker(): CustomMarker<BusStop> {
    return CustomMarker(
        la = this.py,
        lo = this.px,
        title = this.np,
        icon = R.drawable.ic_bus_stop_marker,
        item = this,
    )
}

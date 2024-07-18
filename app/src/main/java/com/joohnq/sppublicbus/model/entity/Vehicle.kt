package com.joohnq.sppublicbus.model.entity

import com.joohnq.sppublicbus.R
import com.joohnq.sppublicbus.view.entity.CustomMarker

data class Vehicle(
    val a: Boolean,
    val p: Int,
    val px: Double,
    val py: Double,
    val ta: String
)

fun Vehicle.toCustomMarker(): CustomMarker<Vehicle> {
    return CustomMarker(
        la = this.py,
        lo = this.px,
        title = this.p.toString(),
        icon = R.drawable.ic_bus_marker,
        item = this,
    )
}
